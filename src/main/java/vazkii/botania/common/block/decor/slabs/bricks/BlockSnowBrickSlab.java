/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 28, 2014, 11:32:45 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.bricks;

import net.minecraft.block.BlockSlab;
import vazkii.botania.api.state.enums.CustomBrickVariant;
import vazkii.botania.common.block.ModFluffBlocks;

public class BlockSnowBrickSlab extends BlockCustomBrickSlab {

	public BlockSnowBrickSlab(boolean full) {
		super(full, CustomBrickVariant.FROSTY_BRICK);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.snowBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.snowBrickSlab;
	}

}