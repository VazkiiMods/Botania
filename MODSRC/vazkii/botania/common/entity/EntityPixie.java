/**
 * This class was created by <Adubbz>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.entity;

import vazkii.botania.common.Botania;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityPixie extends EntityFlyingCreature {

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public EntityPixie(World world) {
		super(world); 
		setSize(1.0F, 1.0F);
	}

	@Override
	protected void updateEntityActionState() {
		double d0 = waypointX - posX;
		double d1 = waypointY - posY;
		double d2 = waypointZ - posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if(d3 < 1.0D || d3 > 3600.0D) {
			waypointX = posX + (double)((rand.nextFloat() * 4.0F - 2.0F) * 2.0F);
			waypointY = posY + (double)((rand.nextFloat() * 4.0F - 2.0F) * 2.0F);
			waypointZ = posZ + (double)((rand.nextFloat() * 4.0F - 2.0F) * 2.0F);
		}

		if(courseChangeCooldown-- <= 0) {
			courseChangeCooldown += rand.nextInt(2) + 2;
			d3 = (double)MathHelper.sqrt_double(d3);

			if(isCourseTraversable(waypointX, waypointY, waypointZ, d3)) {
				motionX += d0 / d3 * 0.1D;
				motionY += d1 / d3 * 0.1D;
				motionZ += d2 / d3 * 0.1D;
			} else {
				waypointX = posX;
				waypointY = posY;
				waypointZ = posZ;
			}
		}

		renderYawOffset = rotationYaw = -((float)Math.atan2(motionX, motionZ)) * 180.0F / (float)Math.PI;
	}

	private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
		double d4 = (waypointX - posX) / par7;
		double d5 = (waypointY - posY) / par7;
		double d6 = (waypointZ - posZ) / par7;
		AxisAlignedBB axisalignedbb = boundingBox.copy();

		for (int i = 1; (double)i < par7; ++i) {
			axisalignedbb.offset(d4, d5, d6);

			if (!worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
				return false;
		}

		return true;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if(worldObj.isRemote)
			for(int i = 0; i < 4; i++)
				Botania.proxy.sparkleFX(worldObj, posX + (Math.random() - 0.5) * 0.25, posY + 0.5  + (Math.random() - 0.5) * 0.25, posZ + (Math.random() - 0.5) * 0.25, 1F, 0.25F, 0.9F, 0.1F + (float) Math.random() * 0.25F, 12);
	}
	
}