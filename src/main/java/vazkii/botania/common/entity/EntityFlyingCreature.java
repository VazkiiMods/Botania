/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityFlyingCreature extends EntityAmbientCreature {

	public EntityFlyingCreature(World world) {
		super(world);
	}

	@Override
	public void fall(float dist, float damageMult) {
		// NO-OP
	}

	@Override
	protected void updateFallState(double y, boolean onGround, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
		// NO-OP
	}

	@Override
	public void moveEntityWithHeading(float par1, float par2) {
		if(isInWater()) {
			moveRelative(par1, par2, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
		} else if(isNotColliding()) {
			moveRelative(par1, par2, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		} else {
			float f2 = 0.91F;

			if (onGround)  {
				Block block = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock();
				f2 = block.slipperiness * 0.91F;
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			moveRelative(par1, par2, onGround ? 0.1F * f3 : 0.02F);
			f2 = 0.91F;

			if (onGround) {
				Block block = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock();
				f2 = block.slipperiness * 0.91F;
			}

			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= f2;
			motionZ *= f2;
		}

		prevLimbSwingAmount = limbSwingAmount;
		double d0 = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if(f4 > 1.0F)
			f4 = 1.0F;

		limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}

	@Override
	public boolean isOnLadder() {
		return false;
	}
}