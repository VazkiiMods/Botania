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

import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class EntityDoppleganger extends EntityCreature implements IBotaniaBoss {

	public static final float ARENA_RANGE = 12F;
	private static final int SPAWN_TICKS = 160;
	private static final float MAX_HP = 800F;

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
	private static final DataParameter<Integer> DATA_MOB_SPAWN_TICKS = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TP_DELAY = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> AGGRO = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HARD_MODE = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.BOOLEAN);
	private static final DataParameter<BlockPos> SOURCE = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<UUID>> BOSSINFO_ID = EntityDataManager.createKey(EntityDoppleganger.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private static final BlockPos[] PYLON_LOCATIONS = {
		new BlockPos(4, 1, 4),
		new BlockPos(4, 1, -4),
		new BlockPos(-4, 1, 4),
		new BlockPos(-4, 1, -4)
	};

	private static final List<String> CHEATY_BLOCKS = Arrays.asList("OpenBlocks:beartrap",
			"ThaumicTinkerer:magnet");

	private boolean spawnLandmines = false;
	private boolean spawnPixies = false;
	private boolean anyWithArmor = false;

	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	public EntityPlayer trueKiller = null;

	private static boolean isPlayingMusic = false;

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS)).setCreateFog(true);

	public EntityDoppleganger(World world) {
		super(world);
		setSize(0.6F, 1.8F);
		((PathNavigateGround) getNavigator()).setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, Float.MAX_VALUE));
		isImmuneToFire = true;
		experienceValue = 825;
		Botania.proxy.addBoss(this);
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
		if(world.getTileEntity(pos) instanceof TileEntityBeacon && isTruePlayer(player)) {
			if(world.getDifficulty() == EnumDifficulty.PEACEFUL) {
				player.addChatMessage(new TextComponentTranslation("botaniamisc.peacefulNoob").setStyle(new Style().setColor(TextFormatting.RED)));
				return false;
			}

			for(BlockPos coords : PYLON_LOCATIONS) {
				BlockPos pos_ = pos.add(coords);

				IBlockState state = world.getBlockState(pos_);
				Block blockat = state.getBlock();
				if(blockat != ModBlocks.pylon || state.getValue(BotaniaStateProps.PYLON_VARIANT) != PylonVariant.GAIA) {
					player.addChatMessage(new TextComponentTranslation("botaniamisc.needsCatalysts").setStyle(new Style().setColor(TextFormatting.RED)));
					return false;
				}
			}

			if(!hasProperArena(world, pos)) {
				PacketHandler.sendTo((EntityPlayerMP) player,
						new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.ARENA_INDICATOR, pos.getX(), pos.getY(), pos.getZ()));

				player.addChatMessage(new TextComponentTranslation("botaniamisc.badArena").setStyle(new Style().setColor(TextFormatting.RED)));
				return false;
			}

			stack.stackSize--;

			EntityDoppleganger e = new EntityDoppleganger(world);
			e.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
			e.setInvulTime(SPAWN_TICKS);
			e.setHealth(1F);
			e.setSource(pos);
			e.setMobSpawnTicks(MOB_SPAWN_TICKS);
			e.setHardMode(hard);

			List<EntityPlayer> players = e.getPlayersAround();
			int playerCount = 0;
			for(EntityPlayer p : players)
				if(isTruePlayer(p))
					playerCount++;

			e.setPlayerCount(playerCount);
			e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP * playerCount);

			e.playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 10F, 0.1F);
			world.spawnEntityInWorld(e);
			return true;
		}

		return false;
	}

	private static boolean hasProperArena(World world, BlockPos startPos) {
		int heightCheck = 3;
		int heightMin = 2;
		int range = (int) Math.ceil(ARENA_RANGE);
		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++) {
				if(Math.abs(i) == 4 && Math.abs(j) == 4 || vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(i, j, 0, 0) > ARENA_RANGE)
					continue; // Ignore pylons and out of circle

				int air = 0;

				yCheck: {
					for(int k = heightCheck + heightMin + 1; k >= -heightCheck; k--) {
						BlockPos pos = startPos.add(i, k, j);
						boolean isAir = world.getBlockState(pos).getCollisionBoundingBox(world, pos) == null;
						if(isAir)
							air++;
						else {
							if(k > heightCheck)
								continue;
							else if(air > 2)
								break yCheck;
							air = 0;
						}
					}

					return false;
				}
			}

		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(INVUL_TIME, 0);
		dataManager.register(AGGRO, false);
		dataManager.register(TP_DELAY, 0);
		dataManager.register(SOURCE, BlockPos.ORIGIN);
		dataManager.register(DATA_MOB_SPAWN_TICKS, 0);
		dataManager.register(HARD_MODE, false);
		dataManager.register(PLAYER_COUNT, 0);
		dataManager.register(BOSSINFO_ID, Optional.absent());
	}

	public int getInvulTime() {
		return dataManager.get(INVUL_TIME);
	}

	public boolean isAggroed() {
		return dataManager.get(AGGRO);
	}

	public int getTPDelay() {
		return dataManager.get(TP_DELAY);
	}

	public BlockPos getSource() {
		return dataManager.get(SOURCE);
	}

	public int getMobSpawnTicks() {
		return dataManager.get(DATA_MOB_SPAWN_TICKS);
	}

	public boolean isHardMode() {
		return dataManager.get(HARD_MODE);
	}

	public int getPlayerCount() {
		return dataManager.get(PLAYER_COUNT);
	}

	public void setInvulTime(int time) {
		dataManager.set(INVUL_TIME, time);
	}

	public void setAggroed(boolean aggored) {
		dataManager.set(AGGRO, aggored);
	}

	public void setTPDelay(int delay) {
		dataManager.set(TP_DELAY, delay);
	}

	public void setSource(BlockPos pos) {
		dataManager.set(SOURCE, pos);
	}

	public void setMobSpawnTicks(int ticks) {
		dataManager.set(DATA_MOB_SPAWN_TICKS, ticks);
	}

	public void setHardMode(boolean hardMode) {
		dataManager.set(HARD_MODE, hardMode);
	}

	public void setPlayerCount(int count) {
		dataManager.set(PLAYER_COUNT, count);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_INVUL_TIME, getInvulTime());
		par1nbtTagCompound.setBoolean(TAG_AGGRO, isAggroed());
		par1nbtTagCompound.setInteger(TAG_MOB_SPAWN_TICKS, getMobSpawnTicks());

		BlockPos source = getSource();
		par1nbtTagCompound.setInteger(TAG_SOURCE_X, source.getX());
		par1nbtTagCompound.setInteger(TAG_SOURCE_Y, source.getY());
		par1nbtTagCompound.setInteger(TAG_SOURCE_Z, source.getZ());

		par1nbtTagCompound.setBoolean(TAG_HARD_MODE, isHardMode());
		par1nbtTagCompound.setInteger(TAG_PLAYER_COUNT, getPlayerCount());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setInvulTime(par1nbtTagCompound.getInteger(TAG_INVUL_TIME));
		setAggroed(par1nbtTagCompound.getBoolean(TAG_AGGRO));
		setMobSpawnTicks(par1nbtTagCompound.getInteger(TAG_MOB_SPAWN_TICKS));

		int x = par1nbtTagCompound.getInteger(TAG_SOURCE_X);
		int y = par1nbtTagCompound.getInteger(TAG_SOURCE_Y);
		int z = par1nbtTagCompound.getInteger(TAG_SOURCE_Z);
		setSource(new BlockPos(x, y, z));

		setHardMode(par1nbtTagCompound.getBoolean(TAG_HARD_MODE));
		if(par1nbtTagCompound.hasKey(TAG_PLAYER_COUNT))
			setPlayerCount(par1nbtTagCompound.getInteger(TAG_PLAYER_COUNT));
		else setPlayerCount(1);
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource par1DamageSource, float par2) {
		Entity e = par1DamageSource.getEntity();
		if((par1DamageSource.damageType.equals("player") || e instanceof EntityPixie) && e != null && isTruePlayer(e) && getInvulTime() == 0) {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			boolean crit = false;
			if(e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) e;
				crit = p.fallDistance > 0.0F && !p.onGround && !p.isOnLadder() && !p.isInWater() && !p.isPotionActive(MobEffects.BLINDNESS) && !p.isRiding();
			}

			int cap = crit ? 60 : 40;
			return super.attackEntityFrom(par1DamageSource, Math.min(cap, par2) * (isHardMode() ? 0.6F : 1F));
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
	protected void damageEntity(@Nonnull DamageSource par1DamageSource, float par2) {
		super.damageEntity(par1DamageSource, par2);

		Entity attacker = par1DamageSource.getEntity();
		if(attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.copy().sub(playerVector).copy().normalize().multiply(0.75);

			if(getHealth() > 0) {
				motionX = -motionVector.x;
				motionY = 0.5;
				motionZ = -motionVector.z;
				setTPDelay(4);
				spawnPixies = isAggroed();
			}

			setAggroed(true);
		}
	}

	@Override
	public void onDeath(@Nonnull DamageSource source) {
		super.onDeath(source);
		EntityLivingBase entitylivingbase = getAttackingEntity();
		if(entitylivingbase instanceof EntityPlayer) {
			((EntityPlayer) entitylivingbase).addStat(ModAchievements.gaiaGuardianKill, 1);
			if(!anyWithArmor)
				((EntityPlayer) entitylivingbase).addStat(ModAchievements.gaiaGuardianNoArmor, 1);
		}

		this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1D, 0D, 0D);
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
		return new ResourceLocation(LibMisc.MOD_ID, isHardMode() ? "gaia_guardian_2" : "gaia_guardian");
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, @Nonnull DamageSource source)
	{
		// Save true killer, they get extra loot
		if ("player".equals(source.getDamageType())
				&& source.getEntity() instanceof EntityPlayer) {
			this.trueKiller = ((EntityPlayer) source.getEntity());
		}

		// Drop equipment and clear it so multiple calls to super don't do it again
		super.dropEquipment(wasRecentlyHit, lootingModifier);

		for (EntityEquipmentSlot e : EntityEquipmentSlot.values()) {
			this.setItemStackToSlot(e, null);
		}

		// Generate loot table for every single attacking player
		for (UUID u : playersWhoAttacked) {
			EntityPlayer player = worldObj.getPlayerEntityByUUID(u);
			if (player == null)
				continue;

			EntityPlayer saveLastAttacker = this.attackingPlayer;
			attackingPlayer = player;
			super.dropLoot(wasRecentlyHit, lootingModifier, DamageSource.causePlayerDamage(player));
			attackingPlayer = saveLastAttacker;
		}

		trueKiller = null;
	}

	@Override
	public void setDead() {
		Botania.proxy.removeBoss(this);
		BlockPos source = getSource();
		Botania.proxy.playRecordClientSided(worldObj, source, null);
		isPlayingMusic = false;
		super.setDead();
	}

	private List<EntityPlayer> getPlayersAround() {
		BlockPos source = getSource();
		float range = 15F;
		return worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		BlockPos source = getSource();
		float range = ARENA_RANGE;
		int invul = getInvulTime();

		if (worldObj.isRemote) {
			for(int i = 0; i < 360; i += 8) {
				float r = 0.6F;
				float g = 0F;
				float b = 0.2F;
				float m = 0.15F;
				float mv = 0.35F;

				float rad = i * (float) Math.PI / 180F;
				double x = source.getX() + 0.5 - Math.cos(rad) * range;
				double y = source.getY() + 0.5;
				double z = source.getZ() + 0.5 - Math.sin(rad) * range;

				Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, 0.5F, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
			}

			if(worldObj.isRemote && !isPlayingMusic && !isDead && !getPlayersAround().isEmpty()) {
				Botania.proxy.playRecordClientSided(worldObj, source, (ItemRecord) (isHardMode() ? ModItems.recordGaia2 : ModItems.recordGaia1));
				isPlayingMusic = true;
			}

			EntityPlayer player = Botania.proxy.getClientPlayer();

			player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.isCreativeMode;

			if(invul > 10) {
				Vector3 pos = Vector3.fromEntityCenter(this).subtract(new Vector3(0, 0.2, 0));
				for (BlockPos arr : PYLON_LOCATIONS) {
					Vector3 pylonPos = new Vector3(source.getX() + arr.getX(), source.getY() + arr.getY(), source.getZ() + arr.getZ());
					double worldTime = ticksExisted;
					worldTime /= 5;

					float rad = 0.75F + (float) Math.random() * 0.05F;
					double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
					double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

					Vector3 partPos = new Vector3(xp, pylonPos.y, zp);
					Vector3 mot = pos.copy().sub(partPos).multiply(0.04);

					float r = 0.7F + (float) Math.random() * 0.3F;
					float g = (float) Math.random() * 0.3F;
					float b = 0.7F + (float) Math.random() * 0.3F;

					Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
					Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.4F, (float) mot.x, (float) mot.y, (float) mot.z);
				}
			}

			return;
		}

		dataManager.set(BOSSINFO_ID, Optional.of(bossInfo.getUniqueId()));
		bossInfo.setPercent(getHealth() / getMaxHealth());

		if(!getPassengers().isEmpty())
			this.dismountRidingEntity();

		if(worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
			setDead();

		int radius = 1;
		int posXInt = MathHelper.floor_double(posX);
		int posYInt = MathHelper.floor_double(posY);
		int posZInt = MathHelper.floor_double(posZ);
		for(int i = -radius; i < radius + 1; i++)
			for(int j = -radius; j < radius + 1; j++)
				for(int k = -radius; k < radius + 1; k++) {
					int xp = posXInt + i;
					int yp = posYInt + j;
					int zp = posZInt + k;
					BlockPos posp = new BlockPos(xp, yp, zp);
					if(isCheatyBlock(worldObj, posp)) {
						Block block = worldObj.getBlockState(posp).getBlock();
						List<ItemStack> items = block.getDrops(worldObj, posp, worldObj.getBlockState(posp), 0);
						for(ItemStack stack : items) {
							if(ConfigHandler.blockBreakParticles)
								worldObj.playEvent(2001, posp, Block.getStateId(worldObj.getBlockState(posp)));
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, xp + 0.5, yp + 0.5, zp + 0.5, stack));
						}
						worldObj.setBlockToAir(posp);
					}
				}

		boolean hard = isHardMode();
		List<EntityPlayer> players = getPlayersAround();
		int playerCount = getPlayerCount();

		if(players.isEmpty() && !worldObj.playerEntities.isEmpty())
			setDead();
		else {
			for(EntityPlayer player : players) {
				if(player.inventory.armorInventory[0] != null || player.inventory.armorInventory[1] != null || player.inventory.armorInventory[2] != null || player.inventory.armorInventory[3] != null)
					anyWithArmor = true;

				Collection<PotionEffect> active = player.getActivePotionEffects();
				active.removeIf(effect -> effect.getDuration() < 200 && effect.getIsAmbient() && !effect.getPotion().isBadEffect());

				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.isCreativeMode;
				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= range) {
					Vector3 sourceVector = new Vector3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
					Vector3 playerVector = Vector3.fromEntityCenter(player);
					Vector3 motion = sourceVector.copy().sub(playerVector).copy().normalize();

					player.motionX = motion.x;
					player.motionY = 0.2;
					player.motionZ = motion.z;
					player.velocityChanged = true;
				}
			}
		}

		if(isDead)
			return;

		int mobTicks = getMobSpawnTicks();
		boolean spawnMissiles = hard && ticksExisted % 15 < 4;

		if(invul > 0 && mobTicks == MOB_SPAWN_TICKS) {
			if(invul < SPAWN_TICKS)  {
				if(invul > SPAWN_TICKS / 2 && worldObj.rand.nextInt(SPAWN_TICKS - invul + 1) == 0)
					for(int i = 0; i < 2; i++)
						spawnExplosionParticle();
			}

			setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);

			motionY = 0;
		} else {
			if(isAggroed()) {
				boolean dying = getHealth() / getMaxHealth() < 0.2;
				if(dying && mobTicks > 0) {
					motionX = 0;
					motionY = 0;
					motionZ = 0;

					int reverseTicks = MOB_SPAWN_TICKS - mobTicks;
					if(reverseTicks < MOB_SPAWN_START_TICKS) {
						motionY = 0.2;
						setInvulTime(invul + 1);
					}

					if(reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobTicks > MOB_SPAWN_END_TICKS && mobTicks % MOB_SPAWN_WAVE_TIME == 0) {
						for(int pl = 0; pl < playerCount; pl++)
							for(int i = 0; i < 3 + worldObj.rand.nextInt(2); i++) {
								EntityLiving entity = null;
								switch(worldObj.rand.nextInt(2)) {
								case 0 : {
									entity = new EntityZombie(worldObj);
									if(worldObj.rand.nextInt(hard ? 3 : 12) == 0)
										entity = new EntityWitch(worldObj);

									break;
								}
								case 1 : {
									entity = new EntitySkeleton(worldObj);
									entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
									if(worldObj.rand.nextInt(8) == 0) {
										((EntitySkeleton) entity).setSkeletonType(1);
										entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(hard ? ModItems.elementiumSword : Items.STONE_SWORD));
									}
									break;
								}
								case 3 : {
									if(!players.isEmpty())
										for(int j = 0; j < 1 + worldObj.rand.nextInt(hard ? 8 : 5); j++) {
											EntityPixie pixie = new EntityPixie(worldObj);
											pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
											pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
											worldObj.spawnEntityInWorld(pixie);
										}
								}
								}

								if(entity != null) {
									if(!entity.isImmuneToFire())
										entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 30, 0));
									range = 6F;
									entity.setPosition(posX + 0.5 + Math.random() * range - range / 2, posY - 1, posZ + 0.5 + Math.random() * range - range / 2);
									worldObj.spawnEntityInWorld(entity);
								}
							}

						if(hard && ticksExisted % 3 < 2) {
							for(int i = 0; i < playerCount; i++)
								spawnMissile();
							spawnMissiles = false;
						}
					}

					setMobSpawnTicks(mobTicks - 1);
					setTPDelay(10);
				} else if(getTPDelay() > 0) {
					if(invul > 0)
						setInvulTime(invul - 1);

					setTPDelay(getTPDelay() - 1);
					if(getTPDelay() == 0 && getHealth() > 0) {
						int tries = 0;
						while(!teleportRandomly() && tries < 50)
							tries++;
						if(tries >= 50)
							teleportTo(source.getX() + 0.5, source.getY() + 1.6, source.getZ() + 0.5);

						if(spawnLandmines) {
							int count = dying && hard ? 7 : 6;
							for(int i = 0; i < count; i++) {
								int x = source.getX() - 10 + rand.nextInt(20);
								int z = source.getZ() - 10 + rand.nextInt(20);
								int y = worldObj.getTopSolidOrLiquidBlock(new BlockPos(x, -1, z)).getY();

								EntityMagicLandmine landmine = new EntityMagicLandmine(worldObj);
								landmine.setPosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								worldObj.spawnEntityInWorld(landmine);
							}

						}
							
						if(!players.isEmpty())
							for(int pl = 0; pl < playerCount; pl++)
								for(int i = 0; i < (spawnPixies ? worldObj.rand.nextInt(hard ? 6 : 3) : 1); i++) {
									EntityPixie pixie = new EntityPixie(worldObj);
									pixie.setProps(players.get(rand.nextInt(players.size())), this, 1, 8);
									pixie.setPosition(posX + width / 2, posY + 2, posZ + width / 2);
									worldObj.spawnEntityInWorld(pixie);
								}

						setTPDelay(hard ? (dying ? 35 : 45) : (dying ? 40 : 60));
						spawnLandmines = true;
						spawnPixies = false;
					}
				}

				if(spawnMissiles)
					spawnMissile();
			} else {
				range = 3F;
				players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				if(!players.isEmpty())
					damageEntity(DamageSource.causePlayerDamage(players.get(0)), 0);
			}
		}
	}

	@Override
	public boolean isNonBoss()
	{
		return false;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player)
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player)
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	private void spawnMissile() {
		EntityMagicMissile missile = new EntityMagicMissile(this, true);
		missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1), posZ + (Math.random() - 0.5 * 0.1));
		if(missile.getTarget()) {
			this.playSound(BotaniaSoundEvents.missile, 0.6F, 0.8F + (float) Math.random() * 0.2F);
			worldObj.spawnEntityInWorld(missile);
		}
	}

	private static boolean isCheatyBlock(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		String name = Block.REGISTRY.getNameForObject(block).toString();
		return CHEATY_BLOCKS.contains(name);
	}

	// EntityEnderman code below ============================================================================

	private boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return teleportTo(d0, d1, d2);
	}

	private boolean teleportTo(double par1, double par3, double par5) {
		double d3 = posX;
		double d4 = posY;
		double d5 = posZ;
		posX = par1;
		posY = par3;
		posZ = par5;
		boolean flag = false;
		BlockPos pos = new BlockPos(this);

		if(worldObj.isBlockLoaded(pos)) {
			boolean flag1 = false;

			while(!flag1 && pos.getY() > 0) {
				IBlockState state = worldObj.getBlockState(pos.down());
				Block block = state.getBlock();

				if(block.getMaterial(state).blocksMovement())
					flag1 = true;
				else {
					--posY;
					pos = pos.down();
				}
			}

			if(flag1) {
				setPosition(posX, posY, posZ);

				if(worldObj.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !worldObj.containsAnyLiquid(getEntityBoundingBox()))
					flag = true;

				// Prevent out of bounds teleporting
				BlockPos source = getSource();
				if(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(posX, posY, posZ, source.getX(), source.getY(), source.getZ()) > 12)
					flag = false;
			}
		}

		if (!flag) {
			setPosition(d3, d4, d5);
			return false;
		} else  {
			short short1 = 128;

			for(int l = 0; l < short1; ++l)  {
				double d6 = l / (short1 - 1.0D);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height;
				double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				worldObj.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
			}

			playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
			return true;
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

		boolean unicode = mc.fontRendererObj.getUnicodeFlag();
		mc.fontRendererObj.setUnicodeFlag(true);
		mc.fontRendererObj.drawStringWithShadow("" + getPlayerCount(), px + 15, py + 4, 0xFFFFFF);
		mc.fontRendererObj.setUnicodeFlag(unicode);
		GlStateManager.popMatrix();

		return 5;
	}

	@Override
	public UUID getBossInfoUuid() {
		return dataManager.get(BOSSINFO_ID).or(new UUID(0, 0));
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
				float grainIntensity = time > 20 ? 1F : Math.max(isHardMode() ? 0.5F : 0F, time / 20F);

				ARBShaderObjects.glUniform1fARB(grainIntensityUniform, grainIntensity);
				ARBShaderObjects.glUniform1fARB(hpFractUniform, getHealth() / getMaxHealth());
			};

		return background ? null : shaderCallback;
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
