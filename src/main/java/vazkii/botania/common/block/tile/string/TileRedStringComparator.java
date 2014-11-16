/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 16, 2014, 10:22:01 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRedStringComparator extends TileRedString {

	int comparatorValue = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		ChunkCoordinates binding = getBinding();
		ForgeDirection dir = getOrientation();
		Block block = getBlockAtBinding();
		int origVal = comparatorValue;
		
		if(block.hasComparatorInputOverride()) {
			int val = block.getComparatorInputOverride(worldObj, binding.posX, binding.posY, binding.posZ, dir.getOpposite().ordinal());
			comparatorValue = val;
		} else comparatorValue = 0;
		
		if(origVal != comparatorValue)
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}
	
	public int getComparatorValue() {
		return comparatorValue;
	}
		
	@Override
	public boolean acceptBlock(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		return block.hasComparatorInputOverride();
	}

}
