package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockLivingrockBrickSlab extends BlockLivingSlab {

	public BlockLivingrockBrickSlab(boolean full) {
		super(full, ModBlocks.livingrock.getDefaultState().withProperty(BotaniaStateProps.LIVINGROCK_VARIANT, LivingRockVariant.BRICK));
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.livingrockBrickSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.livingrockBrickSlab;
	}

}
