package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockLivingwoodSlab extends BlockLivingSlab {

	public BlockLivingwoodSlab(boolean full) {
		super(full, ModBlocks.livingwood, 0);
		setHardness(2.0F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModBlocks.livingwoodSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModBlocks.livingwoodSlab;
	}

}
