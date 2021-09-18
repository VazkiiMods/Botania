/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.*;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;

public class EntityManaBurst extends ThrowableProjectile implements IManaBurst {
	private static final String TAG_TICKS_EXISTED = "ticksExisted";
	private static final String TAG_COLOR = "color";
	private static final String TAG_MANA = "mana";
	private static final String TAG_STARTING_MANA = "startingMana";
	private static final String TAG_MIN_MANA_LOSS = "minManaLoss";
	private static final String TAG_TICK_MANA_LOSS = "manaLossTick";
	private static final String TAG_SPREADER_X = "spreaderX";
	private static final String TAG_SPREADER_Y = "spreaderY";
	private static final String TAG_SPREADER_Z = "spreaderZ";
	private static final String TAG_GRAVITY = "gravity";
	private static final String TAG_LENS_STACK = "lensStack";
	private static final String TAG_HAS_SHOOTER = "hasShooter";
	private static final String TAG_SHOOTER = "shooterUUID";
	private static final String TAG_LAST_COLLISION_X = "lastCollisionX";
	private static final String TAG_LAST_COLLISION_Y = "lastCollisionY";
	private static final String TAG_LAST_COLLISION_Z = "lastCollisionZ";
	private static final String TAG_WARPED = "warped";
	private static final String TAG_ORBIT_TIME = "orbitTime";
	private static final String TAG_TRIPPED = "tripped";
	private static final String TAG_MAGNETIZE_POS = "magnetizePos";

	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MANA = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> START_MANA = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MIN_MANA_LOSS = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> MANA_LOSS_PER_TICK = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<BlockPos> SOURCE_COORDS = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<ItemStack> SOURCE_LENS = SynchedEntityData.defineId(EntityManaBurst.class, EntityDataSerializers.ITEM_STACK);

	private float accumulatedManaLoss = 0;
	private boolean fake = false;
	private final Set<BlockPos> alreadyCollidedAt = new HashSet<>();
	private boolean fullManaLastTick = true;
	private UUID shooterIdentity = null;
	private int _ticksExisted = 0;
	private boolean scanBeam = false;
	private BlockPos lastCollision;
	private boolean warped = false;
	private int orbitTime = 0;
	private boolean tripped = false;
	private BlockPos magnetizePos = null;

	public final List<PositionProperties> propsList = new ArrayList<>();

	public EntityManaBurst(EntityType<EntityManaBurst> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(COLOR, 0);
		entityData.define(MANA, 0);
		entityData.define(START_MANA, 0);
		entityData.define(MIN_MANA_LOSS, 0);
		entityData.define(MANA_LOSS_PER_TICK, 0F);
		entityData.define(GRAVITY, 0F);
		entityData.define(SOURCE_COORDS, BlockPos.ZERO);
		entityData.define(SOURCE_LENS, ItemStack.EMPTY);
	}

