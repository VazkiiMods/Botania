package vazkii.botania.common.block.decor.slabs.prismarine;

import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockDarkPrismarineSlab extends BlockPrismarineSlab {

	public BlockDarkPrismarineSlab(boolean full) {
		super(full, 2);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.darkPrismarineSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.darkPrismarineSlab;
	}

}
