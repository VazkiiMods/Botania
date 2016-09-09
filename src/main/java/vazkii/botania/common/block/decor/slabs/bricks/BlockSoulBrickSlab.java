/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 28, 2014, 10:19:46 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.bricks;

import net.minecraft.block.BlockSlab;
import vazkii.botania.api.state.enums.CustomBrickVariant;
import vazkii.botania.common.block.ModFluffBlocks;

public class BlockSoulBrickSlab extends BlockCustomBrickSlab {

	public BlockSoulBrickSlab(boolean full) {
		super(full, CustomBrickVariant.SOUL_BRICK);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.soulBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.soulBrickSlab;
	}

}
