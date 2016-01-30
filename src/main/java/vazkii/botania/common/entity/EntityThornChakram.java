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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

public class EntityThornChakram extends EntityThrowable {

	private static final int MAX_BOUNCES = 16;
	boolean bounced = false;
	private ItemStack stack;

	public EntityThornChakram(World world) {
		super(world);
	}

	public EntityThornChakram(World world, EntityLivingBase e, ItemStack stack) {
		super(world, e);
		this.stack = stack.copy();
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(30, 0);
		dataWatcher.addObject(31, (byte) 0);

		dataWatcher.setObjectWatched(30);
		dataWatcher.setObjectWatched(31);
	}

	@Override
	public void onUpdate() {
		double mx = motionX;
		double my = motionY;
		double mz = motionZ;

		super.onUpdate();

		if(isFire()) {
			double r = 0.1;
			double m = 0.1;
			for(int i = 0; i < 3; i++)
				worldObj.spawnParticle(EnumParticleTypes.FLAME, posX + r * (Math.random() - 0.5), posY + r * (Math.random() - 0.5), posZ + r * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5));
		}

		int bounces = getTimesBounced();
		if(bounces >= MAX_BOUNCES || ticksExisted > 60) {
			EntityLivingBase thrower = getThrower();
			noClip = true;
			if(thrower == null)
				dropAndKill();
			else {
				Vector3 motion = Vector3.fromEntityCenter(thrower).sub(Vector3.fromEntityCenter(this)).normalize();
				motionX = motion.x;
				motionY = motion.y;
				motionZ = motion.z;
				if(!worldObj.isRemote && MathHelper.pointDistanceSpace(posX, posY, posZ, thrower.posX, thrower.posY, thrower.posZ) < 1)
					if(!(thrower instanceof EntityPlayer && (((EntityPlayer) thrower).capabilities.isCreativeMode || ((EntityPlayer) thrower).inventory.addItemStackToInventory(getItemStack()))))
						dropAndKill();
					else
						setDead();
			}
		} else {
			if(!bounced) {
				motionX = mx;
				motionY = my;
				motionZ = mz;
			}
			bounced = false;
		}
	}

	private void dropAndKill() {
		if(!worldObj.isRemote) {
			ItemStack stack = getItemStack();
			EntityItem item = new EntityItem(worldObj, posX, posY, posZ, stack);
			worldObj.spawnEntityInWorld(item);
			setDead();
		}
	}

	private ItemStack getItemStack() {
		return stack != null ? stack.copy() : new ItemStack(ModItems.thornChakram, 1, isFire() ? 1 : 0);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if(noClip)
			return;

		if (pos.getBlockPos() != null) {
			Block block = worldObj.getBlockState(pos.getBlockPos()).getBlock();
			worldObj.getTileEntity(pos.getBlockPos());
			if(block instanceof BlockBush || block instanceof BlockLeaves)
				return;
		}

		boolean fire = isFire();
		EntityLivingBase thrower = getThrower();
		if(!worldObj.isRemote && pos.entityHit != null && pos.entityHit instanceof EntityLivingBase && pos.entityHit != thrower) {
			pos.entityHit.attackEntityFrom(thrower != null ? thrower instanceof EntityPlayer ? DamageSource.causePlayerDamage((EntityPlayer) thrower) : DamageSource.causeMobDamage(thrower) : DamageSource.generic, 12);
			if(fire)
				pos.entityHit.setFire(5);
			else if(worldObj.rand.nextInt(3) == 0)
				((EntityLivingBase) pos.entityHit).addPotionEffect(new PotionEffect(Potion.poison.id, 60, 0));
		} else if (pos.getBlockPos() != null) {
			int bounces = getTimesBounced();
			if(bounces < MAX_BOUNCES) {
				Vector3 currentMovementVec = new Vector3(motionX, motionY, motionZ);
				EnumFacing dir = pos.sideHit;
				Vector3 normalVector = new Vector3(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ()).normalize();
				Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

				motionX = movementVec.x;
				motionY = movementVec.y;
				motionZ = movementVec.z;
				bounced = true;
			}
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	public int getTimesBounced() {
		return dataWatcher.getWatchableObjectInt(30);
	}

	public void setTimesBounced(int times) {
		dataWatcher.updateObject(30, times);
	}

	public boolean isFire() {
		return dataWatcher.getWatchableObjectByte(31) != 0;
	}

	public void setFire(boolean fire) {
		dataWatcher.updateObject(31, (byte) (fire ? 1 : 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(stack != null) {
			compound.setTag("fly_stack", stack.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("fly_stack")) {
			stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("fly_stack"));
		}
	}

}
