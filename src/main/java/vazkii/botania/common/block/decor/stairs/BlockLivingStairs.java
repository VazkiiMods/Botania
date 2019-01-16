package vazkii.botania.common.block.decor.stairs;

import net.minecraft.block.state.IBlockState;

public class BlockLivingStairs extends BlockModStairs {

	public BlockLivingStairs(IBlockState state) {
		super(state, state.getBlock().getTranslationKey().replaceAll("tile.", "") + state.getBlock().getMetaFromState(state) + "Stairs");
	}

}
