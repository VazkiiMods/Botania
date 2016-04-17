package vazkii.botania.common.block.decor.slabs.prismarine;

import net.minecraft.block.BlockSlab;
import vazkii.botania.api.state.enums.PrismarineVariant;
import vazkii.botania.common.block.ModFluffBlocks;

public class BlockDarkPrismarineSlab extends BlockPrismarineSlab {

	public BlockDarkPrismarineSlab(boolean full) {
		super(full, PrismarineVariant.DARK_PRISMARINE);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.darkPrismarineSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.darkPrismarineSlab;
	}

}
