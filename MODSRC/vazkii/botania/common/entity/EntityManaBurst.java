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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpreader;

public class EntityManaBurst extends EntityThrowable {

	private static final String TAG_COLOR = "color";
	
	boolean fake = false;
	
	public EntityManaBurst(World world) {
		super(world);
		setSize(0F, 0F);
        dataWatcher.addObject(25, 0);
        dataWatcher.setObjectWatched(25);
	}
	
	public EntityManaBurst(World par1World, TileSpreader spreader, boolean fake, int color) {
		this(par1World);
		this.fake = fake;
		
		setLocationAndAngles(spreader.xCoord + 0.5, spreader.yCoord + 0.5, spreader.zCoord + 0.5, 0, 0);
		rotationYaw = -(spreader.rotationX + 90F);
		rotationPitch = spreader.rotationY;
		
		float f = 0.4F;
        this.motionX = -(double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f) / 2D;
        this.motionZ = -(double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f) / 2D;
        this.motionY = -(double)(-MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f) / 2D;
	
        posX += motionX * 5;
        posY += motionY * 5;
        posZ += motionZ * 5;
        setColor(color);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		particles();
		
		if(ticksExisted > 100)
			setDead();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_COLOR, getColor());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setColor(par1nbtTagCompound.getInteger(TAG_COLOR));
	}
	
	public void particles() {
		if(!worldObj.isRemote)
			return;
		
		Color color = new Color(getColor());
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;

		if(fake)
			Botania.proxy.sparkleFX(worldObj, posX, posY, posZ, r, g, b, 1F, 0, true);
		else Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.5F * (float) (100 - ticksExisted) / 100F, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);
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

	int getColor() {
		return dataWatcher.getWatchableObjectInt(25);
	}
	
	void setColor(int color) {
		dataWatcher.updateObject(25, color);
	}
	
}
