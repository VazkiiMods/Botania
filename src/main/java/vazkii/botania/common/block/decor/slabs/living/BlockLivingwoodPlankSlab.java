package vazkii.botania.common.block.decor.slabs.living;

import net.minecraft.block.BlockSlab;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;

public class BlockLivingwoodPlankSlab extends BlockLivingSlab {

	public BlockLivingwoodPlankSlab(boolean full) {
		super(full, ModBlocks.livingwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.PLANKS));
		setHardness(2.0F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.livingwoodPlankSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.livingwoodPlankSlab;
	}

}
