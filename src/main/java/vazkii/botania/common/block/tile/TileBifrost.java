/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 20, 2014, 8:35:27 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.Botania;

public class TileBifrost extends TileMod {

	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(ticks <= 0) {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			} else ticks--;
		} else if(Math.random() < 0.1)
			Botania.proxy.sparkleFX(worldObj, xCoord + Math.random(), yCoord + Math.random(), zCoord + Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.45F + 0.2F * (float) Math.random(), 6);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_TICKS, ticks);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		ticks = par1nbtTagCompound.getInteger(TAG_TICKS);
	}

}
