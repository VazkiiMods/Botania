/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import vazkii.botania.client.core.handler.BossBarHandler;
import vazkii.botania.client.core.helper.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class EntityDoppleganger extends MobEntity implements IEntityAdditionalSpawnData {
	public static final float ARENA_RANGE = 12F;
	public static final int ARENA_HEIGHT = 5;

	private static final int SPAWN_TICKS = 160;
	public static final float MAX_HP = 320F;

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
	private static final Tag.Identified<Block> BLACKLIST = ModTags.Blocks.GAIA_BREAK_BLACKLIST;

	private static final TrackedData<Integer> INVUL_TIME = DataTracker.registerData(EntityDoppleganger.class, TrackedDataHandlerRegistry.INTEGER);

	private static final List<BlockPos> PYLON_LOCATIONS = ImmutableList.of(
			new BlockPos(4, 1, 4),
			new BlockPos(4, 1, -4),
			new BlockPos(-4, 1, 4),
			new BlockPos(-4, 1, -4)
	);

	private static final List<Identifier> CHEATY_BLOCKS = Arrays.asList(
			new Identifier("openblocks", "beartrap"),
			new Identifier("thaumictinkerer", "magnet")
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
	private final ServerBossBar bossInfo = (ServerBossBar) new ServerBossBar(ModEntities.DOPPLEGANGER.getName(), BossBar.Color.PINK, BossBar.Style.PROGRESS).setThickenFog(true);;
	private UUID bossInfoUUID = bossInfo.getUuid();
	public PlayerEntity trueKiller = null;

	public EntityDoppleganger(EntityType<EntityDoppleganger> type, World world) {
		super(type, world);
		experiencePoints = 825;
		if (world.isClient) {
			Botania.proxy.addBoss(this);
		}
	}

	public static boolean spawn(PlayerEntity player, ItemStack stack, World world, BlockPos pos, boolean hard) {
		//initial checks
		if (!(world.getBlockEntity(pos) instanceof BeaconBlockEntity) ||
				!isTruePlayer(player) ||
				countGaiaGuardiansAround(world, pos) > 0) {
			return false;
		}

		//check difficulty
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (!world.isClient) {
				player.sendSystemMessage(new TranslatableText("botaniamisc.peacefulNoob").formatted(Formatting.RED), Util.NIL_UUID);
			}
			return false;
		}

		//check pylons
		List<BlockPos> invalidPylonBlocks = checkPylons(world, pos);
		if (!invalidPylonBlocks.isEmpty()) {
			if (world.isClient) {
				warnInvalidBlocks(world, invalidPylonBlocks);
			} else {
				player.sendSystemMessage(new TranslatableText("botaniamisc.needsCatalysts").formatted(Formatting.RED), Util.NIL_UUID);
			}

			return false;
		}

		//check arena shape
		List<BlockPos> invalidArenaBlocks = checkArena(world, pos);
		if (!invalidArenaBlocks.isEmpty()) {
			if (world.isClient) {
				warnInvalidBlocks(world, invalidArenaBlocks);
			} else {
				PacketHandler.sendTo((ServerPlayerEntity) player,
						new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.ARENA_INDICATOR, pos.getX(), pos.getY(), pos.getZ()));

				player.sendSystemMessage(new TranslatableText("botaniamisc.badArena").formatted(Formatting.RED), Util.NIL_UUID);
			}

			return false;
		}

		//all checks ok, spawn the boss
		if (!world.isClient) {
			stack.decrement(1);

			EntityDoppleganger e = ModEntities.DOPPLEGANGER.create(world);
			e.updatePosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
			e.setInvulTime(SPAWN_TICKS);
			e.setHealth(1F);
			e.source = pos;
			e.mobSpawnTicks = MOB_SPAWN_TICKS;
			e.hardMode = hard;

			int playerCount = e.getPlayersAround().size();
			e.playerCount = playerCount;
			e.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(MAX_HP * playerCount);
			if (hard) {
				e.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(15);
			}

			e.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 10F, 0.1F);
			e.initialize(world, world.getLocalDifficulty(e.getBlockPos()), SpawnReason.EVENT, null, null);
			world.spawnEntity(e);
		}

		return true;
	}

	private static List<BlockPos> checkPylons(World world, BlockPos beaconPos) {
		List<BlockPos> invalidPylonBlocks = new ArrayList<>();

		for (BlockPos coords : PYLON_LOCATIONS) {
			BlockPos pos_ = beaconPos.add(coords);

			BlockState state = world.getBlockState(pos_);
			if (state.getBlock() != ModBlocks.gaiaPylon) {
				invalidPylonBlocks.add(pos_);
			}
		}

		return invalidPylonBlocks;
	}

	private static List<BlockPos> checkArena(World world, BlockPos beaconPos) {
		List<BlockPos> trippedPositions = new ArrayList<>();
		int range = (int) Math.ceil(ARENA_RANGE);
		BlockPos pos;

		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				if (Math.abs(x) == 4 && Math.abs(z) == 4 || vazkii.botania.common.core.helper.MathHelper.pointDistancePlane(x, z, 0, 0) > ARENA_RANGE) {
					continue; // Ignore pylons and out of circle
				}

				boolean hasFloor = false;

				for (int y = -2; y <= ARENA_HEIGHT; y++) {
					if (x == 0 && y == 0 && z == 0) {
						continue; //the beacon
					}

					pos = beaconPos.add(x, y, z);

					BlockState state = world.getBlockState(pos);

					boolean allowBlockHere = y < 0;
					boolean isBlockHere = !state.getCollisionShape(world, pos).isEmpty();

					if (allowBlockHere && isBlockHere) //floor is here! good
					{
						hasFloor = true;
					}

					if (y == 0 && !hasFloor) //column is entirely missing floor
					{
						trippedPositions.add(pos.down());
					}

					if (!allowBlockHere && isBlockHere && !BLACKLIST.contains(state.getBlock())) //ceiling is obstructed in this column
					{
						trippedPositions.add(pos);
					}
				}
			}
		}

		return trippedPositions;
	}

	private static void warnInvalidBlocks(World world, Iterable<BlockPos> invalidPositions) {
		WispParticleData data = WispParticleData.wisp(0.5F, 1, 0.2F, 0.2F, 8, false);
		for (BlockPos pos_ : invalidPositions) {
			world.addParticle(data, pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 0, 0, 0);
		}
	}

	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, ARENA_RANGE * 1.5F));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(INVUL_TIME, 0);
	}

	public int getInvulTime() {
		return dataTracker.get(INVUL_TIME);
	}

	public BlockPos getSource() {
		return source;
	}

	public void setInvulTime(int time) {
		dataTracker.set(INVUL_TIME, time);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag cmp) {
		super.writeCustomDataToTag(cmp);
		cmp.putInt(TAG_INVUL_TIME, getInvulTime());
		cmp.putBoolean(TAG_AGGRO, aggro);
		cmp.putInt(TAG_MOB_SPAWN_TICKS, mobSpawnTicks);

		cmp.putInt(TAG_SOURCE_X, source.getX());
		cmp.putInt(TAG_SOURCE_Y, source.getY());
		cmp.putInt(TAG_SOURCE_Z, source.getZ());

		cmp.putBoolean(TAG_HARD_MODE, hardMode);
		cmp.putInt(TAG_PLAYER_COUNT, playerCount);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag cmp) {
		super.readCustomDataFromTag(cmp);
		setInvulTime(cmp.getInt(TAG_INVUL_TIME));
		aggro = cmp.getBoolean(TAG_AGGRO);
		mobSpawnTicks = cmp.getInt(TAG_MOB_SPAWN_TICKS);

		int x = cmp.getInt(TAG_SOURCE_X);
		int y = cmp.getInt(TAG_SOURCE_Y);
		int z = cmp.getInt(TAG_SOURCE_Z);
		source = new BlockPos(x, y, z);

		hardMode = cmp.getBoolean(TAG_HARD_MODE);
		if (cmp.contains(TAG_PLAYER_COUNT)) {
			playerCount = cmp.getInt(TAG_PLAYER_COUNT);
		} else {
			playerCount = 1;
		}

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void heal(float amount) {
		if (getInvulTime() == 0) {
			super.heal(amount);
		}
	}

	@Override
	public void kill() {
		this.setHealth(0.0F);
	}

	@Override
	public boolean damage(@Nonnull DamageSource source, float amount) {
		Entity e = source.getAttacker();
		if (e instanceof PlayerEntity && isTruePlayer(e) && getInvulTime() == 0) {
			PlayerEntity player = (PlayerEntity) e;

			if (!playersWhoAttacked.contains(player.getUuid())) {
				playersWhoAttacked.add(player.getUuid());
			}

			int cap = 25;
			return super.damage(source, Math.min(cap, amount));
		}

		return false;
	}

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

	public static boolean isTruePlayer(Entity e) {
		if (!(e instanceof PlayerEntity)) {
			return false;
		}

		PlayerEntity player = (PlayerEntity) e;

		String name = player.getName().getString();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}

	@Override
	protected void applyDamage(@Nonnull DamageSource source, float amount) {
		super.applyDamage(source, amount);

		Entity attacker = source.getSource();
		if (attacker != null) {
			Vector3 thisVector = Vector3.fromEntityCenter(this);
			Vector3 playerVector = Vector3.fromEntityCenter(attacker);
			Vector3 motionVector = thisVector.subtract(playerVector).normalize().multiply(0.75);

			if (getHealth() > 0) {
				setVelocity(-motionVector.x, 0.5, -motionVector.z);
				tpDelay = 4;
				spawnPixies = aggro;
			}

			aggro = true;
		}
	}

	@Override
	public void onDeath(@Nonnull DamageSource source) {
		super.onDeath(source);
		LivingEntity entitylivingbase = getPrimeAdversary();
		if (entitylivingbase instanceof ServerPlayerEntity && !anyWithArmor) {
			DopplegangerNoArmorTrigger.INSTANCE.trigger((ServerPlayerEntity) entitylivingbase, this, source);
		}

		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
		world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1D, 0D, 0D);
	}

	@Override
	public boolean canImmediatelyDespawn(double dist) {
		return false;
	}

	@Override
	public Identifier getLootTableId() {
		return prefix(hardMode ? "gaia_guardian_2" : "gaia_guardian");
	}

	@Override
	protected void dropLoot(@Nonnull DamageSource source, boolean wasRecentlyHit) {
		// Save true killer, they get extra loot
		if (wasRecentlyHit && source.getAttacker() instanceof PlayerEntity) {
			trueKiller = (PlayerEntity) source.getAttacker();
		}

		// Drop equipment and clear it so multiple calls to super don't do it again
		super.dropEquipment(source, 0, wasRecentlyHit);

		for (EquipmentSlot e : EquipmentSlot.values()) {
			equipStack(e, ItemStack.EMPTY);
		}

		// Generate loot table for every single attacking player
		for (UUID u : playersWhoAttacked) {
			PlayerEntity player = world.getPlayerByUuid(u);
			if (player == null) {
				continue;
			}

			PlayerEntity saveLastAttacker = attackingPlayer;
			Vec3d savePos = getPos();

			attackingPlayer = player; // Fake attacking player as the killer
			// Spoof pos so drops spawn at the player
			updatePosition(player.getX(), player.getY(), player.getZ());
			super.dropLoot(DamageSource.player(player), wasRecentlyHit);
			updatePosition(savePos.getX(), savePos.getY(), savePos.getZ());
			attackingPlayer = saveLastAttacker;
		}

		trueKiller = null;
	}

	@Override
	public void remove() {
		if (world.isClient) {
			Botania.proxy.removeBoss(this);
		}
		super.remove();
	}

	public List<PlayerEntity> getPlayersAround() {
		float range = 15F;
		return world.getEntities(PlayerEntity.class, new Box(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range), player -> isTruePlayer(player) && !player.isSpectator());
	}

	private static int countGaiaGuardiansAround(World world, BlockPos source) {
		float range = 15F;
		List<EntityDoppleganger> l = world.getNonSpectatingEntities(EntityDoppleganger.class, new Box(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
		return l.size();
	}

	private void particles() {
		for (int i = 0; i < 360; i += 8) {
			float r = 0.6F;
			float g = 0F;
			float b = 0.2F;
			float m = 0.15F;
			float mv = 0.35F;

			float rad = i * (float) Math.PI / 180F;
			double x = source.getX() + 0.5 - Math.cos(rad) * ARENA_RANGE;
			double y = source.getY() + 0.5;
			double z = source.getZ() + 0.5 - Math.sin(rad) * ARENA_RANGE;

			WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
			world.addParticle(data, x, y, z, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * mv, (float) (Math.random() - 0.5F) * m);
		}

		if (getInvulTime() > 10) {
			Vector3 pos = Vector3.fromEntityCenter(this).subtract(new Vector3(0, 0.2, 0));
			for (BlockPos arr : PYLON_LOCATIONS) {
				Vector3 pylonPos = new Vector3(source.getX() + arr.getX(), source.getY() + arr.getY(), source.getZ() + arr.getZ());
				double worldTime = age;
				worldTime /= 5;

				float rad = 0.75F + (float) Math.random() * 0.05F;
				double xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad;
				double zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad;

				Vector3 partPos = new Vector3(xp, pylonPos.y, zp);
				Vector3 mot = pos.subtract(partPos).multiply(0.04);

				float r = 0.7F + (float) Math.random() * 0.3F;
				float g = (float) Math.random() * 0.3F;
				float b = 0.7F + (float) Math.random() * 0.3F;

				WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, r, g, b, 1);
				world.addParticle(data, partPos.x, partPos.y, partPos.z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
				WispParticleData data1 = WispParticleData.wisp(0.4F, r, g, b);
				world.addParticle(data1, partPos.x, partPos.y, partPos.z, (float) mot.x, (float) mot.y, (float) mot.z);
			}
		}
	}

	private void smashBlocksAround(int centerX, int centerY, int centerZ, int radius) {
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius + 1; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					int x = centerX + dx;
					int y = centerY + dy;
					int z = centerZ + dz;

					BlockPos pos = new BlockPos(x, y, z);
					BlockState state = world.getBlockState(pos);
					Block block = state.getBlock();

					if (state.getHardness(world, pos) == -1) {
						continue;
					}

					if (CHEATY_BLOCKS.contains(Registry.BLOCK.getId(block))) {
						world.breakBlock(pos, true);
					} else {
						//don't break blacklisted blocks
						if (BLACKLIST.contains(block)) {
							continue;
						}
						//don't break the floor
						if (y < source.getY()) {
							continue;
						}
						//don't break blocks in pylon columns
						if (Math.abs(source.getX() - x) == 4 && Math.abs(source.getZ() - z) == 4) {
							continue;
						}

						world.breakBlock(pos, true);
					}
				}
			}
		}
	}

	private void clearPotions(PlayerEntity player) {
		List<StatusEffect> potionsToRemove = player.getStatusEffects().stream()
				.filter(effect -> effect.getDuration() < 160 && effect.isAmbient() && effect.getEffectType().getType() != StatusEffectType.HARMFUL)
				.map(StatusEffectInstance::getEffectType)
				.distinct()
				.collect(Collectors.toList());

		potionsToRemove.forEach(potion -> {
			player.removeStatusEffect(potion);
			((ServerWorld) world).getChunkManager().sendToNearbyPlayers(player,
					new RemoveEntityStatusEffectS2CPacket(player.getEntityId(), potion));
		});
	}

	private void keepInsideArena(PlayerEntity player) {
		if (vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(player.getX(), player.getY(), player.getZ(), source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5) >= ARENA_RANGE) {
			Vector3 sourceVector = new Vector3(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
			Vector3 playerVector = Vector3.fromEntityCenter(player);
			Vector3 motion = sourceVector.subtract(playerVector).normalize();

			player.setVelocity(motion.x, 0.2, motion.z);
			player.velocityModified = true;
		}
	}

	private void spawnMobs(List<PlayerEntity> players) {
		for (int pl = 0; pl < playerCount; pl++) {
			for (int i = 0; i < 3 + world.random.nextInt(2); i++) {
				MobEntity entity = null;
				switch (world.random.nextInt(2)) {
				case 0: {
					entity = new ZombieEntity(world);
					if (world.random.nextInt(hardMode ? 3 : 12) == 0) {
						entity = EntityType.WITCH.create(world);
					}
					break;
				}
				case 1: {
					entity = EntityType.SKELETON.create(world);
					if (world.random.nextInt(8) == 0) {
						entity = EntityType.WITHER_SKELETON.create(world);
					}
					break;
				}
				case 3: {
					if (!players.isEmpty()) {
						for (int j = 0; j < 1 + world.random.nextInt(hardMode ? 8 : 5); j++) {
							EntityPixie pixie = new EntityPixie(world);
							pixie.setProps(players.get(random.nextInt(players.size())), this, 1, 8);
							pixie.updatePosition(getX() + getWidth() / 2, getY() + 2, getZ() + getWidth() / 2);
							pixie.initialize(world, world.getLocalDifficulty(pixie.getBlockPos()),
									SpawnReason.MOB_SUMMONED, null, null);
							world.spawnEntity(pixie);
						}
					}
					break;
				}
				}

				if (entity != null) {
					if (!entity.isFireImmune()) {
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
					}
					float range = 6F;
					entity.updatePosition(getX() + 0.5 + Math.random() * range - range / 2, getY() - 1,
							getZ() + 0.5 + Math.random() * range - range / 2);
					entity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()),
							SpawnReason.MOB_SUMMONED, null, null);
					if (entity instanceof WitherSkeletonEntity && hardMode) {
						entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.elementiumSword));
					}
					world.spawnEntity(entity);
				}
			}
		}
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		int invul = getInvulTime();

		if (world.isClient) {
			particles();
			PlayerEntity player = Botania.proxy.getClientPlayer();
			if (getPlayersAround().contains(player)) {
				player.abilities.flying &= player.abilities.creativeMode;
			}
			return;
		}

		bossInfo.setPercent(getHealth() / getMaxHealth());

		if (hasVehicle()) {
			stopRiding();
		}

		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			remove();
		}

		smashBlocksAround(MathHelper.floor(getX()), MathHelper.floor(getY()), MathHelper.floor(getZ()), 1);

		List<PlayerEntity> players = getPlayersAround();

		if (players.isEmpty() && !world.getPlayers().isEmpty()) {
			remove();
		} else {
			for (PlayerEntity player : players) {
				for (EquipmentSlot e : EquipmentSlot.values()) {
					if (e.getType() == EquipmentSlot.Type.ARMOR && !player.getEquippedStack(e).isEmpty()) {
						anyWithArmor = true;
						break;
					}
				}

				//also see SleepingHandler
				if (player.isSleeping()) {
					player.wakeUp();
				}

				clearPotions(player);
				keepInsideArena(player);
				player.abilities.flying &= player.abilities.creativeMode;
			}
		}

		if (!isAlive() || players.isEmpty()) {
			return;
		}

		boolean spawnMissiles = hardMode && age % 15 < 4;

		if (invul > 0 && mobSpawnTicks == MOB_SPAWN_TICKS) {
			if (invul < SPAWN_TICKS) {
				if (invul > SPAWN_TICKS / 2 && world.random.nextInt(SPAWN_TICKS - invul + 1) == 0) {
					for (int i = 0; i < 2; i++) {
						playSpawnEffects();
					}
				}
			}

			setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);

			setVelocity(getVelocity().getX(), 0, getVelocity().getZ());
		} else {
			if (aggro) {
				boolean dying = getHealth() / getMaxHealth() < 0.2;
				if (dying && mobSpawnTicks > 0) {
					setVelocity(Vec3d.ZERO);

					int reverseTicks = MOB_SPAWN_TICKS - mobSpawnTicks;
					if (reverseTicks < MOB_SPAWN_START_TICKS) {
						setVelocity(getVelocity().getX(), 0.2, getVelocity().getZ());
						setInvulTime(invul + 1);
					}

					if (reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobSpawnTicks > MOB_SPAWN_END_TICKS && mobSpawnTicks % MOB_SPAWN_WAVE_TIME == 0) {
						spawnMobs(players);

						if (hardMode && age % 3 < 2) {
							for (int i = 0; i < playerCount; i++) {
								spawnMissile();
							}
							spawnMissiles = false;
						}
					}

					mobSpawnTicks--;
					tpDelay = 10;
				} else if (tpDelay > 0) {
					if (invul > 0) {
						setInvulTime(invul - 1);
					}

					tpDelay--;
					if (tpDelay == 0 && getHealth() > 0) {
						teleportRandomly();

						if (spawnLandmines) {
							int count = dying && hardMode ? 7 : 6;
							for (int i = 0; i < count; i++) {
								int x = source.getX() - 10 + random.nextInt(20);
								int y = (int) players.get(random.nextInt(players.size())).getY();
								int z = source.getZ() - 10 + random.nextInt(20);

								EntityMagicLandmine landmine = ModEntities.MAGIC_LANDMINE.create(world);
								landmine.updatePosition(x + 0.5, y, z + 0.5);
								landmine.summoner = this;
								world.spawnEntity(landmine);
							}

						}

						for (int pl = 0; pl < playerCount; pl++) {
							for (int i = 0; i < (spawnPixies ? world.random.nextInt(hardMode ? 6 : 3) : 1); i++) {
								EntityPixie pixie = new EntityPixie(world);
								pixie.setProps(players.get(random.nextInt(players.size())), this, 1, 8);
								pixie.updatePosition(getX() + getWidth() / 2, getY() + 2, getZ() + getWidth() / 2);
								pixie.initialize(world, world.getLocalDifficulty(pixie.getBlockPos()),
										SpawnReason.MOB_SUMMONED, null, null);
								world.spawnEntity(pixie);
							}
						}

						tpDelay = hardMode ? dying ? 35 : 45 : dying ? 40 : 60;
						spawnLandmines = true;
						spawnPixies = false;
					}
				}

				if (spawnMissiles) {
					spawnMissile();
				}
			} else {
				applyDamage(DamageSource.player(players.get(0)), 0);
			}
		}
	}

	@Override
	public boolean canUsePortals() {
		return false;
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		bossInfo.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		bossInfo.removePlayer(player);
	}

	@Override
	protected void tickCramming() {
		if (getInvulTime() == 0) {
			super.tickCramming();
		}
	}

	@Override
	public boolean isPushable() {
		return super.isPushable() && getInvulTime() == 0;
	}

	private void spawnMissile() {
		EntityMagicMissile missile = new EntityMagicMissile(this, true);
		missile.updatePosition(getX() + (Math.random() - 0.5 * 0.1), getY() + 2.4 + (Math.random() - 0.5 * 0.1), getZ() + (Math.random() - 0.5 * 0.1));
		if (missile.findTarget()) {
			playSound(ModSounds.missile, 0.6F, 0.8F + (float) Math.random() * 0.2F);
			world.spawnEntity(missile);
		}
	}

	private void teleportRandomly() {
		//choose a location to teleport to
		double oldX = getX(), oldY = getY(), oldZ = getZ();
		double newX, newY = source.getY(), newZ;
		int tries = 0;

		do {
			newX = source.getX() + (random.nextDouble() - .5) * ARENA_RANGE;
			newZ = source.getZ() + (random.nextDouble() - .5) * ARENA_RANGE;
			tries++;
			//ensure it's inside the arena ring, and not just its bounding square
		} while (tries < 50 && vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(newX, newY, newZ, source.getX(), source.getY(), source.getZ()) > 12);

		if (tries == 50) {
			//failsafe: teleport to the beacon
			newX = source.getX() + .5;
			newY = source.getY() + 1.6;
			newZ = source.getZ() + .5;
		}

		//for low-floor arenas, ensure landing on the ground
		BlockPos tentativeFloorPos = new BlockPos(newX, newY - 1, newZ);
		if (world.getBlockState(tentativeFloorPos).getCollisionShape(world, tentativeFloorPos).isEmpty()) {
			newY--;
		}

		//teleport there
		requestTeleport(newX, newY, newZ);

		//play sound
		world.playSound(null, oldX, oldY, oldZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
		this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

		Random random = getRandom();

		//spawn particles along the path
		int particleCount = 128;
		for (int i = 0; i < particleCount; ++i) {
			double progress = i / (double) (particleCount - 1);
			float vx = (random.nextFloat() - 0.5F) * 0.2F;
			float vy = (random.nextFloat() - 0.5F) * 0.2F;
			float vz = (random.nextFloat() - 0.5F) * 0.2F;
			double px = oldX + (newX - oldX) * progress + (random.nextDouble() - 0.5D) * getWidth() * 2.0D;
			double py = oldY + (newY - oldY) * progress + random.nextDouble() * getHeight();
			double pz = oldZ + (newZ - oldZ) * progress + (random.nextDouble() - 0.5D) * getWidth() * 2.0D;
			world.addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
		}

		Vec3d oldPosVec = new Vec3d(oldX, oldY + getHeight() / 2, oldZ);
		Vec3d newPosVec = new Vec3d(newX, newY + getHeight() / 2, newZ);

		if (oldPosVec.squaredDistanceTo(newPosVec) > 1) {
			//damage players in the path of the teleport
			for (PlayerEntity player : getPlayersAround()) {
				boolean hit = player.getBoundingBox().expand(0.25).rayTrace(oldPosVec, newPosVec)
						.isPresent();
				if (hit) {
					player.damage(DamageSource.mob(this), 6);
				}
			}

			//break blocks in the path of the teleport
			int breakSteps = (int) oldPosVec.distanceTo(newPosVec);
			if (breakSteps >= 2) {
				for (int i = 0; i < breakSteps; i++) {
					float progress = i / (float) (breakSteps - 1);
					int breakX = MathHelper.floor(oldX + (newX - oldX) * progress);
					int breakY = MathHelper.floor(oldY + (newY - oldY) * progress);
					int breakZ = MathHelper.floor(oldZ + (newZ - oldZ) * progress);

					smashBlocksAround(breakX, breakY, breakZ, 1);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public Identifier getBossBarTexture() {
		return BossBarHandler.defaultBossBar;
	}

	@Environment(EnvType.CLIENT)
	private static Rectangle barRect;
	@Environment(EnvType.CLIENT)
	private static Rectangle hpBarRect;

	@Environment(EnvType.CLIENT)
	public Rectangle getBossBarTextureRect() {
		if (barRect == null) {
			barRect = new Rectangle(0, 0, 185, 15);
		}
		return barRect;
	}

	@Environment(EnvType.CLIENT)
	public Rectangle getBossBarHPTextureRect() {
		if (hpBarRect == null) {
			hpBarRect = new Rectangle(0, barRect.y + barRect.height, 181, 7);
		}
		return hpBarRect;
	}

	@Environment(EnvType.CLIENT)
	public int bossBarRenderCallback(MatrixStack ms, int x, int y) {
		ms.push();
		int px = x + 160;
		int py = y + 12;

		MinecraftClient mc = MinecraftClient.getInstance();
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		mc.getItemRenderer().renderGuiItemIcon(stack, px, py);

		mc.textRenderer.drawWithShadow(ms, Integer.toString(playerCount), px + 15, py + 4, 0xFFFFFF);
		ms.pop();

		return 5;
	}

	public UUID getBossInfoUuid() {
		return bossInfoUUID;
	}

	@Environment(EnvType.CLIENT)
	@Nullable
	public ShaderHelper.BotaniaShader getBossBarShaderProgram(boolean background) {
		return background ? null : ShaderHelper.BotaniaShader.DOPPLEGANGER_BAR;
	}

	@Environment(EnvType.CLIENT)
	private ShaderCallback shaderCallback;

	@Environment(EnvType.CLIENT)
	public ShaderCallback getBossBarShaderCallback(boolean background) {
		if (shaderCallback == null) {
			shaderCallback = shader1 -> {
				int grainIntensityUniform = GlStateManager.getUniformLocation(shader1, "grainIntensity");
				int hpFractUniform = GlStateManager.getUniformLocation(shader1, "hpFract");

				float time = getInvulTime();
				float grainIntensity = time > 20 ? 1F : Math.max(hardMode ? 0.5F : 0F, time / 20F);

				ShaderHelper.FLOAT_BUF.position(0);
				ShaderHelper.FLOAT_BUF.put(0, grainIntensity);
				RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);

				ShaderHelper.FLOAT_BUF.position(0);
				ShaderHelper.FLOAT_BUF.put(0, getHealth() / getMaxHealth());
				RenderSystem.glUniform1(hpFractUniform, ShaderHelper.FLOAT_BUF);
			};
		}

		return background ? null : shaderCallback;
	}

	@Override
	public void writeSpawnData(PacketByteBuf buffer) {
		buffer.writeInt(playerCount);
		buffer.writeBoolean(hardMode);
		buffer.writeLong(source.asLong());
		buffer.writeLong(bossInfoUUID.getMostSignificantBits());
		buffer.writeLong(bossInfoUUID.getLeastSignificantBits());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void readSpawnData(PacketByteBuf additionalData) {
		playerCount = additionalData.readInt();
		hardMode = additionalData.readBoolean();
		source = BlockPos.fromLong(additionalData.readLong());
		long msb = additionalData.readLong();
		long lsb = additionalData.readLong();
		bossInfoUUID = new UUID(msb, lsb);
		MinecraftClient.getInstance().getSoundManager().play(new DopplegangerMusic(this));
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	private static class DopplegangerMusic extends MovingSoundInstance {
		private final EntityDoppleganger guardian;

		public DopplegangerMusic(EntityDoppleganger guardian) {
			super(guardian.hardMode ? ModSounds.gaiaMusic2 : ModSounds.gaiaMusic1, SoundCategory.RECORDS);
			this.guardian = guardian;
			this.x = guardian.getSource().getX();
			this.y = guardian.getSource().getY();
			this.z = guardian.getSource().getZ();
			// this.repeat = true; TODO restore once LWJGL3/vanilla bug fixed?
		}

		@Override
		public void tick() {
			if (!guardian.isAlive()) {
				setDone();
			}
		}
	}
}
