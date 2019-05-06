/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 12, 2014, 3:47:45 PM (GMT)]
 */
package vazkii.botania.common.entity;

import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibEntityNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntityDoppleganger extends EntityLiving implements IBotaniaBoss, IEntityAdditionalSpawnData {

	public static final float ARENA_RANGE = 12F;
	public static final int ARENA_HEIGHT = 5;

	private static final int SPAWN_TICKS = 160;
	private static final float MAX_HP = 320F;

	private static final int MOB_SPAWN_START_TICKS = 20;
	private static final int MOB_SPAWN_END_TICKS = 80;
	private static final int MOB_SPAWN_BASE_TICKS = 800;
	private static final int MOB_SPAWN_TICKS = MOB_SPAWN_BASE_TICKS + MOB_SPAWN_START_TICKS + MOB_SPAWN_END_TICKS;
	private static final int MOB_SPAWN_WAVES = 10;
	private static final int MOB_SPAWN_WAVE_TIME = MOB_SPAWN_BASE_TICKS / MOB_SPAWN_WAVES;

	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourcesZ";
	private static final String TAG_MOB_SPAWN_TICKS = "mobSpawnTicks";
	private static final String TAG_HARD_MODE = "hardMode";
	private static final String TAG_PLAYER_COUNT = "playerCount";

	private static final DataParameter<Integer> INVUL_TIME = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.VARINT);

	private static final List<BlockPos> PYLON_LOCATIONS = ImmutableList.of(
			new BlockPos(4, 1, 4),
			new BlockPos(4, 1, -4),
			new BlockPos(-4, 1, 4),
			new BlockPos(-4, 1, -4)
	);

	private static final List<ResourceLocation> CHEATY_BLOCKS = Arrays.asList(
		new ResourceLocation("openblocks", "beartrap"),
		new ResourceLocation("thaumictinkerer", "magnet")
	);

	private boolean spawnLandmines = false;
	private boolean spawnPixies = false;
	private boolean anyWithArmor = false;
	private boolean aggro = false;
	private int tpDelay = 0;
	private int mobSpawnTicks = 0;
	private int playerCount = 0;
	private boolean hardMode = false;
	private BlockPos source = BlockPos.ORIGIN;
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity." + LibEntityNames.DOPPLEGANGER_REGISTRY + ".name"), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS).setCreateFog(true);;
	private UUID bossInfoUUID = bossInfo.getUniqueId();
	public EntityPlayer trueKiller = null;

	public EntityDoppleganger(World world) {
		super(world);
		setSize(0.6F, 1.8F);
		isImmuneToFire = true;
		experienceValue = 825;
		if(world.isRemote) {
			Botania.proxy.addBoss(this);
		}
	}

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos p : PYLON_LOCATIONS)
			mb.addComponent(p.up(), ModBlocks.pylon.getDefaultState().withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.GAIA));

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				mb.addComponent(new BeaconComponent(new BlockPos(i - 1, 0, j - 1)));

		mb.addComponent(new BeaconBeamComponent(new BlockPos(0, 1, 0)));
		mb.setRenderOffset(new BlockPos(0, -1, 0));

		return mb.makeSet();
	}

	public static boolean spawn(EntityPlayer player, ItemStack stack, World world, BlockPos pos, boolean hard) {
		//initial checks
		if(
			!(world.getTileEntity(pos) instanceof TileEntityBeacon) || 
			!isTruePlayer(player) ||
			countGaiaGuardiansAround(world, pos) > 0
		)
			return false;
		
		//check difficulty
		if(world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if(!world.isRemote)
				player.sendMessage(new TextComponentTranslation("botaniamisc.peacefulNoob").setStyle(new Style().setColor(TextFormatting.RED)));
			return false;
		}

		//check pylons
		List<BlockPos> invalidPylonBlocks = checkPylons(world, pos);
		if(!invalidPylonBlocks.isEmpty()) {
			if(world.isRemote) {
				warnInvalidBlocks(invalidPylonBlocks);
			} else {
				player.sendMessage(new TextComponentTranslation("botaniamisc.needsCatalysts").setStyle(new Style().setColor(TextFormatting.RED)));
			}

			return false;
		}

		//check arena shape
		List<BlockPos> invalidArenaBlocks = checkArena(world, pos);
		if(!invalidArenaBlocks.isEmpty()) {
			if(world.isRemote) {
				warnInvalidBlocks(invalidArenaBlocks);
			} else {
				PacketHandler.sendTo((EntityPlayerMP) player,
					new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.ARENA_INDICATOR, pos.getX(), pos.getY(), pos.getZ()));

				player.sendMessage(new TextComponentTranslation("botaniamisc.badArena").setStyle(new Style().setColor(TextFormatting.RED)));
			}

			return false;
		}

		//all checks ok, spawn the boss
		if(!world.isRemote) {
			stack.shrink(1);

			EntityDoppleganger e = new EntityDoppleganger(world);
			e.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
			e.setInvulTime(SPAWN_TICKS);
			e.setHealth(1F);
			e.source = pos;
			e.mobSpawnTicks = MOB_SPAWN_TICKS;
			e.hardMode = hard;

			int playerCount = e.getPlayersAround().size();
			e.playerCount = playerCount;
			e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP * playerCount);
			if(hard)
				e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).setBaseValue(15);

			e.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 10F, 0.1F);
			e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), null);
			world.spawnEntity(e);
		}
		
		return true;
	}

	private static List<BlockPos> checkPylons(World world, BlockPos beaconPos) {
		List<BlockPos> invalidPylonBlocks = new ArrayList<>();

		for(BlockPos coords : PYLON_LOCATIONS) {
			BlockPos pos_ = beaconPos.add(coords);
			
			IBlockState state = world.getBlockState(pos_);
			if(state.getBlock() != ModBlocks.pylon || state.getValue(BotaniaStateProps.PYLON_VARIANT) != PylonVariant.GAIA) {
				invalidPylonBlocks.add(pos_);
			}
		}

		return invalidPylonBlocks;
	}

	private static List<BlockPos> checkArena(World world, BlockPos beaconPos) {
		List<BlockPos> trippedPositions = new ArrayList<>();
		int range = (int) Math.ceil(ARENA_RANGE);
		BlockPos pos;

		for(int x = -range; x <= range; x++)
			for(int z = -range; z <= range; z++) {
				if(Math.abs(x) == 4 && Math.abs(z) == 4 || vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(x, z, 0, 0) > ARENA_RANGE)
					continue; // Ignore pylons and out of circle

				boolean hasFloor = false;

				for(int y = -2; y <= ARENA_HEIGHT; y++) {
					if(x == 0 && y == 0 && z == 0)
						continue; //the beacon

					pos = beaconPos.add(x, y, z);

					IBlockState state = world.getBlockState(pos);

					boolean allowBlockHere = y < 0;
					boolean isBlockHere = state.getCollisionBoundingBox(world, pos) != null;

					if(allowBlockHere && isBlockHere) //floor is here! good
						hasFloor = true;

					if(y == 0 && !hasFloor) //column is entirely missing floor
						trippedPositions.add(pos.down());

					if(!allowBlockHere && isBlockHere && !BotaniaAPI.gaiaBreakBlacklist.contains(state.getBlock())) //ceiling is obstructed in this column
						trippedPositions.add(pos);
				}
			}

		return trippedPositions;
	}

	private static void warnInvalidBlocks(Iterable<BlockPos> invalidPositions) {
		Botania.proxy.setWispFXDepthTest(false);
		for(BlockPos pos_ : invalidPositions) {
			Botania.proxy.wispFX(pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 1F, 0.2F, 0.2F, 0.5F, 0F, 8);
		}
		Botania.proxy.setWispFXDepthTest(true);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, ARENA_RANGE * 1.5F));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(INVUL_TIME, 0);
	}

	public int getInvulTime() {
		return dataManager.get(INVUL_TIME);
	}

	public BlockPos getSource() {
		return source;
	}

	public void setInvulTime(int time) {
		dataManager.set(INVUL_TIME, time);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound cmp) {
		super.writeEntityToNBT(cmp);
		cmp.setInteger(TAG_INVUL_TIME, getInvulTime());
		cmp.setBoolean(TAG_AGGRO, aggro);
		cmp.setInteger(TAG_MOB_SPAWN_TICKS, mobSpawnTicks);

		cmp.setInteger(TAG_SOURCE_X, source.getX());
		cmp.setInteger(TAG_SOURCE_Y, source.getY());
		cmp.setInteger(TAG_SOURCE_Z, source.getZ());

		cmp.setBoolean(TAG_HARD_MODE, hardMode);
		cmp.setInteger(TAG_PLAYER_COUNT, playerCount);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound cmp) {
		super.readEntityFromNBT(cmp);
		setInvulTime(cmp.getInteger(TAG_INVUL_TIME));
		aggro = cmp.getBoolean(TAG_AGGRO);
		mobSpawnTicks = cmp.getInteger(TAG_MOB_SPAWN_TICKS);

		int x = cmp.getInteger(TAG_SOURCE_X);
		int y = cmp.getInteger(TAG_SOURCE_Y);
		int z = cmp.getInteger(TAG_SOURCE_Z);
		source = new BlockPos(x, y, z);

		hardMode = cmp.getBoolean(TAG_HARD_MODE);
		if(cmp.hasKey(TAG_PLAYER_COUNT))
			playerCount = cmp.getInteger(TAG_PLAYER_COUNT);
		else playerCount = 1;

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void setCustomNameTag(@Nonnull String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void heal(float amount) {
		if(getInvulTime() == 0) {
			super.heal(amount);
		}
	}

	@Override
	public void onKillCommand() {
		this.setHealth(0.0F);
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float par2) {
		Entity e = source.getTrueSource();
		if (e instanceof EntityPlayer && isTruePlayer(e) && getInvulTime() == 0) {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			int cap = 25;
			return super.attackEntityFrom(source, Math.min(cap, par2));
		}

		return false;
	}

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");
	public static boolean isTruePlayer(Entity e) {
		if(!(e instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}

	@Override
	protected void damageEntity(@Nonnull DamageSource source, float par2) {
		super.damageEntity(source, par2);

		Entity attacker = source.getImmediateSource();
		if(attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.subtract(playerVector).normalize().multiply(0.75);

			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
				tpDelay = 4;
				spawnPixies = aggro;
			}

			aggro = true;
		}
	}

	@Override
	public void onDeath(@Nonnull DamageSource source) {
		super.onDeath(source);
		EntityLivingBase entitylivingbase = getAttackingEntity();
		if(entitylivingbase instanceof EntityPlayerMP && !anyWithArmor) {
			DopplegangerNoArmorTrigger.INSTANCE.trigger((EntityPlayerMP) entitylivingbase, this, source);
		}

		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1D, 0D, 0D);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public ResourceLocation getLootTable() {
		return new ResourceLocation(LibMisc.MOD_ID, hardMode ? "gaia_guardian_2" : "gaia_guardian");
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, @Nonnull DamageSource source) {
		// Save true killer, they get extra loot
		if (wasRecentlyHit && source.getTrueSource() instanceof EntityPlayer) {
			trueKiller = (EntityPlayer) source.getTrueSource();
		}

		// Drop equipment and clear it so multiple calls to super don't do it again
		super.dropEquipment(wasRecentlyHit, lootingModifier);

		for (EntityEquipmentSlot e : EntityEquipmentSlot.values()) {
			setItemStackToSlot(e, ItemStack.EMPTY);
		}

		// Generate loot table for every single attacking player
		for (UUID u : playersWhoAttacked) {
			EntityPlayer player = world.getPlayerEntityByUUID(u);
			if (player == null)
				continue;

			EntityPlayer saveLastAttacker = attackingPlayer;
			double savePosX = posX;
			double savePosY = posY;
			double savePosZ = posZ;

			attackingPlayer = player; // Fake attacking player as the killer
			posX = player.posX;       // Spoof pos so drops spawn at the player
			posY = player.posY;
			posZ = player.posZ;
			super.dropLoot(wasRecentlyHit, lootingModifier, DamageSource.causePlayerDamage(player));
			posX = savePosX;
			posY = savePosY;
			posZ = savePosZ;
			attackingPlayer = saveLastAttacker;
		}

		trueKiller = null;
	}

	@Override
	public void setDead() {
		if(world.isRemote) {
			Botania.proxy.removeBoss(this);
		}
		super.setDead();
	}

	public List<EntityPlayer> getPlayersAround() {
		float range = 15F;
		return world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range), player -> isTruePlayer(player) && !player.isSpectator());
	}

	private static int countGaiaGuardiansAround(World world, BlockPos source) {
		float range = 15F;
		List l = world.getEntitiesWithinAABB(EntityDoppleganger.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
		return l.size();
	}

	private void particles() {
		for(int i = 0; i < 360; i += 8) {
			float r = 0.6F;
			float g = 0F;
			float b = 0.2F;
			float m = 0.15F;
			float mv = 0.35F;

			float rad = i * (float) Math.PI / 180F;
			double x = source.getX() + 0.5 - Math.cos(rad) * ARENA_RANGE;
			double y = source.getY() + 0.5;
			double z = source.getZ() + 0.5 - Math.sin(rad) * ARENA_RANGE;

			Botania.proxy.wispFX(x, y, z, r, g, b, 0.5F, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
		}

		if(getInvulTime() > 10) {
			Vector3 pos = Vector3.fromEntityCenter(this).subtract(new Vector3(0, 0.2, 0));
			for(BlockPos arr : PYLON_LOCATIONS) {
				Vector3 pylonPos = new Vector3(source.getX() + arr.getX(), source.getY() + arr.getY(), source.getZ() + arr.getZ());
				double worldTime = ticksExisted;
				worldTime /= 5;

				float rad = 0.75F + (float) Math.random() * 0.05F;
				double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
				double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

				Vector3 partPos = new Vector3(xp, pylonPos.y, zp);
				Vector3 mot = pos.subtract(partPos).multiply(0.04);

				float r = 0.7F + (float) Math.random() * 0.3F;
				float g = (float) Math.random() * 0.3F;
				float b = 0.7F + (float) Math.random() * 0.3F;

				Botania.proxy.wispFX(partPos.x, partPos.y, partPos.z, r, g, b, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
				Botania.proxy.wispFX(partPos.x, partPos.y, partPos.z, r, g, b, 0.4F, (float) mot.x, (float) mot.y, (float) mot.z);
			}
		}
	}

	private void smashBlocksAround(int centerX, int centerY, int centerZ, int radius) {
		for(int dx = -radius; dx <= radius; dx++)
			for(int dy = -radius; dy <= radius + 1; dy++)
				for(int dz = -radius; dz <= radius; dz++) {
					int x = centerX + dx;
					int y = centerY + dy;
					int z = centerZ + dz;
					
					BlockPos pos = new BlockPos(x, y, z);
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					
					if(state.getBlockHardness(world, pos) == -1) continue;
					
					if(CHEATY_BLOCKS.contains(block.getRegistryName())) {
						world.destroyBlock(pos, true);
					} else {
						//don't break blacklisted blocks
						if(BotaniaAPI.gaiaBreakBlacklist.contains(block)) continue;
						//don't break the floor
						if(y < source.getY()) continue;
						//don't break blocks in pylon columns
						if(Math.abs(source.getX() - x) == 4 && Math.abs(source.getZ() - z) == 4) continue;
						
						world.destroyBlock(pos, true);
					}
				}
	}

	private void clearPotions(EntityPlayer player) {
		int posXInt = MathHelper.floor(posX);
		int posZInt = MathHelper.floor(posZ);

		List<Potion> potionsToRemove = player.getActivePotionEffects().stream()
				.filter(effect -> effect.getDuration() < 160 && effect.getIsAmbient() && !effect.getPotion().isBadEffect())
				.map(PotionEffect::getPotion)
				.distinct()
				.collect(Collectors.toList());

		potionsToRemove.forEach(potion -> {
			player.removePotionEffect(potion);
			((WorldServer) world).getPlayerChunkMap().getEntry(posXInt >> 4, posZInt >> 4).sendPacket(new SPacketRemoveEntityEffect(player.getEntityId(), potion));
		});
	}

	private void keepInsideArena(EntityPlayer player) {
		if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) {
			Vector3 sourceVector = new Vector3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
			Vector3 playerVector = Vector3.fromEntityCenter(player);
			Vector3 motion = sourceVector.subtract(playerVector).normalize();

			player.motionX = motion.x;
			player.motionY = 0.2;
			player.motionZ = motion.z;
			player.velocityChanged = true;
		}
	}

	private void spawnMobs(List<EntityPlayer> players) {
		for(int pl = 0; pl < playerCount; pl++) {
			for(int i = 0; i < 3 + world.rand.nextInt(2); i++) {
				EntityLiving entity = null;
				switch (world.rand.nextInt(2)) {
					case 0: {
						entity = new EntityZombie(world);
						if(world.rand.nextInt(hardMode ? 3 : 12) == 0) {
							entity = new EntityWitch(world);
						}
						break;
					}
					case 1: {
						entity = new EntitySkeleton(world);
						if(world.rand.nextInt(8) == 0) {
							entity = new EntityWitherSkeleton(world);
						}
						break;
					}
					case 3: {
						if(!players.isEmpty()) {
							for(int j = 0; j < 1 + world.rand.nextInt(hardMode ? 8 : 5); j++) {
								EntityPixie pixie = new EntityPixie(world);
								pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
								pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
								pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);
								world.spawnEntity(pixie);
							}
						}
						break;
					}
				}

				if(entity != null) {
					if(!entity.isImmuneToFire())
						entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
					float range = 6F;
					entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
					entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
					if(entity instanceof EntityWitherSkeleton && hardMode) {
						entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.elementiumSword));
					}
					world.spawnEntity(entity);
				}
			}
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		int invul = getInvulTime();

		if(world.isRemote) {
			particles();
			EntityPlayer player = Botania.proxy.getClientPlayer();
			if(getPlayersAround().contains(player))
				player.capabilities.isFlying &= player.capabilities.isCreativeMode;
			return;
		}

		bossInfo.setPercent(getHealth() / getMaxHealth());

		if(isRiding())
			dismountRidingEntity();

		if(world.getDifficulty() == EnumDifficulty.PEACEFUL)
			setDead();

		smashBlocksAround(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ), 1);

		List<EntityPlayer> players = getPlayersAround();

		if(players.isEmpty() && !world.playerEntities.isEmpty())
			setDead();
		else {
			for(EntityPlayer player : players) {
				for(EntityEquipmentSlot e : EntityEquipmentSlot.values()) {
					if(e.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !player.getItemStackFromSlot(e).isEmpty()) {
						anyWithArmor = true;
						break;
					}
				}

				//also see SleepingHandler
				if(player.isPlayerSleeping()) player.wakeUpPlayer(true, true, false);
				
				clearPotions(player);
				keepInsideArena(player);
				player.capabilities.isFlying &= player.capabilities.isCreativeMode;
			}
		}

		if(isDead)
			return;

		boolean spawnMissiles = hardMode && ticksExisted % 15 < 4;

		if(invul > 0 && mobSpawnTicks == MOB_SPAWN_TICKS) {
			if(invul < SPAWN_TICKS)  {
				if(invul > SPAWN_TICKS / 2 && world.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
					for(int i = 0; i < 2; i++)
						spawnExplosionParticle();
			}

			setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);

			motionY = 0;
		} else {
			if(aggro) {
				boolean dying = getHealth() / getMaxHealth() < 0.2;
				if(dying && mobSpawnTicks > 0) {
					motionX = 0;
					motionY = 0;
					motionZ = 0;

					int reverseTicks = MOB_SPAWN_TICKS - mobSpawnTicks;
					if(reverseTicks < MOB_SPAWN_START_TICKS) {
						motionY = 0.2;
						setInvulTime(invul + 1);
					}

					if(reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobSpawnTicks > MOB_SPAWN_END_TICKS && mobSpawnTicks % MOB_SPAWN_WAVE_TIME == 0) {
						spawnMobs(players);

						if(hardMode && ticksExisted % 3 < 2) {
							for(int i = 0; i < playerCount; i++)
								spawnMissile();
							spawnMissiles = false;
						}
					}

					mobSpawnTicks--;
					tpDelay = 10;
				} else if(tpDelay > 0) {
					if(invul > 0)
						setInvulTime(invul - 1);

					tpDelay--;
					if(tpDelay == 0 && getHealth() > 0) {
						teleportRandomly();

						if(spawnLandmines) {
							int count = dying && hardMode ? 7 : 6;
							for(int i = 0; i < count; i++) {
								int x = source.getX() - 10 + rand.nextInt(20);
								int y = (int) players.get(rand.nextInt(players.size())).posY;
								int z = source.getZ() - 10 + rand.nextInt(20);

								EntityMagicLandmine landmine = new EntityMagicLandmine(world);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								world.spawnEntity(landmine);
							}

						}

						if(!players.isEmpty())
							for(int pl = 0; pl < playerCount; pl++)
								for(int i = 0; i < (spawnPixies ? world.rand.nextInt(hardMode ? 6 : 3) : 1); i++) {
									EntityPixie pixie = new EntityPixie(world);
									pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
									pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
									pixie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pixie)), null);
									world.spawnEntity(pixie);
								}

						tpDelay = hardMode ? dying ? 35 : 45 : dying ? 40 : 60;
						spawnLandmines = true;
						spawnPixies = false;
					}
				}

				if(spawnMissiles)
					spawnMissile();
			} else {
				if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		bossInfo.removePlayer(player);
	}

	@Override
	protected void collideWithNearbyEntities() {
		if(getInvulTime() == 0)
			super.collideWithNearbyEntities();
	}

	@Override
	public boolean canBePushed() {
		return super.canBePushed() && getInvulTime() == 0;
	}

	private void spawnMissile() {
		EntityMagicMissile missile = new EntityMagicMissile(this, true);
		missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1), posZ + (Math.random() - 0.5 * 0.1));
		if(missile.findTarget()) {
			playSound(ModSounds.missile, 0.6F, 0.8F + (float) Math.random() * 0.2F);
			world.spawnEntity(missile);
		}
	}

	private void teleportRandomly() {
		//choose a location to teleport to
		double oldX = posX, oldY = posY, oldZ = posZ;
		double newX, newY = source.getY(), newZ;
		int tries = 0;

		do {
			newX = source.getX() + (rand.nextDouble() - .5) * ARENA_RANGE;
			newZ = source.getZ() + (rand.nextDouble() - .5) * ARENA_RANGE;
			tries++;
			//ensure it's inside the arena ring, and not just its bounding square
		}
		while(tries < 50 && vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(newX, newY, newZ, source.getX(), source.getY(), source.getZ()) > 12);

		if(tries == 50) {
			//failsafe: teleport to the beacon
			newX = source.getX() + .5;
			newY = source.getY() + 1.6;
			newZ = source.getZ() + .5;
		}
		
		//for low-floor arenas, ensure landing on the ground
		BlockPos tentativeFloorPos = new BlockPos(newX, newY - 1, newZ);
		if(world.getBlockState(tentativeFloorPos).getCollisionBoundingBox(world, tentativeFloorPos) == null) {
			newY--;
		}

		//teleport there
		setPositionAndUpdate(newX, newY, newZ);

		//play sound
		world.playSound(null, oldX, oldY, oldZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
		this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

		Random random = getRNG();

		//spawn particles along the path
		int particleCount = 128;
		for(int i = 0; i < particleCount; ++i) {
			double progress = i / (double) (particleCount - 1);
			float vx = (random.nextFloat() - 0.5F) * 0.2F;
			float vy = (random.nextFloat() - 0.5F) * 0.2F;
			float vz = (random.nextFloat() - 0.5F) * 0.2F;
			double px = oldX + (newX - oldX) * progress + (random.nextDouble() - 0.5D) * width * 2.0D;
			double py = oldY + (newY - oldY) * progress + random.nextDouble() * height;
			double pz = oldZ + (newZ - oldZ) * progress + (random.nextDouble() - 0.5D) * width * 2.0D;
			world.spawnParticle(EnumParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
		}

		Vec3d oldPosVec = new Vec3d(oldX, oldY + height / 2, oldZ);
		Vec3d newPosVec = new Vec3d(newX, newY + height / 2, newZ);

		if(oldPosVec.squareDistanceTo(newPosVec) > 1) {
			//damage players in the path of the teleport
			for(EntityPlayer player : getPlayersAround()) {
				RayTraceResult rtr = player.getEntityBoundingBox().grow(0.25).calculateIntercept(oldPosVec, newPosVec);
				if(rtr != null)
					player.attackEntityFrom(DamageSource.causeMobDamage(this), 6);
			}

			//break blocks in the path of the teleport
			int breakSteps = (int) oldPosVec.distanceTo(newPosVec);
			if(breakSteps >= 2) {
				for(int i = 0; i < breakSteps; i++) {
					float progress = i / (float) (breakSteps - 1);
					int breakX = MathHelper.floor(oldX + (newX - oldX) * progress);
					int breakY = MathHelper.floor(oldY + (newY - oldY) * progress);
					int breakZ = MathHelper.floor(oldZ + (newZ - oldZ) * progress);

					smashBlocksAround(breakX, breakY, breakZ, 1);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}

	@SideOnly(Side.CLIENT)
	private static Rectangle barRect;
	@SideOnly(Side.CLIENT)
	private static Rectangle hpBarRect;

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarTextureRect() {
		if(barRect == null)
			barRect = new Rectangle(0, 0, 185, 15);
		return barRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Rectangle getBossBarHPTextureRect() {
		if(hpBarRect == null)
			hpBarRect = new Rectangle(0, barRect.y + barRect.height, 181, 7);
		return hpBarRect;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int bossBarRenderCallback(ScaledResolution res, int x, int y) {
		GlStateManager.pushMatrix();
		int px = x + 160;
		int py = y + 12;

		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		mc.getRenderItem().renderItemIntoGUI(stack, px, py);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		mc.fontRenderer.setUnicodeFlag(true);
		mc.fontRenderer.drawStringWithShadow("" + playerCount, px + 15, py + 4, 0xFFFFFF);
		mc.fontRenderer.setUnicodeFlag(unicode);
		GlStateManager.popMatrix();

		return 5;
	}

	@Override
	public UUID getBossInfoUuid() {
		return bossInfoUUID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBossBarShaderProgram(boolean background) {
		return background ? 0 : ShaderHelper.dopplegangerBar;
	}

	@SideOnly(Side.CLIENT)
	private ShaderCallback shaderCallback;

	@Override
	@SideOnly(Side.CLIENT)
	public ShaderCallback getBossBarShaderCallback(boolean background, int shader) {
		if(shaderCallback == null)
			shaderCallback = shader1 -> {
				int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader1, "grainIntensity");
				int hpFractUniform = ARBShaderObjects.glGetUniformLocationARB(shader1, "hpFract");

				float time = getInvulTime();
				float grainIntensity = time > 20 ? 1F : Math.max(hardMode ? 0.5F : 0F, time / 20F);

				ARBShaderObjects.glUniform1fARB(grainIntensityUniform, grainIntensity);
				ARBShaderObjects.glUniform1fARB(hpFractUniform, getHealth() / getMaxHealth());
			};

			return background ? null : shaderCallback;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(playerCount);
		buffer.writeBoolean(hardMode);
		buffer.writeLong(source.toLong());
		buffer.writeLong(bossInfoUUID.getMostSignificantBits());
		buffer.writeLong(bossInfoUUID.getLeastSignificantBits());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void readSpawnData(ByteBuf additionalData) {
		playerCount = additionalData.readInt();
		hardMode = additionalData.readBoolean();
		source = BlockPos.fromLong(additionalData.readLong());
		long msb = additionalData.readLong();
		long lsb = additionalData.readLong();
		bossInfoUUID = new UUID(msb, lsb);
		Minecraft.getMinecraft().getSoundHandler().playSound(new DopplegangerMusic(this));
	}

	@SideOnly(Side.CLIENT)
	private static class DopplegangerMusic extends MovingSound {
		private final EntityDoppleganger guardian;

		public DopplegangerMusic(EntityDoppleganger guardian) {
			super(guardian.hardMode ? ModSounds.gaiaMusic2 : ModSounds.gaiaMusic1, SoundCategory.RECORDS);
			this.guardian = guardian;
			this.xPosF = guardian.getSource().getX();
			this.yPosF = guardian.getSource().getY();
			this.zPosF = guardian.getSource().getZ();
			this.repeat = true;
		}

		@Override
		public void update() {
			if (!guardian.isEntityAlive()) {
				donePlaying = true;
			}
		}
	}

	private static class BeaconComponent extends MultiblockComponent {

		public BeaconComponent(BlockPos relPos) {
			super(relPos, Blocks.IRON_BLOCK.getDefaultState());
		}

		@Override
		public boolean matches(World world, BlockPos pos) {
			return world.getBlockState(pos).getBlock().isBeaconBase(world, pos, pos.add(new BlockPos(-relPos.getX(), -relPos.getY(), -relPos.getZ())));
		}

	}

	private static class BeaconBeamComponent extends MultiblockComponent {

		public BeaconBeamComponent(BlockPos relPos) {
			super(relPos, Blocks.BEACON.getDefaultState());
		}

		@Override
		public boolean matches(World world, BlockPos pos) {
			return world.getTileEntity(pos) instanceof TileEntityBeacon;
		}
	}
}
