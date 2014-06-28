package vazkii.botania.common.block.decor.slabs.prismarine;

import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockPrismarineBrickSlab extends BlockPrismarineSlab {

	public BlockPrismarineBrickSlab(boolean full) {
		super(full, 1);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.prismarineBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.prismarineBrickSlab;
	}

}
