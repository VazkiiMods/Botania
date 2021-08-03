/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ItemSupplier.class)
public class EntityThornChakram extends ThrowableProjectile implements ItemSupplier {
	private static final EntityDataAccessor<Integer> BOUNCES = SynchedEntityData.defineId(EntityThornChakram.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FLARE = SynchedEntityData.defineId(EntityThornChakram.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> RETURN_TO = SynchedEntityData.defineId(EntityThornChakram.class, EntityDataSerializers.INT);
	private static final int MAX_BOUNCES = 16;
	private boolean bounced = false;
	private ItemStack stack = ItemStack.EMPTY;

	public EntityThornChakram(EntityType<EntityThornChakram> type, Level world) {
		super(type, world);
	}

	public EntityThornChakram(LivingEntity e, Level world, ItemStack stack) {
		super(ModEntities.THORN_CHAKRAM, e, world);
		this.stack = stack.copy();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(BOUNCES, 0);
		entityData.define(FLARE, false);
		entityData.define(RETURN_TO, -1);
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	public void tick() {
		// Standard motion
		Vec3 old = getDeltaMovement();

		super.tick();

		if (!bounced) {
			// Reset the drag applied by super
			setDeltaMovement(old);
		}

		bounced = false;

		// Returning motion
		if (isReturning()) {
			Entity thrower = getOwner();
			if (thrower != null) {
				Vector3 motion = Vector3.fromEntityCenter(thrower).subtract(Vector3.fromEntityCenter(this)).normalize();
				setDeltaMovement(motion.toVector3d());
			}
		}

		// Client FX
		if (level.isClientSide && isFire()) {
			double r = 0.1;
			double m = 0.1;
			for (int i = 0; i < 3; i++) {
				level.addParticle(ParticleTypes.FLAME, getX() + r * (Math.random() - 0.5), getY() + r * (Math.random() - 0.5), getZ() + r * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5));
			}
		}

		// Server state control
		if (!level.isClientSide && (getTimesBounced() >= MAX_BOUNCES || tickCount > 60)) {
			Entity thrower = getOwner();
			if (thrower == null) {
				dropAndKill();
			} else {
				setEntityToReturnTo(thrower.getId());
				if (distanceToSqr(thrower) < 2) {
					dropAndKill();
				}
			}
		}
	}

	private void dropAndKill() {
		ItemStack stack = getItemStack();
		ItemEntity item = new ItemEntity(level, getX(), getY(), getZ(), stack);
		level.addFreshEntity(item);
		remove();
	}

	private ItemStack getItemStack() {
		return !stack.isEmpty()
				? stack.copy()
				: isFire() ? new ItemStack(ModItems.flareChakram) : new ItemStack(ModItems.thornChakram);
	}

	@Override
	protected void onHit(@Nonnull HitResult pos) {
		if (isReturning()) {
			return;
		}

		switch (pos.getType()) {
		case BLOCK: {
			BlockHitResult rtr = (BlockHitResult) pos;
			Block block = level.getBlockState(rtr.getBlockPos()).getBlock();
			if (block instanceof BushBlock || block instanceof LeavesBlock) {
				return;
			}

			int bounces = getTimesBounced();
			if (bounces < MAX_BOUNCES) {
				Vector3 currentMovementVec = new Vector3(getDeltaMovement());
				Direction dir = rtr.getDirection();
				Vector3 normalVector = new Vector3(dir.getStepX(), dir.getStepY(), dir.getStepZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				setDeltaMovement(movementVec.toVector3d());
				bounced = true;

				if (!level.isClientSide) {
					setTimesBounced(getTimesBounced() + 1);
				}
			}

			break;
		}
		case ENTITY: {
			EntityHitResult rtr = (EntityHitResult) pos;
			if (!level.isClientSide && rtr.getEntity() instanceof LivingEntity && rtr.getEntity() != getOwner()) {
				Entity thrower = getOwner();
				DamageSource src = DamageSource.GENERIC;
				if (thrower instanceof Player) {
					src = DamageSource.thrown(this, thrower);
				} else if (thrower instanceof LivingEntity) {
					src = DamageSource.mobAttack((LivingEntity) thrower);
				}
				rtr.getEntity().hurt(src, 12);
				if (isFire()) {
					rtr.getEntity().setSecondsOnFire(5);
				} else if (level.random.nextInt(3) == 0) {
					((LivingEntity) rtr.getEntity()).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
				}
			}

			break;
		}
		default:
			break;
		}
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	private int getTimesBounced() {
		return entityData.get(BOUNCES);
	}

	private void setTimesBounced(int times) {
		entityData.set(BOUNCES, times);
	}

	public boolean isFire() {
		return entityData.get(FLARE);
	}

	public void setFire(boolean fire) {
		entityData.set(FLARE, fire);
	}

	private boolean isReturning() {
		return getEntityToReturnTo() > -1;
	}

	private int getEntityToReturnTo() {
		return entityData.get(RETURN_TO);
	}

	private void setEntityToReturnTo(int entityID) {
		entityData.set(RETURN_TO, entityID);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (!stack.isEmpty()) {
			compound.put("fly_stack", stack.save(new CompoundTag()));
		}
		compound.putBoolean("flare", isFire());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("fly_stack")) {
			stack = ItemStack.of(compound.getCompound("fly_stack"));
		}
		setFire(compound.getBoolean("flare"));
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return getItemStack();
	}
}
