/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.Bound;
import vazkii.botania.api.configdata.GaiaFightConfiguration;
import vazkii.botania.api.configdata.MobSpawnData;
import vazkii.botania.api.internal.GaiaFightParticipant;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.advancements.GaiaGuardianNoArmorTrigger;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.BotaniaMonsterTeam;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.loot.BotaniaLootTables;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.mixin.BeaconBlockEntityAccessor;
import vazkii.botania.mixin.MobAccessor;
import vazkii.botania.network.clientbound.ArenaIndicatorEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.common.helper.PlayerHelper.isTruePlayer;

public class GaiaGuardianEntity extends Mob {
	public static final int ARENA_RANGE = 12;
	public static final int ARENA_RANGE_UP = 5;
	public static final int ARENA_RANGE_DOWN = 2;
	public static final int ARENA_PLAYER_RANGE = (int) (ARENA_RANGE * 1.25);
	public static final int DOUBLE_ARENA_PLAYER_RANGE_SQUARED = (2 * ARENA_PLAYER_RANGE) * (2 * ARENA_PLAYER_RANGE);
	public static final int ARENA_TELEPORT_RANGE = ARENA_RANGE - 1;
	public static final int ARENA_TELEPORT_RANGE_SQUARED = ARENA_TELEPORT_RANGE * ARENA_TELEPORT_RANGE;
	public static final int ARENA_MOB_RANGE = (int) (ARENA_RANGE * 1.5);

	private static final int SPAWN_TICKS = 160;
	public static final float MAX_HP = 320F;
	public static final Supplier<IMultiblock> ARENA_MULTIBLOCK = Suppliers.memoize(() -> {
		var beaconBase = PatchouliAPI.get().predicateMatcher(Blocks.IRON_BLOCK,
				state -> state.is(BlockTags.BEACON_BASE_BLOCKS));
		return PatchouliAPI.get().makeMultiblock(
				new String[][] {
						{
								"P_______P",
								"_________",
								"_________",
								"_________",
								"_________",
								"_________",
								"_________",
								"_________",
								"P_______P",
						},
						{
								"_________",
								"_________",
								"_________",
								"_________",
								"____B____",
								"_________",
								"_________",
								"_________",
								"_________",
						},
						{
								"_________",
								"_________",
								"_________",
								"___III___",
								"___I0I___",
								"___III___",
								"_________",
								"_________",
								"_________",
						}
				},
				'P', BotaniaBlocks.gaiaPylon,
				'B', Blocks.BEACON,
				'I', beaconBase,
				'0', beaconBase
		);
	});

	private static final int MOB_SPAWN_START_TICKS = 20;
	private static final int MOB_SPAWN_END_TICKS = 80;
	private static final int MOB_SPAWN_BASE_TICKS = 800;
	private static final int MOB_SPAWN_TICKS = MOB_SPAWN_BASE_TICKS + MOB_SPAWN_START_TICKS + MOB_SPAWN_END_TICKS;
	private static final int MOB_SPAWN_WAVES = 10;
	private static final int MOB_SPAWN_WAVE_TIME = MOB_SPAWN_BASE_TICKS / MOB_SPAWN_WAVES;
	private static final int DAMAGE_CAP = 32;

	private static final String TAG_INVUL_TIME = "invulTime";
	private static final String TAG_AGGRO = "aggro";
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";
	private static final String TAG_MOB_SPAWN_TICKS = "mobSpawnTicks";
	private static final String TAG_HARD_MODE = "hardMode";
	private static final String TAG_PLAYER_COUNT = "playerCount";

