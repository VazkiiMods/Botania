/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 21, 2014, 7:51:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileAltar extends TileMod {

	public static final String TAG_HAS_WATER = "hasWater";
	
	public boolean hasWater = false;
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_HAS_WATER, hasWater);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		hasWater = cmp.getBoolean(TAG_HAS_WATER);
	}
	
}
