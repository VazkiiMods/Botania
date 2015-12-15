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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

public class EntityThornChakram extends EntityThrowable {

	private static final int MAX_BOUNCES = 16;
	boolean bounced = false;

	public EntityThornChakram(World world) {
		super(world);
	}

	public EntityThornChakram(World world, EntityLivingBase e) {
		super(world, e);
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
				worldObj.spawnParticle("flame", posX + r * (Math.random() - 0.5), posY + r * (Math.random() - 0.5), posZ + r * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5), m * (Math.random() - 0.5));
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
				if(MathHelper.pointDistanceSpace(posX, posY, posZ, thrower.posX, thrower.posY, thrower.posZ) < 1)
					if(!(thrower instanceof EntityPlayer && (((EntityPlayer) thrower).capabilities.isCreativeMode || ((EntityPlayer) thrower).inventory.addItemStackToInventory(getItemStack()))))
						dropAndKill();
					else if(!worldObj.isRemote)
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
		return new ItemStack(ModItems.thornChakram, 1, isFire() ? 1 : 0);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if(noClip)
			return;

		Block block = worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
		if(block instanceof BlockBush || block instanceof BlockLeaves)
			return;

		boolean fire = isFire();
		EntityLivingBase thrower = getThrower();
		if(pos.entityHit != null && pos.entityHit instanceof EntityLivingBase && pos.entityHit != thrower) {
			((EntityLivingBase) pos.entityHit).attackEntityFrom(thrower != null ? thrower instanceof EntityPlayer ? DamageSource.causePlayerDamage((EntityPlayer) thrower) : DamageSource.causeMobDamage(thrower) : DamageSource.generic, 12);
			if(fire)
				((EntityLivingBase) pos.entityHit).setFire(5);
			else if(worldObj.rand.nextInt(3) == 0)
				((EntityLivingBase) pos.entityHit).addPotionEffect(new PotionEffect(Potion.poison.id, 60, 0));
		} else {
			int bounces = getTimesBounced();
			if(bounces < MAX_BOUNCES) {
				Vector3 currentMovementVec = new Vector3(motionX, motionY, motionZ);
				ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
				Vector3 normalVector = new Vector3(dir.offsetX, dir.offsetY, dir.offsetZ).normalize();
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

}
