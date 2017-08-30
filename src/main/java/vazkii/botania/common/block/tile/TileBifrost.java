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

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.Botania;

public class TileBifrost extends TileMod {

	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	@Override
	public void update() {
		if(!world.isRemote) {
			if(ticks <= 0) {
				world.setBlockToAir(pos);
			} else ticks--;
		} else if(Math.random() < 0.1)
			Botania.proxy.sparkleFX(pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.45F + 0.2F * (float) Math.random(), 6);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound ret = super.writeToNBT(par1nbtTagCompound);
		ret.setInteger(TAG_TICKS, ticks);
		return ret;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
		ticks = par1nbtTagCompound.getInteger(TAG_TICKS);
	}

}
