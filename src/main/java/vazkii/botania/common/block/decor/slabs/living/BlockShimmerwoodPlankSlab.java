/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 11, 2015, 3:47:47 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockShimmerwoodPlankSlab extends BlockLivingSlab {

	public BlockShimmerwoodPlankSlab(boolean full) {
		super(full, ModBlocks.shimmerwoodPlanks.getDefaultState());
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.shimmerwoodPlankSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.shimmerwoodPlankSlab;
	}
}
