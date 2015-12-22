package vazkii.botania.common.block.decor.stairs.living;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;

public class BlockLivingrockBrickStairs extends BlockLivingStairs {

	public BlockLivingrockBrickStairs() {
		super(ModBlocks.livingrock.getDefaultState().withProperty(BotaniaStateProps.LIVINGROCK_VARIANT, LivingRockVariant.BRICK));
	}

}
