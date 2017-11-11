package vazkii.botania.common.block;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;

import javax.annotation.Nonnull;

// White to Gray
public class BlockModDoubleFlower1 extends BlockModDoubleFlower {
	public BlockModDoubleFlower1() {
		super(false);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, VARIANT, HALF, BotaniaStateProps.DOUBLEFLOWER_VARIANT_1);
	}

	@Override
	protected IBlockState pickDefaultState() {
		return blockState.getBaseState()
				.withProperty(FACING, EnumFacing.SOUTH)
				.withProperty(VARIANT, EnumPlantType.SUNFLOWER)
				.withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
				.withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, EnumDyeColor.WHITE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1).getMetadata();
		if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			meta |= 8;
		}
		return meta;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		BlockDoublePlant.EnumBlockHalf half = (meta & 8) > 0 ? BlockDoublePlant.EnumBlockHalf.UPPER : BlockDoublePlant.EnumBlockHalf.LOWER;
		meta &= -9;
		return getDefaultState().withProperty(HALF, half).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, EnumDyeColor.byMetadata(meta));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		super.registerModels();
		ModelHandler.registerCustomItemblock(this, 8, i -> "double_flower_" + EnumDyeColor.byMetadata(i).getName());
	}
}
