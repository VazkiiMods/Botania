package vazkii.botania.common.block.decor.stairs.living;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;

public class BlockDreamwoodPlankStairs extends BlockLivingStairs {

	public BlockDreamwoodPlankStairs() {
		super(ModBlocks.dreamwood.getDefaultState().withProperty(BotaniaStateProps.LIVINGWOOD_VARIANT, LivingWoodVariant.PLANKS));
	}

}