	public EntityManaBurst(IManaSpreader spreader, boolean fake) {
		this(ModEntities.MANA_BURST, ((BlockEntity) spreader).getLevel());

		BlockEntity tile = spreader.tileEntity();

		this.fake = fake;

		setBurstSourceCoords(tile.getBlockPos());
		moveTo(tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5, 0, 0);
		setYRot(-(spreader.getRotationX() + 90F));
		setXRot(spreader.getRotationY());

		float f = 0.4F;
		double mx = Mth.sin(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(Mth.cos(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f) / 2D;
		double my = Mth.sin(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		setDeltaMovement(mx, my, mz);
	}

	public EntityManaBurst(Player player) {
		super(ModEntities.MANA_BURST, player, player.level);

		setBurstSourceCoords(new BlockPos(0, Integer.MIN_VALUE, 0));
		setRot(player.getYRot() + 180, -player.getXRot());

		float f = 0.4F;
		double mx = Mth.sin(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(Mth.cos(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f) / 2D;
		double my = Mth.sin(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		setDeltaMovement(mx, my, mz);
	}

	@Override
	public void tick() {
		setTicksExisted(getTicksExisted() + 1);
		super.tick();

		if (!fake && isAlive() && !scanBeam) {
			ping();
		}

		ILensEffect lens = getLensInstance();
		if (lens != null) {
			lens.updateBurst(this, getSourceLens());
		}

		int mana = getMana();
		if (getTicksExisted() >= getMinManaLoss()) {
			accumulatedManaLoss += getManaLossPerTick();
			int loss = (int) accumulatedManaLoss;
			setMana(mana - loss);
			accumulatedManaLoss -= loss;

			if (getMana() <= 0) {
				discard();
			}
		}

		particles();

		fullManaLastTick = getMana() == getStartingMana();

		if (scanBeam) {
			PositionProperties props = new PositionProperties(this);
			if (propsList.isEmpty()) {
				propsList.add(props);
			} else {
				PositionProperties lastProps = propsList.get(propsList.size() - 1);
				if (!props.coordsEqual(lastProps)) {
					propsList.add(props);
				}
			}
		}
	}

	@Override
	public boolean updateFluidHeightAndDoFluidPushing(Tag<Fluid> fluid, double mag) {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	private BlockEntity collidedTile = null;
	private boolean noParticles = false;

	public BlockEntity getCollidedTile(boolean noParticles) {
		this.noParticles = noParticles;

		int iterations = 0;
		while (isAlive() && iterations < ConfigHandler.COMMON.spreaderTraceTime.getValue()) {
			tick();
			iterations++;
		}

		if (fake) {
			incrementFakeParticleTick();
		}

		return collidedTile;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt(TAG_TICKS_EXISTED, getTicksExisted());
		tag.putInt(TAG_COLOR, getColor());
		tag.putInt(TAG_MANA, getMana());
		tag.putInt(TAG_STARTING_MANA, getStartingMana());
		tag.putInt(TAG_MIN_MANA_LOSS, getMinManaLoss());
		tag.putFloat(TAG_TICK_MANA_LOSS, getManaLossPerTick());
		tag.putFloat(TAG_GRAVITY, getBurstGravity());

		ItemStack stack = getSourceLens();
		CompoundTag lensCmp = new CompoundTag();
		if (!stack.isEmpty()) {
			lensCmp = stack.save(lensCmp);
		}
		tag.put(TAG_LENS_STACK, lensCmp);

		BlockPos coords = getBurstSourceBlockPos();
		tag.putInt(TAG_SPREADER_X, coords.getX());
		tag.putInt(TAG_SPREADER_Y, coords.getY());
		tag.putInt(TAG_SPREADER_Z, coords.getZ());

		if (lastCollision != null) {
			tag.putInt(TAG_LAST_COLLISION_X, lastCollision.getX());
			tag.putInt(TAG_LAST_COLLISION_Y, lastCollision.getY());
			tag.putInt(TAG_LAST_COLLISION_Z, lastCollision.getZ());
		}

		UUID identity = getShooterUUID();
		boolean hasShooter = identity != null;
		tag.putBoolean(TAG_HAS_SHOOTER, hasShooter);
		if (hasShooter) {
			tag.putUUID(TAG_SHOOTER, identity);
		}
		tag.putBoolean(TAG_WARPED, warped);
		tag.putInt(TAG_ORBIT_TIME, orbitTime);
		tag.putBoolean(TAG_TRIPPED, tripped);
		if (magnetizePos != null) {
			tag.put(TAG_MAGNETIZE_POS, BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, magnetizePos).get().orThrow());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag cmp) {
		super.readAdditionalSaveData(cmp);
		setTicksExisted(cmp.getInt(TAG_TICKS_EXISTED));
		setColor(cmp.getInt(TAG_COLOR));
		setMana(cmp.getInt(TAG_MANA));
		setStartingMana(cmp.getInt(TAG_STARTING_MANA));
		setMinManaLoss(cmp.getInt(TAG_MIN_MANA_LOSS));
		setManaLossPerTick(cmp.getFloat(TAG_TICK_MANA_LOSS));
		setGravity(cmp.getFloat(TAG_GRAVITY));

		CompoundTag lensCmp = cmp.getCompound(TAG_LENS_STACK);
		ItemStack stack = ItemStack.of(lensCmp);
		if (!stack.isEmpty()) {
			setSourceLens(stack);
		} else {
			setSourceLens(ItemStack.EMPTY);
		}

		int x = cmp.getInt(TAG_SPREADER_X);
		int y = cmp.getInt(TAG_SPREADER_Y);
		int z = cmp.getInt(TAG_SPREADER_Z);

		setBurstSourceCoords(new BlockPos(x, y, z));

		if (cmp.contains(TAG_LAST_COLLISION_X)) {
			x = cmp.getInt(TAG_LAST_COLLISION_X);
			y = cmp.getInt(TAG_LAST_COLLISION_Y);
			z = cmp.getInt(TAG_LAST_COLLISION_Z);
			lastCollision = new BlockPos(x, y, z);
		}

		// Reread Motion because Entity.load clamps it to +/-10
		ListTag motion = cmp.getList("Motion", 6);
		setDeltaMovement(motion.getDouble(0), motion.getDouble(1), motion.getDouble(2));

		boolean hasShooter = cmp.getBoolean(TAG_HAS_SHOOTER);
		if (hasShooter) {
			UUID serializedUuid = cmp.getUUID(TAG_SHOOTER);
			UUID identity = getShooterUUID();
			if (!serializedUuid.equals(identity)) {
				setShooterUUID(serializedUuid);
			}
		}
		warped = cmp.getBoolean(TAG_WARPED);
		orbitTime = cmp.getInt(TAG_ORBIT_TIME);
		tripped = cmp.getBoolean(TAG_TRIPPED);
		if (cmp.contains(TAG_MAGNETIZE_POS)) {
			magnetizePos = BlockPos.CODEC.parse(NbtOps.INSTANCE, cmp.get(TAG_MAGNETIZE_POS)).get().orThrow();
		} else {
			magnetizePos = null;
		}
	}

	public void particles() {
		if (!isAlive() || !level.isClientSide) {
			return;
		}

		ILensEffect lens = getLensInstance();
		if (lens != null && !lens.doParticles(this, getSourceLens())) {
			return;
		}

		int color = getColor();
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		float osize = getParticleSize();
		float size = osize;

		if (fake) {
			if (getMana() == getStartingMana()) {
				size = 2F;
			} else if (fullManaLastTick) {
				size = 4F;
			}

			if (!noParticles && shouldDoFakeParticles()) {
				SparkleParticleData data = SparkleParticleData.fake(0.4F * size, r, g, b, 1);
				Botania.proxy.addParticleForce(level, data, getX(), getY(), getZ(), 0, 0, 0);
			}
		} else {
			boolean depth = !Botania.proxy.isClientPlayerWearingMonocle();

			if (ConfigHandler.CLIENT.subtlePowerSystem.getValue()) {
				WispParticleData data = WispParticleData.wisp(0.1F * size, r, g, b, depth);
				Botania.proxy.addParticleForceNear(level, data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
			} else {
				float or = r;
				float og = g;
				float ob = b;

				double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

				double iterX = getX();
				double iterY = getY();
				double iterZ = getZ();

				Vec3 currentPos = position();
				Vec3 oldPos = new Vec3(xo, yo, zo);
				Vec3 diffVec = oldPos.subtract(currentPos);
				Vec3 diffVecNorm = diffVec.normalize();

				double distance = 0.095;

				do {
					if (luminance < 0.1) {
						r = or + (float) Math.random() * 0.125F;
						g = og + (float) Math.random() * 0.125F;
						b = ob + (float) Math.random() * 0.125F;
					}
					size = osize + ((float) Math.random() - 0.5F) * 0.065F + (float) Math.sin(new Random(uuid.getMostSignificantBits()).nextInt(9001)) * 0.4F;
					WispParticleData data = WispParticleData.wisp(0.2F * size, r, g, b, depth);
					Botania.proxy.addParticleForceNear(level, data, iterX, iterY, iterZ,
							(float) -getDeltaMovement().x() * 0.01F,
							(float) -getDeltaMovement().y() * 0.01F,
							(float) -getDeltaMovement().z() * 0.01F);

					iterX += diffVecNorm.x * distance;
					iterY += diffVecNorm.y * distance;
					iterZ += diffVecNorm.z * distance;

					currentPos = new Vec3(iterX, iterY, iterZ);
					diffVec = oldPos.subtract(currentPos);
					if (getOrbitTime() > 0) {
						break;
					}
				} while (Math.abs(diffVec.length()) > distance);

				WispParticleData data = WispParticleData.wisp(0.1F * size, or, og, ob, depth);
				level.addParticle(data, iterX, iterY, iterZ, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);
			}
		}
	}

	public float getParticleSize() {
		return (float) getMana() / (float) getStartingMana();
	}

	@Override
	protected void onHit(@Nonnull HitResult rtr) {
		BlockPos collidePos = null;
		boolean dead = false;

		if (rtr.getType() == HitResult.Type.BLOCK) {
			collidePos = ((BlockHitResult) rtr).getBlockPos();
			if (collidePos.equals(lastCollision)) {
				return;
			}
			lastCollision = collidePos.immutable();
			BlockEntity tile = level.getBlockEntity(collidePos);
			BlockState state = level.getBlockState(collidePos);
			Block block = state.getBlock();

			if (block instanceof IManaCollisionGhost ghost
					&& ghost.isGhost(state, level, collidePos)
					&& !(block instanceof IManaTrigger)
					|| block instanceof BushBlock
					|| block instanceof LeavesBlock) {
				return;
			}

			BlockPos sourcePos = getBurstSourceBlockPos();
			if (tile != null && !tile.getBlockPos().equals(sourcePos)) {
				collidedTile = tile;
			}

			if (tile == null || !tile.getBlockPos().equals(sourcePos)) {
				if (!fake && !noParticles && !level.isClientSide
						&& tile instanceof IManaReceiver receiver
						&& receiver.canReceiveManaFromBursts()) {
					onReceiverImpact(receiver);
				}

				if (block instanceof IManaTrigger trigger) {
					trigger.onBurstCollision(this, level, collidePos);
				}

				if (block instanceof IManaCollisionGhost) {
					return;
				} else {
					dead = true;
				}
			}
		}

		ILensEffect lens = getLensInstance();
		if (lens != null) {
			dead = lens.collideBurst(this, rtr, collidedTile instanceof IManaReceiver receiver
					&& receiver.canReceiveManaFromBursts(), dead, getSourceLens());
		}

		if (collidePos != null && !hasAlreadyCollidedAt(collidePos)) {
			alreadyCollidedAt.add(collidePos);
		}

		if (dead && isAlive()) {
			if (!fake && level.isClientSide) {
				int color = getColor();
				float r = (color >> 16 & 0xFF) / 255F;
				float g = (color >> 8 & 0xFF) / 255F;
				float b = (color & 0xFF) / 255F;

				int mana = getMana();
				int maxMana = getStartingMana();
				float size = (float) mana / (float) maxMana;

				if (!ConfigHandler.CLIENT.subtlePowerSystem.getValue()) {
					for (int i = 0; i < 4; i++) {
						WispParticleData data = WispParticleData.wisp(0.15F * size, r, g, b);
						level.addParticle(data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
					}
				}
				SparkleParticleData data = SparkleParticleData.sparkle((float) 4, r, g, b, 2);
				level.addParticle(data, getX(), getY(), getZ(), 0, 0, 0);
			}

			discard();
		}
	}

	private void onReceiverImpact(IManaReceiver tile) {
		if (hasWarped()) {
			return;
		}

		ILensEffect lens = getLensInstance();
		int mana = getMana();

		if (lens != null) {
			ItemStack stack = getSourceLens();
			mana = lens.getManaToTransfer(this, stack, tile);
		}

		if (tile instanceof IManaCollector collector) {
			mana *= collector.getManaYieldMultiplier(this);
		}

		tile.receiveMana(mana);

		if (tile instanceof IThrottledPacket throttledPacket) {
			throttledPacket.markDispatchable();
		} else {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile.tileEntity());
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);

		if (!fake) {
			BlockEntity tile = getShooter();
			if (tile instanceof IManaSpreader spreader) {
				spreader.setCanShoot(true);
			}
		} else {
			setDeathTicksForFakeParticle();
		}
	}

	private BlockEntity getShooter() {
		return level.getBlockEntity(getBurstSourceBlockPos());
	}

	@Override
	public float getGravity() {
		return getBurstGravity();
	}

	@Override
	public boolean isFake() {
		return fake;
	}

	@Override
	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public void setScanBeam() {
		scanBeam = true;
	}

	@Override
	public int getColor() {
		return entityData.get(COLOR);
	}

	@Override
	public void setColor(int color) {
		entityData.set(COLOR, color);
	}

	@Override
	public int getMana() {
		return entityData.get(MANA);
	}

	@Override
	public void setMana(int mana) {
		entityData.set(MANA, mana);
	}

	@Override
	public int getStartingMana() {
		return entityData.get(START_MANA);
	}

	@Override
	public void setStartingMana(int mana) {
		entityData.set(START_MANA, mana);
	}

	@Override
	public int getMinManaLoss() {
		return entityData.get(MIN_MANA_LOSS);
	}

	@Override
	public void setMinManaLoss(int minManaLoss) {
		entityData.set(MIN_MANA_LOSS, minManaLoss);
	}

	@Override
	public float getManaLossPerTick() {
		return entityData.get(MANA_LOSS_PER_TICK);
	}

	@Override
	public void setManaLossPerTick(float mana) {
		entityData.set(MANA_LOSS_PER_TICK, mana);
	}

	@Override
	public float getBurstGravity() {
		return entityData.get(GRAVITY);
	}

	@Override
	public void setGravity(float gravity) {
		entityData.set(GRAVITY, gravity);
	}

	@Override
	public BlockPos getBurstSourceBlockPos() {
		return entityData.get(SOURCE_COORDS);
	}

	@Override
	public void setBurstSourceCoords(BlockPos pos) {
		entityData.set(SOURCE_COORDS, pos);
	}

	@Override
	public ItemStack getSourceLens() {
		return entityData.get(SOURCE_LENS);
	}

	@Override
	public void setSourceLens(ItemStack lens) {
		entityData.set(SOURCE_LENS, lens);
	}

	@Override
	public int getTicksExisted() {
		return _ticksExisted;
	}

	public void setTicksExisted(int ticks) {
		_ticksExisted = ticks;
	}

	private ILensEffect getLensInstance() {
		ItemStack lens = getSourceLens();
		if (!lens.isEmpty() && lens.getItem() instanceof ILensEffect) {
			return (ILensEffect) lens.getItem();
		}

		return null;
	}

	@Override
	public boolean hasAlreadyCollidedAt(BlockPos pos) {
		return alreadyCollidedAt.contains(pos);
	}

	@Override
	public void setCollidedAt(BlockPos pos) {
		if (!hasAlreadyCollidedAt(pos)) {
			alreadyCollidedAt.add(pos.immutable());
		}
	}

	@Override
	public void setShooterUUID(UUID uuid) {
		shooterIdentity = uuid;
	}

	@Override
	public UUID getShooterUUID() {
		return shooterIdentity;
	}

	@Override
	public void ping() {
		BlockEntity tile = getShooter();
		if (tile instanceof IPingable pingable) {
			pingable.pingback(this, getShooterUUID());
		}
	}

	@Override
	public boolean hasWarped() {
		return warped;
	}

	@Override
	public void setWarped(boolean warped) {
		this.warped = warped;
	}

	@Override
	public int getOrbitTime() {
		return orbitTime;
	}

	@Override
	public void setOrbitTime(int time) {
		this.orbitTime = time;
	}

	@Override
	public boolean hasTripped() {
		return tripped;
	}

	@Override
	public void setTripped(boolean tripped) {
		this.tripped = tripped;
	}

	@Nullable
	@Override
	public BlockPos getMagnetizedPos() {
		return magnetizePos;
	}

	@Override
	public void setMagnetizePos(@Nullable BlockPos pos) {
		this.magnetizePos = pos;
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	protected boolean shouldDoFakeParticles() {
		if (ConfigHandler.CLIENT.staticWandBeam.getValue()) {
			return true;
		}

		BlockEntity tile = getShooter();
		return tile instanceof IManaSpreader spreader
				&& (getMana() != getStartingMana() && fullManaLastTick
						|| Math.abs(spreader.getBurstParticleTick() - getTicksExisted()) < 4);
	}

	private void incrementFakeParticleTick() {
		BlockEntity tile = getShooter();
		if (tile instanceof IManaSpreader spreader) {
			spreader.setBurstParticleTick(spreader.getBurstParticleTick() + 2);
			if (spreader.getLastBurstDeathTick() != -1 && spreader.getBurstParticleTick() > spreader.getLastBurstDeathTick()) {
				spreader.setBurstParticleTick(0);
			}
		}
	}

	private void setDeathTicksForFakeParticle() {
		BlockPos coords = getBurstSourceBlockPos();
		BlockEntity tile = level.getBlockEntity(coords);
		if (tile instanceof IManaSpreader spreader) {
			spreader.setLastBurstDeathTick(getTicksExisted());
		}
	}

	public static class PositionProperties {

		public final BlockPos coords;
		public final BlockState state;

		public boolean invalid = false;

		public PositionProperties(Entity entity) {
			int x = Mth.floor(entity.getX());
			int y = Mth.floor(entity.getY());
			int z = Mth.floor(entity.getZ());
			coords = new BlockPos(x, y, z);
			state = entity.level.getBlockState(coords);
		}

		public boolean coordsEqual(PositionProperties props) {
			return coords.equals(props.coords);
		}

		public boolean contentsEqual(Level world) {
			if (!world.hasChunkAt(coords)) {
				invalid = true;
				return false;
			}

			return world.getBlockState(coords) == state;
		}

		@Override
		public int hashCode() {
			return Objects.hash(coords, state);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof PositionProperties
					&& ((PositionProperties) o).state == state
					&& ((PositionProperties) o).coords.equals(coords);
		}
	}

}
