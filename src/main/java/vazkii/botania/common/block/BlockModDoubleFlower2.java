package vazkii.botania.common.block;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;

import javax.annotation.Nonnull;

// Silver to black
public class BlockModDoubleFlower2 extends BlockModDoubleFlower {

	public BlockModDoubleFlower2() {
		super(true);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, VARIANT, HALF, BotaniaStateProps.DOUBLEFLOWER_VARIANT_2);
	}

	@Override
	protected IBlockState pickDefaultState() {
		return blockState.getBaseState()
				.with(FACING, EnumFacing.SOUTH)
				.with(VARIANT, EnumPlantType.SUNFLOWER)
				.with(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
				.with(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2, EnumDyeColor.SILVER);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.get(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2).getMetadata() - 8;
		if (state.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			meta |= 8;
		}
		return meta;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		BlockDoublePlant.EnumBlockHalf half = (meta & 8) > 0 ? BlockDoublePlant.EnumBlockHalf.UPPER : BlockDoublePlant.EnumBlockHalf.LOWER;
		meta &= -9;
		return getDefaultState().with(HALF, half).with(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2, EnumDyeColor.byMetadata(meta + 8));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModels() {
		super.registerModels();
		ModelHandler.registerCustomItemblock(this, 8, i -> "double_flower_" + EnumDyeColor.byMetadata(i + 8).getName());
	}
}
