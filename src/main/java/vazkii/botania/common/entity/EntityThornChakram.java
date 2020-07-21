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
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

@EnvironmentInterface(value = EnvType.CLIENT, itf = FlyingItemEntity.class)
public class EntityThornChakram extends ThrownEntity implements FlyingItemEntity {
	private static final TrackedData<Integer> BOUNCES = DataTracker.registerData(EntityThornChakram.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> FLARE = DataTracker.registerData(EntityThornChakram.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> RETURN_TO = DataTracker.registerData(EntityThornChakram.class, TrackedDataHandlerRegistry.INTEGER);
	private static final int MAX_BOUNCES = 16;
	private boolean bounced = false;
	private ItemStack stack = ItemStack.EMPTY;

	public EntityThornChakram(EntityType<EntityThornChakram> type, World world) {
		super(type, world);
	}

	public EntityThornChakram(LivingEntity e, World world, ItemStack stack) {
		super(ModEntities.THORN_CHAKRAM, e, world);
		this.stack = stack.copy();
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(BOUNCES, 0);
		dataTracker.startTracking(FLARE, false);
		dataTracker.startTracking(RETURN_TO, -1);
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean isImmuneToExplosion() {
		return true;
	}

	@Override
	public void tick() {
		// Standard motion
		Vec3d old = getVelocity();

		super.tick();

		if (!bounced) {
			// Reset the drag applied by super
			setVelocity(old);
		}

		bounced = false;

		// Returning motion
		if (isReturning()) {
			Entity thrower = getOwner();
			if (thrower != null) {
				Vector3 motion = Vector3.fromEntityCenter(thrower).subtract(Vector3.fromEntityCenter(this)).normalize();
				setVelocity(motion.toVector3d());
			}
		}

		// Client FX
		if (world.isClient && isFire()) {
			double r = 0.1;
			double m = 0.1;
			for (int i = 0; i < 3; i++) {
				world.addParticle(ParticleTypes.FLAME, getX() + r * (Math.random() - 0.5), getY() + r * (Math.random() - 0.5), getZ() + r * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5));
			}
		}

		// Server state control
		if (!world.isClient && (getTimesBounced() >= MAX_BOUNCES || age > 60)) {
			Entity thrower = getOwner();
			if (thrower == null) {
				dropAndKill();
			} else {
				setEntityToReturnTo(thrower.getEntityId());
				if (squaredDistanceTo(thrower) < 2) {
					dropAndKill();
				}
			}
		}
	}

	private void dropAndKill() {
		ItemStack stack = getItemStack();
		ItemEntity item = new ItemEntity(world, getX(), getY(), getZ(), stack);
		world.spawnEntity(item);
		remove();
	}

	private ItemStack getItemStack() {
		return !stack.isEmpty()
				? stack.copy()
				: isFire() ? new ItemStack(ModItems.flareChakram) : new ItemStack(ModItems.thornChakram);
	}

	@Override
	protected void onCollision(@Nonnull HitResult pos) {
		if (isReturning()) {
			return;
		}

		switch (pos.getType()) {
		case BLOCK: {
			BlockHitResult rtr = (BlockHitResult) pos;
			Block block = world.getBlockState(rtr.getBlockPos()).getBlock();
			if (block instanceof PlantBlock || block instanceof LeavesBlock) {
				return;
			}

			int bounces = getTimesBounced();
			if (bounces < MAX_BOUNCES) {
				Vector3 currentMovementVec = new Vector3(getVelocity());
				Direction dir = rtr.getSide();
				Vector3 normalVector = new Vector3(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				setVelocity(movementVec.toVector3d());
				bounced = true;

				if (!world.isClient) {
					setTimesBounced(getTimesBounced() + 1);
				}
			}

			break;
		}
		case ENTITY: {
			EntityHitResult rtr = (EntityHitResult) pos;
			if (!world.isClient && rtr.getEntity() instanceof LivingEntity && rtr.getEntity() != getOwner()) {
				Entity thrower = getOwner();
				DamageSource src = DamageSource.GENERIC;
				if (thrower instanceof PlayerEntity) {
					src = DamageSource.thrownProjectile(this, thrower);
				} else if (thrower instanceof LivingEntity) {
					src = DamageSource.mob((LivingEntity) thrower);
				}
				rtr.getEntity().damage(src, 12);
				if (isFire()) {
					rtr.getEntity().setOnFireFor(5);
				} else if (world.random.nextInt(3) == 0) {
					((LivingEntity) rtr.getEntity()).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0));
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
		return dataTracker.get(BOUNCES);
	}

	private void setTimesBounced(int times) {
		dataTracker.set(BOUNCES, times);
	}

	public boolean isFire() {
		return dataTracker.get(FLARE);
	}

	public void setFire(boolean fire) {
		dataTracker.set(FLARE, fire);
	}

	private boolean isReturning() {
		return getEntityToReturnTo() > -1;
	}

	private int getEntityToReturnTo() {
		return dataTracker.get(RETURN_TO);
	}

	private void setEntityToReturnTo(int entityID) {
		dataTracker.set(RETURN_TO, entityID);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag compound) {
		super.writeCustomDataToTag(compound);
		if (!stack.isEmpty()) {
			compound.put("fly_stack", stack.toTag(new CompoundTag()));
		}
		compound.putBoolean("flare", isFire());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag compound) {
		super.readCustomDataFromTag(compound);
		if (compound.contains("fly_stack")) {
			stack = ItemStack.fromTag(compound.getCompound("fly_stack"));
		}
		setFire(compound.getBoolean("flare"));
	}

	@Nonnull
	@Override
	public ItemStack getStack() {
		return getItemStack();
	}
}
