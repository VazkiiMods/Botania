/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 28, 2014, 11:49:42 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.bricks;

import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModFluffBlocks;

public class BlockTileSlab extends BlockCustomBrickSlab {

	public BlockTileSlab(boolean full) {
		super(full, 3);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.tileSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.tileSlab;
	}

}
