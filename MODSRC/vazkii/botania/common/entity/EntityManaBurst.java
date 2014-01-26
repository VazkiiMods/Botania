/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 5:09:12 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.awt.Color;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpreader;

public class EntityManaBurst extends EntityThrowable {

	boolean fake;
	int color;
	
	public EntityManaBurst(World par1World, TileSpreader spreader, boolean fake, int color) {
		super(par1World);
		this.fake = fake;
		this.color = color;
		setSize(0F, 0F);
		setLocationAndAngles(spreader.xCoord, spreader.yCoord, spreader.zCoord, 0, 0);
		rotationYaw = spreader.rotationX;
		rotationPitch = spreader.rotationY;
		float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f);
	}
	
	public void particles() {
		Color color = new Color(this.color);
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;

		if(fake)
			Botania.proxy.sparkleFX(worldObj, posX, posY, posZ, r, g, b, 1F, 0, true);
		else Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 1F);
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		// TODO
		if(movingobjectposition.entityHit == null)
			setDead();
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
