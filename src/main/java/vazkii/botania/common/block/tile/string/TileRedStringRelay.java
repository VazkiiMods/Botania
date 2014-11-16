/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 16, 2014, 10:52:21 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;

public class TileRedStringRelay extends TileRedString {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		if(x == xCoord && y == yCoord + 1 && z == zCoord)
			return false;
		
		Block block = worldObj.getBlock(x, y, z);
		return block instanceof BlockFlower;
	}

}
