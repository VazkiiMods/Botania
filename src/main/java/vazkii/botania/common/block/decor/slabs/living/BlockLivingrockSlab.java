package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockLivingrockSlab extends BlockLivingSlab {

	public BlockLivingrockSlab(boolean full) {
		super(full, ModBlocks.livingrock.getDefaultState());
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.livingrockSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.livingrockSlab;
	}

}
