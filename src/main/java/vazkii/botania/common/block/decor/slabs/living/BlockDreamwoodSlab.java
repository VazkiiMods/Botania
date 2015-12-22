package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockDreamwoodSlab extends BlockLivingSlab {

	public BlockDreamwoodSlab(boolean full) {
		super(full, ModBlocks.dreamwood.getDefaultState());
		setHardness(2.0F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.dreamwoodSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.dreamwoodSlab;
	}

}
