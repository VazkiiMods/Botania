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
import net.minecraft.util.ITickable;
import vazkii.botania.common.Botania;

import javax.annotation.Nonnull;

public class TileBifrost extends TileMod implements ITickable {

	private static final String TAG_TICKS = "ticks";

	public int ticks = 0;

	@Override
	public void update() {
		if(!world.isRemote) {
			if(ticks <= 0) {
				world.setBlockToAir(pos);
			} else ticks--;
		}
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