	private static final EntityDataAccessor<Integer> DATA_INVUL_TIME = SynchedEntityData.defineId(GaiaGuardianEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Long> DATA_SOURCE_POS = SynchedEntityData.defineId(GaiaGuardianEntity.class, EntityDataSerializers.LONG);
	private static final EntityDataAccessor<Integer> DATA_INITIAL_PLAYER_COUNT = SynchedEntityData.defineId(GaiaGuardianEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Long> DATA_BOSS_INFO_UUID_LOWER = SynchedEntityData.defineId(GaiaGuardianEntity.class, EntityDataSerializers.LONG);
	private static final EntityDataAccessor<Long> DATA_BOSS_INFO_UUID_UPPER = SynchedEntityData.defineId(GaiaGuardianEntity.class, EntityDataSerializers.LONG);

	private static final int PYLON_OFFSET = 4;
	private static final List<BlockPos> PYLON_OFFSETS = ImmutableList.of(
			new BlockPos(PYLON_OFFSET, 1, PYLON_OFFSET),
			new BlockPos(PYLON_OFFSET, 1, -PYLON_OFFSET),
			new BlockPos(-PYLON_OFFSET, 1, PYLON_OFFSET),
			new BlockPos(-PYLON_OFFSET, 1, -PYLON_OFFSET)
	);

	private static final List<ResourceLocation> CHEATY_BLOCKS = Arrays.asList(
			ResourceLocation.fromNamespaceAndPath("openblocks", "beartrap"),
			ResourceLocation.fromNamespaceAndPath("thaumictinkerer", "magnet")
	);

	private static final WeakHashMap<GaiaGuardianEntity, GaiaFightData> SERVER_GAIA_FIGHTS = new WeakHashMap<>();
	private static final WeakHashMap<GaiaGuardianEntity, GaiaFightData> CLIENT_GAIA_FIGHTS = new WeakHashMap<>();
	private static final Supplier<MobSpawnData> FALLBACK_MOB_SPAWN_DATA = Suppliers.memoize(
			() -> MobSpawnData.entityWeight(BotaniaEntities.PIXIE, 1).build());
	private static final ResourceLocation ATTRIBUTE_MODIFIER_ID = botaniaRL("gaia_fight_modifier");

	// this should never collide with the /team command, since space is not allowed in scoreboard team names
	public static final String GAIA_FIGHT_TEAM_NAME = "Gaia-spawned Monsters";
	/**
	 * Team for mobs spawned in gaia fights.
	 *
	 * @see vazkii.botania.mixin.EntityMixin
	 */
	public static final Team GAIA_FIGHT_TEAM = new BotaniaMonsterTeam(GAIA_FIGHT_TEAM_NAME);

	private boolean spawnLandmines = false;
	private boolean spawnPixies = false;
	private boolean anyWithArmor = false;
	private boolean aggro = false;
	private int tpDelay = 0;
	private int mobSpawnTicks = 0;
	private boolean hardMode = false;
	private BlockPos source = Bound.UNBOUND_POS;
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(BotaniaEntities.DOPPLEGANGER.getDescription(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);
	private UUID bossInfoUUID = bossInfo.getId();
	@Nullable
	public Player trueKiller = null;
	@Nullable
	private GaiaFightConfiguration config;

	public GaiaGuardianEntity(EntityType<GaiaGuardianEntity> type, Level world) {
		super(type, world);
		xpReward = 825;
		entityData.set(DATA_BOSS_INFO_UUID_LOWER, bossInfoUUID.getLeastSignificantBits());
		entityData.set(DATA_BOSS_INFO_UUID_UPPER, bossInfoUUID.getMostSignificantBits());
		if (world.isClientSide) {
			Proxy.INSTANCE.addBoss(this);
		}
	}

	private <T> Optional<T> getConfigData(Function<GaiaFightConfiguration, T> fieldGetter) {
		return Optional.ofNullable(config).map(fieldGetter);
	}

	private OptionalInt getConfigDataInt(ToIntFunction<GaiaFightConfiguration> fieldGetter) {
		return config == null ? OptionalInt.empty() : OptionalInt.of(fieldGetter.applyAsInt(config));
	}

	private OptionalInt getConfigDataIntSample(Function<GaiaFightConfiguration, IntProvider> fieldGetter) {
		return config == null ? OptionalInt.empty() : OptionalInt.of(fieldGetter.apply(config).sample(random));
	}

	public static boolean validateArena(Player player, Level world, BlockPos pos) {
		//initial checks
		if (!(world.getBlockEntity(pos) instanceof BeaconBlockEntity) || !isTruePlayer(player)
				|| anyActiveArenasAround(world, pos)) {
			return false;
		}

		//check difficulty
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (!world.isClientSide) {
				player.sendSystemMessage(Component.translatable("botaniamisc.peacefulNoob").withStyle(ChatFormatting.RED));
			}
			return false;
		}

		//check pylons
		List<BlockPos> invalidPylonBlocks = checkPylons(world, pos);
		if (!invalidPylonBlocks.isEmpty()) {
			if (world.isClientSide) {
				warnInvalidBlocks(world, invalidPylonBlocks);
			} else {
				player.sendSystemMessage(Component.translatable("botaniamisc.needsCatalysts").withStyle(ChatFormatting.RED));
			}

			return false;
		}

		//check arena shape
		List<BlockPos> invalidArenaBlocks = checkArena(world, pos);
		if (!invalidArenaBlocks.isEmpty()) {
			if (world.isClientSide) {
				warnInvalidBlocks(world, invalidArenaBlocks);
			} else {
				XplatAbstractions.instance().sendToPlayer(player, new ArenaIndicatorEffectPacket(pos));

				player.sendSystemMessage(Component.translatable("botaniamisc.badArena").withStyle(ChatFormatting.RED));
			}

			return false;
		}
		return true;
	}

	public static boolean spawn(Player player, ItemStack stack, Level world, BlockPos pos, boolean hard) {
		if (!validateArena(player, world, pos)) {
			return false;
		}

		//all checks ok, spawn the boss
		if (world.isClientSide) {
			return true;
		}
		GaiaGuardianEntity e = BotaniaEntities.DOPPLEGANGER.create(world);
		if (e == null) {
			return false;
		}
		stack.shrink(1);

		e.setPos(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
		e.setInvulTime(SPAWN_TICKS);
		e.setHealth(1F);
		e.bossInfo.setProgress(0.0f);
		e.setSource(pos);
		e.mobSpawnTicks = MOB_SPAWN_TICKS;
		e.setHardMode(hard);

		List<Player> playersAround = e.getPlayersAround();
		int initialPlayerCount = playersAround.size();
		e.setInitialPlayerCount(initialPlayerCount);

		float healthMultiplier = 1;
		if (initialPlayerCount > 1) {
			healthMultiplier += initialPlayerCount * 0.25F;
		}
		e.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MAX_HP * healthMultiplier);

		if (hard) {
			e.getAttribute(Attributes.ARMOR).setBaseValue(15);
		}

		e.playSound(BotaniaSounds.gaiaSummon, 1F, 1F);
		e.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.EVENT, null);
		world.addFreshEntity(e);

		e.updateGaiaFight();

		for (Player nearbyPlayer : playersAround) {
			if (nearbyPlayer instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, e);
			}
		}

		return true;
	}

