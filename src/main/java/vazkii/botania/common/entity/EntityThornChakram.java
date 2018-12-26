/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 6:47:35 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class EntityThornChakram extends EntityThrowable {

	private static final DataParameter<Integer> BOUNCES = EntityDataManager.createKey(EntityThornChakram.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> FLARE = EntityDataManager.createKey(EntityThornChakram.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> RETURN_TO = EntityDataManager.createKey(EntityThornChakram.class, DataSerializers.VARINT);
	private static final int MAX_BOUNCES = 16;
	private boolean bounced = false;
	private ItemStack stack = ItemStack.EMPTY;

	public EntityThornChakram(World world) {
		super(world);
	}

	public EntityThornChakram(World world, EntityLivingBase e, ItemStack stack) {
		super(world, e);
		this.stack = stack.copy();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(BOUNCES, 0);
		dataManager.register(FLARE, false);
		dataManager.register(RETURN_TO, -1);
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	@Override
	public void onUpdate() {
		// Standard motion
		double mx = motionX;
		double my = motionY;
		double mz = motionZ;

		super.onUpdate();

		if(!bounced) {
			// Reset the drag applied by super
			motionX = mx;
			motionY = my;
			motionZ = mz;
		}

		bounced = false;

		// Returning motion
		if(isReturning()) {
			Entity thrower = getThrower();
			if(thrower != null) {
				Vector3 motion = Vector3.fromEntityCenter(thrower).subtract(Vector3.fromEntityCenter(this)).normalize();
				motionX = motion.x;
				motionY = motion.y;
				motionZ = motion.z;
			}
		}

		// Client FX
		if(world.isRemote && isFire()) {
			double r = 0.1;
			double m = 0.1;
			for(int i = 0; i < 3; i++)
				world.spawnParticle(EnumParticleTypes.FLAME, posX + r * (Math.random() - 0.5), posY + r * (Math.random() - 0.5), posZ + r * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5));
		}

		// Server state control
		if(!world.isRemote && (getTimesBounced() >= MAX_BOUNCES || ticksExisted > 60)) {
			EntityLivingBase thrower = getThrower();
			if(thrower == null) {
				dropAndKill();
			} else {
				setEntityToReturnTo(thrower.getEntityId());
				if(getDistanceSq(thrower) < 2)
					dropAndKill();
			}
		}
	}

	private void dropAndKill() {
		ItemStack stack = getItemStack();
		EntityItem item = new EntityItem(world, posX, posY, posZ, stack);
		world.spawnEntity(item);
		setDead();
	}

	private ItemStack getItemStack() {
		return !stack.isEmpty() ? stack.copy() : new ItemStack(ModItems.thornChakram, 1, isFire() ? 1 : 0);
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		if(isReturning())
			return;

		switch (pos.typeOfHit) {
		case BLOCK: {
			Block block = world.getBlockState(pos.getBlockPos()).getBlock();
			if(block instanceof BlockBush || block instanceof BlockLeaves)
				return;

			int bounces = getTimesBounced();
			if(bounces < MAX_BOUNCES) {
				Vector3 currentMovementVec = new Vector3(motionX, motionY, motionZ);
				EnumFacing dir = pos.sideHit;
				Vector3 normalVector = new Vector3(dir.getXOffset(), dir.getYOffset(), dir.getZOffset()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				motionX = movementVec.x;
				motionY = movementVec.y;
				motionZ = movementVec.z;
				bounced = true;

				if(!world.isRemote)
					setTimesBounced(getTimesBounced() + 1);
			}

			break;
		}
		case ENTITY: {
			if(!world.isRemote && pos.entityHit != null && pos.entityHit instanceof EntityLivingBase && pos.entityHit != getThrower()) {
				EntityLivingBase thrower = getThrower();
				pos.entityHit.attackEntityFrom(thrower != null ? thrower instanceof EntityPlayer ? DamageSource.causeThrownDamage(this, thrower) : DamageSource.causeMobDamage(thrower) : DamageSource.GENERIC, 12);
				if(isFire())
					pos.entityHit.setFire(5);
				else if(world.rand.nextInt(3) == 0)
					((EntityLivingBase) pos.entityHit).addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 0));
			}

			break;
		}
		default: break;
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	private int getTimesBounced() {
		return dataManager.get(BOUNCES);
	}

	private void setTimesBounced(int times) {
		dataManager.set(BOUNCES, times);
	}

	public boolean isFire() {
		return dataManager.get(FLARE);
	}

	public void setFire(boolean fire) {
		dataManager.set(FLARE, fire);
	}

	private boolean isReturning() {
		return getEntityToReturnTo() > -1;
	}

	private int getEntityToReturnTo() {
		return dataManager.get(RETURN_TO);
	}

	private void setEntityToReturnTo(int entityID) {
		dataManager.set(RETURN_TO, entityID);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(!stack.isEmpty()) {
			compound.setTag("fly_stack", stack.writeToNBT(new NBTTagCompound()));
		}
		compound.setBoolean("flare", isFire());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("fly_stack")) {
			stack = new ItemStack(compound.getCompoundTag("fly_stack"));
		}
		setFire(compound.getBoolean("flare"));
	}

}
