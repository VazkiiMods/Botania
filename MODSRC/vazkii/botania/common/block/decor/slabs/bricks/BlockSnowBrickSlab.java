/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 28, 2014, 11:32:45 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.bricks;

import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModBlocks;

public class BlockSnowBrickSlab extends BlockCustomBrickSlab {

	public BlockSnowBrickSlab(boolean full) {
		super(full, 2);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.snowBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.snowBrickSlab;
	}

}