	private void updateGaiaFight() {
		if (isAlive()) {
			getGaiaFightMap(level()).put(this, new GaiaFightData(source, hardMode));
			if (!level().isClientSide) {
				level().getEntities(this, MathHelper.inflateBoxAround(source, ARENA_MOB_RANGE),
						this::isGaiaFightParticipant)
						.forEach(mob -> ((Mob) mob).restrictTo(source, ARENA_RANGE));
			}
		} else {
			getGaiaFightMap(level()).remove(this);
			if (!level().isClientSide) {
				level().getEntities(this, MathHelper.inflateBoxAround(source, ARENA_MOB_RANGE),
						this::isGaiaFightParticipant)
						.forEach(mob -> {
							XplatAbstractions.instance().setGaiaFightParticipant((Mob) mob, null);
							((Mob) mob).clearRestriction();
						});
			}
		}
	}

	private boolean isGaiaFightParticipant(Entity e) {
		if (!(e instanceof Mob mob) || !mob.isAlive()) {
			return false;
		}
		Optional<GaiaFightParticipant> participant = XplatAbstractions.instance().getGaiaFightParticipant(mob);
		return participant.filter(p -> p.getSourcePos().equals(source)).isPresent();
	}

	private static Map<GaiaGuardianEntity, GaiaFightData> getGaiaFightMap(Level level) {
		return level.isClientSide() ? CLIENT_GAIA_FIGHTS : SERVER_GAIA_FIGHTS;
	}

	private static List<BlockPos> checkPylons(Level world, BlockPos beaconPos) {
		List<BlockPos> invalidPylonBlocks = new ArrayList<>();

		for (BlockPos pylonOffset : PYLON_OFFSETS) {
			BlockPos expectedPylonPos = beaconPos.offset(pylonOffset);

			BlockState state = world.getBlockState(expectedPylonPos);
			if (!state.is(BotaniaBlocks.gaiaPylon)) {
				invalidPylonBlocks.add(expectedPylonPos);
			}
		}

		return invalidPylonBlocks;
	}

	private static List<BlockPos> checkArena(Level world, BlockPos beaconPos) {
		List<BlockPos> trippedPositions = new ArrayList<>();
		Optional<BeaconBlockEntity> beacon = world.getBlockEntity(beaconPos, BlockEntityType.BEACON);
		if (beacon.isEmpty()) {
			trippedPositions.add(beaconPos);
		} else if (((BeaconBlockEntityAccessor) beacon.get()).botania_getLevels() <= 0) {
			trippedPositions.add(beaconPos);
			trippedPositions.add(beaconPos.above());
			trippedPositions.add(beaconPos.above(2));
		}

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = -ARENA_RANGE; x <= ARENA_RANGE; x++) {
			for (int z = -ARENA_RANGE; z <= ARENA_RANGE; z++) {
				if (Math.abs(x) == PYLON_OFFSET && Math.abs(z) == PYLON_OFFSET
						|| MathHelper.distSqr2D(x, z) > ARENA_RANGE * ARENA_RANGE) {
					continue; // Ignore pylons and out of circle
				}

				boolean hasFloor = false;

				for (int y = -ARENA_RANGE_DOWN; y <= ARENA_RANGE_UP; y++) {
					if (x == 0 && y == 0 && z == 0) {
						continue; //the beacon
					}

					pos.setWithOffset(beaconPos, x, y, z);

					BlockState state = world.getBlockState(pos);

					boolean allowBlockHere = y < 0;
					boolean isBlockHere = !state.getCollisionShape(world, pos).isEmpty();

					if (allowBlockHere && isBlockHere) //floor is here! good
					{
						hasFloor = true;
					}

					if (!allowBlockHere && isBlockHere && !state.is(BotaniaTags.Blocks.GAIA_GUARDIAN_IMMUNE)) //ceiling is obstructed in this column
					{
						trippedPositions.add(pos.immutable());
					}

					if (y == 0 && !hasFloor) //column is entirely missing floor
					{
						trippedPositions.add(pos.move(Direction.DOWN).immutable());
					}
				}
			}
		}

