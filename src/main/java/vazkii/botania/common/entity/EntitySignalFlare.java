/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 7:10:32 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;

public class EntitySignalFlare extends Entity {

	private static final String COLOR_TAG = "color";

	public EntitySignalFlare(World par1World) {
		super(par1World);
		setSize(0F, 0F);
		dataWatcher.addObject(30, 0);
		dataWatcher.setObjectWatched(30);
	}

	@Override
	protected void entityInit() {
		// NO-OP
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(ticksExisted++ >= 100)
			setDead();

		if(!isDead) {
			if(ticksExisted % 10 == 0)
				playSound("creeper.primed", 1F, 1F);

			int color = getColor();
			if(color < 16 && color >= 0) {
				float[] colorArray = EntitySheep.fleeceColorTable[color];

				Botania.proxy.setWispFXDistanceLimit(false);
				for(int i = 0; i < 3; i++)
					Botania.proxy.wispFX(worldObj, posX, posY, posZ + 0.5, colorArray[0], colorArray[1], colorArray[2], (float) Math.random() * 5 + 1F, (float) (Math.random() - 0.5F), 10F * (float) Math.sqrt(256F / (256F - (float) posY)), (float) (Math.random() - 0.5F));

				for(int i = 0; i < 4; i++)
					Botania.proxy.wispFX(worldObj, posX + 0.5, 256, posZ + 0.5, colorArray[0], colorArray[1], colorArray[2], (float) Math.random() * 15 + 8F, (float) (Math.random() - 0.5F) * 8F, 0F, (float) (Math.random() - 0.5F) * 8F);
				Botania.proxy.setWispFXDistanceLimit(true);
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		setColor(nbttagcompound.getInteger(COLOR_TAG));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger(COLOR_TAG, getColor());
	}

	public void setColor(int color) {
		dataWatcher.updateObject(30, color);
	}

	public int getColor() {
		return dataWatcher.getWatchableObjectInt(30);
	}

}
