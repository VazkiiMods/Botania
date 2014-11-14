/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 14, 2014, 5:26:39 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class TileRedStringContainer extends TileRedString {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		return tile != null && tile instanceof IInventory;
	}

}