		return trippedPositions;
	}

	private static void warnInvalidBlocks(Level world, Iterable<BlockPos> invalidPositions) {
		WispParticleData dataQuick = WispParticleData.wisp(0.5F, 1, 0.2F, 0.2F, 2, false);
		WispParticleData dataSlow = WispParticleData.wisp(0.5F, 1, 0.2F, 0.2F, 8, false);
		for (BlockPos pos_ : invalidPositions) {
			world.addParticle(dataQuick, pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 0, 0, 0);
			world.addParticle(dataSlow, pos_.getX() + 0.5, pos_.getY() + 0.5, pos_.getZ() + 0.5, 0, 0, 0);
		}
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, ARENA_MOB_RANGE));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_INVUL_TIME, 0);
		builder.define(DATA_SOURCE_POS, 0L);
		builder.define(DATA_INITIAL_PLAYER_COUNT, 0);
		builder.define(DATA_BOSS_INFO_UUID_LOWER, 0L);
		builder.define(DATA_BOSS_INFO_UUID_UPPER, 0L);
	}

	public int getInvulTime() {
		return entityData.get(DATA_INVUL_TIME);
	}

	public BlockPos getSource() {
		return source;
	}

	public void setInvulTime(int time) {
		entityData.set(DATA_INVUL_TIME, time);
	}

	private void setSource(BlockPos source) {
		this.source = source;
		entityData.set(DATA_SOURCE_POS, source.asLong());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag cmp) {
		super.addAdditionalSaveData(cmp);
		cmp.putInt(TAG_INVUL_TIME, getInvulTime());
		cmp.putBoolean(TAG_AGGRO, aggro);
		cmp.putInt(TAG_MOB_SPAWN_TICKS, mobSpawnTicks);

		cmp.putInt(TAG_SOURCE_X, source.getX());
		cmp.putInt(TAG_SOURCE_Y, source.getY());
		cmp.putInt(TAG_SOURCE_Z, source.getZ());

		cmp.putBoolean(TAG_HARD_MODE, hardMode);
		cmp.putInt(TAG_PLAYER_COUNT, getInitialPlayerCount());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag cmp) {
		super.readAdditionalSaveData(cmp);
		setInvulTime(cmp.getInt(TAG_INVUL_TIME));
		aggro = cmp.getBoolean(TAG_AGGRO);
		mobSpawnTicks = cmp.getInt(TAG_MOB_SPAWN_TICKS);

		int x = cmp.getInt(TAG_SOURCE_X);
		int y = cmp.getInt(TAG_SOURCE_Y);
		int z = cmp.getInt(TAG_SOURCE_Z);
		setSource(new BlockPos(x, y, z));

		setHardMode(cmp.getBoolean(TAG_HARD_MODE));
		setInitialPlayerCount(cmp.contains(TAG_PLAYER_COUNT) ? cmp.getInt(TAG_PLAYER_COUNT) : 1);

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	private void setHardMode(boolean hardMode) {
		this.hardMode = hardMode;
		this.config = BotaniaAPI.instance().getConfigData().getGaiaFightConfiguration(
				hardMode ? GaiaFightConfiguration.HARD : GaiaFightConfiguration.NORMAL);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
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
	public boolean hurt(DamageSource source, float amount) {
		Entity e = source.getEntity();
		if (e instanceof Player player && isTruePlayer(e) && getInvulTime() == 0) {

			if (!playersWhoAttacked.contains(player.getUUID())) {
				playersWhoAttacked.add(player.getUUID());
			}

			return super.hurt(source, Math.min(DAMAGE_CAP, amount));
		}

		return false;
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		super.actuallyHurt(source, Math.min(DAMAGE_CAP, amount));

		Entity attacker = source.getDirectEntity();
		if (attacker != null) {
			Vec3 thisVector = VecHelper.fromEntityCenter(this);
			Vec3 playerVector = VecHelper.fromEntityCenter(attacker);
			Vec3 motionVector = thisVector.subtract(playerVector).normalize().scale(0.75);

			if (getHealth() > 0) {
				setDeltaMovement(-motionVector.x, 0.5, -motionVector.z);
				tpDelay = 4;
				spawnPixies = true;
			}
		}
		invulnerableTime = Math.max(invulnerableTime, 20);
	}

	@Override
	protected float getDamageAfterArmorAbsorb(DamageSource source, float damage) {
		return super.getDamageAfterArmorAbsorb(source, Math.min(DAMAGE_CAP, damage));
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		updateGaiaFight();
		LivingEntity lastAttacker = getKillCredit();

		if (!level().isClientSide()) {
			for (UUID u : playersWhoAttacked) {
				Player player = level().getPlayerByUUID(u);
				if (!isTruePlayer(player)) {
					continue;
				}
				DamageSource currSource = player == lastAttacker ? source : player.damageSources().playerAttack(player);
				if (player != lastAttacker) {
					// Vanilla handles this in attack code, but only for the killer
					CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger((ServerPlayer) player, this, currSource);
				}
				if (!anyWithArmor) {
					GaiaGuardianNoArmorTrigger.INSTANCE.trigger((ServerPlayer) player, this, currSource);
				}
			}

			// Clear wither from nearby players
			for (Player player : getPlayersAround()) {
				if (player.getEffect(MobEffects.WITHER) != null) {
					player.removeEffect(MobEffects.WITHER);
				}
			}

			// Stop all the pixies leftover from the fight
			for (PixieEntity pixie : level().getEntitiesOfClass(PixieEntity.class,
					MathHelper.inflateBoxAround(getSource(), ARENA_PLAYER_RANGE), p -> p.isAlive() && p.isGaia())) {
				pixie.spawnAnim();
				pixie.discard();
			}
			for (MagicLandmineEntity landmine : level().getEntitiesOfClass(MagicLandmineEntity.class,
					MathHelper.inflateBoxAround(getSource(), ARENA_PLAYER_RANGE))) {
				landmine.discard();
			}
		}

		playSound(BotaniaSounds.gaiaDeath, 1F, (1F + (level().getRandom().nextFloat() - level().getRandom().nextFloat()) * 0.2F) * 0.7F);
		level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1D, 0D, 0D);
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public ResourceKey<LootTable> getDefaultLootTable() {
		if (mobSpawnTicks > 0) {
			return BuiltInLootTables.EMPTY;
		}
		return super.getDefaultLootTable();
	}

	@Override
	protected void dropFromLootTable(DamageSource source, boolean wasRecentlyHit) {
		// Save true killer, they get extra loot
		if (wasRecentlyHit && isTruePlayer(source.getEntity())) {
			trueKiller = (Player) source.getEntity();
		}

		// potential head drop
		super.dropFromLootTable(source, wasRecentlyHit);

		ResourceKey<LootTable> playerLoot = getConfigData(GaiaFightConfiguration::getRewardLootTableKey)
				.orElse(BotaniaLootTables.GAIA_GUARDIAN_REWARD);
		// Generate loot table for every single attacking player
		for (UUID u : playersWhoAttacked) {
			Player player = level().getPlayerByUUID(u);
			if (!isTruePlayer(player)) {
				continue;
			}

			Player saveLastAttacker = lastHurtByPlayer;
			Vec3 savePos = position();

			lastHurtByPlayer = player; // Fake attacking player as the killer
			// Spoof pos so drops spawn at the player
			setPos(player.getX(), player.getY(), player.getZ());
			((MobAccessor) this).botania_setLootTable(playerLoot);
			super.dropFromLootTable(player.damageSources().playerAttack(player), wasRecentlyHit);
			setPos(savePos.x(), savePos.y(), savePos.z());
			lastHurtByPlayer = saveLastAttacker;
		}

		trueKiller = null;
	}

	@Override
	public void remove(RemovalReason reason) {
		Proxy.INSTANCE.removeBoss(this);
		super.remove(reason);
		updateGaiaFight();
	}

	@Nullable
	public static Boolean isGaiaFightHardMode(Player player) {
		if (player.isSpectator() || !player.isAlive()) {
			return null;
		}

		var nearestFight = getNearestGaiaFight(player.level(), player.position());
		if (nearestFight == null) {
			return null;
		}
		GaiaFightData fightData = nearestFight.second();
		if (!fightData.arenaCenter().closerToCenterThan(player.position(), ARENA_PLAYER_RANGE)) {
			return null;
		}

		var gaiaGuardian = nearestFight.first();
		return gaiaGuardian.isAlive() ? fightData.hardMode() : null;
	}

	@Nullable
	public static Pair<GaiaGuardianEntity, GaiaFightData> getNearestGaiaFight(Level level, Position pos) {
		double nearestDist = Double.POSITIVE_INFINITY;
		GaiaGuardianEntity nearest = null;
		var gaiaFights = getGaiaFightMap(level);
		for (var entry : gaiaFights.entrySet()) {
			GaiaGuardianEntity gaiaGuardian = entry.getKey();
			if (!gaiaGuardian.isAlive() || gaiaGuardian.level() != level) {
				continue;
			}
			var fightData = entry.getValue();
			double dist = fightData.arenaCenter().distToCenterSqr(pos);
			if (nearestDist > dist) {
				nearestDist = dist;
				nearest = gaiaGuardian;
			}
		}
		return nearest != null ? Pair.of(nearest, gaiaFights.get(nearest)) : null;
	}

	public static boolean isGaiaFightBeacon(Level level, int x, int y, int z) {
		var gaiaFights = getGaiaFightMap(level);
		if (gaiaFights.isEmpty()) {
			return false;
		}
		BlockPos checkPos = new BlockPos(x, y, z);
		for (var entry : gaiaFights.entrySet()) {
			GaiaGuardianEntity gaiaGuardian = entry.getKey();
			if (gaiaGuardian.isAlive() && gaiaGuardian.level() == level
					&& entry.getValue().arenaCenter().equals(checkPos)) {
				return true;
			}
		}
		return false;
	}

	public List<Player> getPlayersAround() {
		List<Player> players = PlayerHelper.getRealPlayersIn(level(),
				MathHelper.inflateBoxAround(source, ARENA_PLAYER_RANGE));
		players.removeIf(player -> !source.closerToCenterThan(player.position(), ARENA_PLAYER_RANGE));
		return players;
	}

	public int getInitialPlayerCount() {
		return entityData.get(DATA_INITIAL_PLAYER_COUNT);
	}

	private void setInitialPlayerCount(int playerCount) {
		entityData.set(DATA_INITIAL_PLAYER_COUNT, playerCount);
	}

	private static boolean anyActiveArenasAround(Level world, BlockPos source) {
		var arena = getNearestGaiaFight(world, source.getCenter());
		return arena != null
				&& MathHelper.distSqr(arena.second().arenaCenter(), source) < DOUBLE_ARENA_PLAYER_RANGE_SQUARED;
	}

	private void particles() {
		Vec3 centerPos = source.getCenter();
		for (int i = 0; i < 360; i += 8) {
			float r = 0.6F;
			float g = 0F;
			float b = 0.2F;
			float m = 0.15F;
			float mv = 0.35F;

			float rad = i * ((float) Math.PI / 180F);

			WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
			level().addAlwaysVisibleParticle(data,
					centerPos.x() - Math.cos(rad) * ARENA_RANGE,
					centerPos.y(),
					centerPos.z() - Math.sin(rad) * ARENA_RANGE,
					(random.nextFloat() - 0.5F) * m, (random.nextFloat() - 0.5F) * mv, (random.nextFloat() - 0.5F) * m);
		}

		if (getInvulTime() > 10) {
			Vec3 pos = VecHelper.fromEntityCenter(this).subtract(0, 0.2, 0);
			for (BlockPos pylonOffset : PYLON_OFFSETS) {
				Vec3 pylonPos = centerPos.add(pylonOffset.getX(), pylonOffset.getY(), pylonOffset.getZ());
				double worldTime = tickCount;
				worldTime /= 5;

				float rad = 0.75F + random.nextFloat() * 0.05F;
				double xp = Math.cos(worldTime) * rad;
				double zp = Math.sin(worldTime) * rad;

				Vec3 partPos = new Vec3(xp, pylonPos.y, zp);
				Vec3 mot = pos.subtract(partPos).scale(0.04);

				float r = 0.7F + random.nextFloat() * 0.3F;
				float g = random.nextFloat() * 0.3F;
				float b = 0.7F + random.nextFloat() * 0.3F;

				WispParticleData data = WispParticleData.wisp(0.25F + random.nextFloat() * 0.1F, r, g, b, 1);
				level().addParticle(data, partPos.x, partPos.y, partPos.z, 0, -(-0.075F - random.nextFloat() * 0.015F), 0);
				WispParticleData data1 = WispParticleData.wisp(0.4F, r, g, b);
				level().addParticle(data1, partPos.x, partPos.y, partPos.z, (float) mot.x, (float) mot.y, (float) mot.z);
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
					BlockState state = level().getBlockState(pos);
					Block block = state.getBlock();

					if (state.getDestroySpeed(level(), pos) == -1) {
						continue;
					}

					if (CHEATY_BLOCKS.contains(BuiltInRegistries.BLOCK.getKey(block))) {
						level().destroyBlock(pos, true);
					} else {
						//don't break immune blocks
						if (state.is(BotaniaTags.Blocks.GAIA_GUARDIAN_IMMUNE)) {
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

						level().destroyBlock(pos, true);
					}
				}
			}
		}
	}

	private void keepInsideArena(Player player) {
		Vec3 center = source.getCenter();
		Vec3 playerPos = player.position();
		double distSq = center.distanceToSqr(playerPos);
		if (distSq > ARENA_RANGE * ARENA_RANGE) {
			Vec3 diff = center.subtract(playerPos);
			Vec3 motion = diff.scale(1.0 / ARENA_RANGE);

			player.setDeltaMovement(motion.x, Math.min(motion.y, 0) + 0.2, motion.z);
			player.hurtMarked = true;
		}
	}

	private void spawnMobs(List<Player> players) {
		int initialPlayerCount = getInitialPlayerCount();
		ServerLevel serverLevel = (ServerLevel) level();
		float range = 6F;
		WeightedRandomList<MobSpawnData> spawnedMobs = getConfigData(GaiaFightConfiguration::spawnedMobs)
				.orElseGet(WeightedRandomList::create);
		for (int pl = 0; pl < initialPlayerCount; pl++) {
			int numSpawnAttempts = getConfigDataIntSample(GaiaFightConfiguration::mobSpawnsPerPlayer).orElse(1);
			for (int i = 0; i < numSpawnAttempts; i++) {
				MobSpawnData pickedMobType = spawnedMobs.getRandom(random).orElseGet(FALLBACK_MOB_SPAWN_DATA);
				int numToSpawn = pickedMobType.count.sample(random);
				for (int j = 0; j < numToSpawn; j++) {
					if (pickedMobType.type != BotaniaEntities.PIXIE) {
						Entity entity = pickedMobType.type.create(serverLevel);
						if (!(entity instanceof Mob mob)) {
							continue;
						}

						if (pickedMobType.nbt != null) {
							mob.readAdditionalSaveData(pickedMobType.nbt);
						}
						if (pickedMobType.spawnAsBaby != null) {
							mob.setBaby(pickedMobType.spawnAsBaby);
						}

						mob.absMoveTo(getX() + 0.5 + Math.random() * range - range / 2,
								getY() - 1,
								getZ() + 0.5 + Math.random() * range - range / 2,
								random.nextFloat() * 360, 0);
						mob.setDeltaMovement(Vec3.ZERO);

						applyAttributesAndEffects(pickedMobType, mob);

						mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
						LooniumBlockEntity.equipSpawnedMob(serverLevel, pickedMobType, mob);

						// in case the mob spawned with a vehicle or passenger(s), ensure those don't drop unexpected loot
						mob.getRootVehicle().getPassengersAndSelf().forEach(e -> {
							if (e instanceof Mob otherMob) {
								mob.restrictTo(source, ARENA_RANGE);
								XplatAbstractions.instance().setGaiaFightParticipant(mob, this);
								Optional<MobSpawnData> spawnData = spawnedMobs.unwrap().stream()
										.filter(mobSpawnData -> mobSpawnData.type.tryCast(otherMob) != null)
										.findFirst();
								if (spawnData.isPresent() && !spawnData.get().allowEquipmentDrops) {
									// prevent armor/weapon drops on player kill, also no nautilus shells from drowned:
									Arrays.stream(EquipmentSlot.values())
											.forEach(slot -> otherMob.setDropChance(slot, 0));
								}
								if (mob instanceof PatrollingMonster patroller && patroller.isPatrolLeader()) {
									patroller.setPatrolLeader(false);
									patroller.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
								}

								if (e != mob && spawnData.isPresent()) {
									applyAttributesAndEffects(spawnData.get(), mob);
								}
							}
						});

						if (!serverLevel.tryAddFreshEntityWithPassengers(mob)) {
							return;
						}

						mob.spawnAnim();
						serverLevel.gameEvent(mob, GameEvent.ENTITY_PLACE, mob.position());

					} else if (!players.isEmpty()) {
						// TODO: pixie entity should probably be adjusted to make this special handling redundant
						PixieEntity pixie = new PixieEntity(serverLevel, true);
						pixie.setProps(players.get(random.nextInt(players.size())), this, 8);
						pixie.setPos(getRandomX(2), getY() + 2, getRandomZ(2));
						pixie.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pixie.blockPosition()),
								MobSpawnType.MOB_SUMMONED, null);
						serverLevel.addFreshEntity(pixie);
					}
				}
			}
		}
	}

	private void applyAttributesAndEffects(MobSpawnData pickedMobType, Mob mob) {
		LooniumBlockEntity.applyAttributesAndEffects(pickedMobType, mob,
				getConfigData(GaiaFightConfiguration::attributeModifiers).orElse(null),
				getConfigData(GaiaFightConfiguration::effectsToApply).orElse(null),
				ATTRIBUTE_MODIFIER_ID);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		int invul = getInvulTime();

		if (level().isClientSide()) {
			particles();
			Player player = Proxy.INSTANCE.getClientPlayer();
			if (!player.isCreative() && getPlayersAround().contains(player)) {
				player.getAbilities().flying = false;
			}
			return;
		}

		bossInfo.setProgress(getHealth() / getMaxHealth());

		if (isPassenger()) {
			stopRiding();
		}

		if (level().getDifficulty() == Difficulty.PEACEFUL) {
			discard();
			return;
		}

		smashBlocksAround(Mth.floor(getX()), Mth.floor(getY()), Mth.floor(getZ()), 1);

		List<Player> players = getPlayersAround();

		if (players.isEmpty() && !level().players().isEmpty()) {
			discard();
		} else {
			for (Player player : players) {
				for (EquipmentSlot e : EquipmentSlot.values()) {
					if (e.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !player.getItemBySlot(e).isEmpty()) {
						anyWithArmor = true;
						break;
					}
				}

				//also see SleepingHandler
				if (player.isSleeping()) {
					player.stopSleeping();
				}

				keepInsideArena(player);
				player.getAbilities().flying &= player.isCreative();
			}
		}

		if (!isAlive() || players.isEmpty()) {
			return;
		}

		if (!SERVER_GAIA_FIGHTS.containsKey(this)) {
			updateGaiaFight();
		}

		int missileSpawnInterval = getConfigData(GaiaFightConfiguration::missileSpawnInterval).orElse(0);
		int missileSpawnCount = getConfigDataIntSample(GaiaFightConfiguration::missileSpawnCount).orElse(0);
		boolean spawnMissiles = missileSpawnInterval > 0 && tickCount % missileSpawnInterval < missileSpawnCount;

		if (invul > 0 && mobSpawnTicks == MOB_SPAWN_TICKS) {
			if (invul < SPAWN_TICKS && invul > SPAWN_TICKS / 2
					&& level().getRandom().nextInt(SPAWN_TICKS - invul + 1) == 0) {
				for (int i = 0; i < 2; i++) {
					spawnAnim();
				}
			}

			setHealth(getHealth() + (getMaxHealth() - 1F) / SPAWN_TICKS);
			setInvulTime(invul - 1);

			setDeltaMovement(getDeltaMovement().x(), 0, getDeltaMovement().z());
		} else {
			if (aggro) {
				boolean dying = getHealth() / getMaxHealth() < 0.2;
				if (dying && mobSpawnTicks > 0) {
					if (tpDelay > 0 && MOB_SPAWN_TICKS == mobSpawnTicks) {
						tpDelay--;
						setInvulTime(invul + 1);
						if (tpDelay == 0 && getHealth() > 0) {
							teleportToBeacon();
						}
					} else {
						setDeltaMovement(Vec3.ZERO);

						int reverseTicks = MOB_SPAWN_TICKS - mobSpawnTicks;
						if (reverseTicks < MOB_SPAWN_START_TICKS) {
							setDeltaMovement(getDeltaMovement().x(), 0.2, getDeltaMovement().z());
							setInvulTime(invul + 1);
						}

						if (reverseTicks > MOB_SPAWN_START_TICKS * 2 && mobSpawnTicks > MOB_SPAWN_END_TICKS
								&& mobSpawnTicks % MOB_SPAWN_WAVE_TIME == 0) {
							spawnMobs(players);

							// TODO: move to config
							if (hardMode && tickCount % 3 < 2) {
								int initialPlayerCount = getInitialPlayerCount();
								for (int i = 0; i < initialPlayerCount; i++) {
									spawnMissile();
								}
								spawnMissiles = false;
							}
						}

						mobSpawnTicks--;
						tpDelay = 10;
					}
				} else if (tpDelay > 0) {
					if (invul > 0) {
						setInvulTime(invul - 1);
					}

					tpDelay--;
					if (tpDelay == 0 && getHealth() > 0) {
						teleportRandomly();

						if (spawnLandmines) {
							int count = getConfigDataIntSample(
									dying ? GaiaFightConfiguration::dyingMineCount : GaiaFightConfiguration::mineCount
							).orElse(6);
							for (int i = 0; i < count; i++) {
								int x = source.getX() - 10 + random.nextInt(20);
								int y = (int) players.get(random.nextInt(players.size())).getY();
								int z = source.getZ() - 10 + random.nextInt(20);

								MagicLandmineEntity landmine = BotaniaEntities.MAGIC_LANDMINE.create(level());
								if (landmine != null) {
									landmine.setPos(x + 0.5, y, z + 0.5);
									landmine.summoner = this;
									level().addFreshEntity(landmine);
								}
							}
						}

						int initialPlayerCount = getInitialPlayerCount();
						for (int pl = 0; pl < initialPlayerCount; pl++) {
							int pixiesCount = getConfigDataIntSample(spawnPixies
									? GaiaFightConfiguration::pixiesAfterHurtCount
									: GaiaFightConfiguration::pixiesAfterTeleportCount
							).orElse(1);
							for (int i = 0; i < pixiesCount; i++) {
								PixieEntity pixie = new PixieEntity(level(), true);
								pixie.setProps(players.get(random.nextInt(players.size())), this, 8);
								pixie.setPos(getRandomX(1), getY() + 2, getRandomZ(1));
								pixie.finalizeSpawn((ServerLevelAccessor) level(),
										level().getCurrentDifficultyAt(pixie.blockPosition()),
										MobSpawnType.MOB_SUMMONED, null);
								level().addFreshEntity(pixie);
							}
						}

						tpDelay = getConfigDataInt(
								dying ? GaiaFightConfiguration::dyingTeleportDelay : GaiaFightConfiguration::teleportDelay
						).orElse(40);
						spawnLandmines = true;
						spawnPixies = false;
					}
				} else {
					// in case of reload during fight
					tpDelay = 60;
				}

				if (spawnMissiles) {
					spawnMissile();
				}
			} else {
				tpDelay = 30; // Trigger first teleport
				aggro = true;
			}
		}
	}

	@Override
	public boolean canChangeDimensions(Level oldLevel, Level newLevel) {
		return false;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		bossInfo.removePlayer(player);
	}

	@Override
	protected void pushEntities() {
		if (getInvulTime() == 0) {
			super.pushEntities();
		}
	}

	@Override
	public boolean isPushable() {
		return super.isPushable() && getInvulTime() == 0;
	}

	private void spawnMissile() {
		MagicMissileEntity missile = new MagicMissileEntity(this, true);
		missile.setPos(getX() + (Math.random() - 0.5 * 0.1), getY() + 2.4 + (Math.random() - 0.5 * 0.1), getZ() + (Math.random() - 0.5 * 0.1));
		if (missile.findTarget()) {
			playSound(BotaniaSounds.missile, 1F, 0.8F + (float) Math.random() * 0.2F);
			level().addFreshEntity(missile);
		}
	}

	private void teleportRandomly() {
		//choose a location to teleport to
		double oldX = getX(), oldY = getY(), oldZ = getZ();
		double newX, newY = 0, newZ;

		int tries = 0;
		do {
			newX = (2 * random.nextDouble() - 1) * ARENA_TELEPORT_RANGE;
			newZ = (2 * random.nextDouble() - 1) * ARENA_TELEPORT_RANGE;
			tries++;
			//ensure it's inside the arena ring, and not just its bounding square
		} while (tries < 10 && MathHelper.distSqr2D(newX, newZ) > ARENA_TELEPORT_RANGE_SQUARED);

		if (tries == 10) {
			//failsafe: teleport to the beacon
			newX = 0;
			newY = 1.6;
			newZ = 0;
		}

		doTeleport(source.getX() + 0.5 + newX, source.getY() + newY, source.getZ() + 0.5 + newZ, oldX, oldY, oldZ);
	}

	private void teleportToBeacon() {
		Vec3 dest = source.getBottomCenter();

		doTeleport(dest.x, dest.y + 1, dest.z, getX(), getY(), getZ());
	}

	private void doTeleport(double newX, double newY, double newZ, double oldX, double oldY, double oldZ) {
		//for low-floor arenas, ensure landing on the ground
		BlockPos tentativeFloorPos = BlockPos.containing(newX, newY - 1, newZ);
		if (level().getBlockState(tentativeFloorPos).getCollisionShape(level(), tentativeFloorPos).isEmpty()) {
			newY--;
		}

		//teleport there
		teleportTo(newX, newY, newZ);

		//play sound
		level().playSound(null, oldX, oldY, oldZ, BotaniaSounds.gaiaTeleport, this.getSoundSource(), 1F, 1F);
		this.playSound(BotaniaSounds.gaiaTeleport, 1F, 1F);

		var random = getRandom();

		//spawn particles along the path
		int particleCount = 128;
		for (int i = 0; i < particleCount; ++i) {
			double progress = i / (double) (particleCount - 1);
			float vx = (random.nextFloat() - 0.5F) * 0.2F;
			float vy = (random.nextFloat() - 0.5F) * 0.2F;
			float vz = (random.nextFloat() - 0.5F) * 0.2F;
			double px = oldX + (newX - oldX) * progress + (random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
			double py = oldY + (newY - oldY) * progress + random.nextDouble() * getBbHeight();
			double pz = oldZ + (newZ - oldZ) * progress + (random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
			level().addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
		}

		Vec3 oldPosVec = new Vec3(oldX, oldY + getBbHeight() / 2, oldZ);
		Vec3 newPosVec = new Vec3(newX, newY + getBbHeight() / 2, newZ);

		if (oldPosVec.distanceToSqr(newPosVec) > 1) {
			//damage players in the path of the teleport
			for (Player player : getPlayersAround()) {
				boolean hit = player.getBoundingBox().inflate(0.25).clip(oldPosVec, newPosVec)
						.isPresent();
				if (hit) {
					player.hurt(damageSources().mobAttack(this), 6);
				}
			}

			//break blocks in the path of the teleport
			int breakSteps = (int) oldPosVec.distanceTo(newPosVec);
			if (breakSteps >= 2) {
				for (int i = 0; i < breakSteps; i++) {
					float progress = i / (float) (breakSteps - 1);
					int breakX = Mth.floor(oldX + (newX - oldX) * progress);
					int breakY = Mth.floor(oldY + (newY - oldY) * progress);
					int breakZ = Mth.floor(oldZ + (newZ - oldZ) * progress);

					smashBlocksAround(breakX, breakY, breakZ, 1);
				}
			}
		}
	}

	public UUID getBossInfoUuid() {
		return bossInfoUUID;
	}

	public boolean isHardMode() {
		return hardMode;
	}

	@Override
	public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> dataValues) {
		super.onSyncedDataUpdated(dataValues);

		if (dataValues.stream().map(SynchedEntityData.DataValue::id)
				.anyMatch(id -> id == DATA_SOURCE_POS.id())) {
			this.source = BlockPos.of(entityData.get(DATA_SOURCE_POS));
			updateGaiaFight();
		}
		if (dataValues.stream().map(SynchedEntityData.DataValue::id)
				.anyMatch(Set.of(DATA_BOSS_INFO_UUID_LOWER.id(), DATA_BOSS_INFO_UUID_UPPER.id())::contains)) {
			this.bossInfoUUID = new UUID(entityData.get(DATA_BOSS_INFO_UUID_UPPER), entityData.get(DATA_BOSS_INFO_UUID_LOWER));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, entity, hardMode ? 1 : 0);
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.hardMode = packet.getData() != 0;
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}

	@Override
	public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean canUsePortal(boolean allowPassengers) {
		return false;
	}

	public record GaiaFightData(BlockPos arenaCenter, boolean hardMode) {
	}
}
