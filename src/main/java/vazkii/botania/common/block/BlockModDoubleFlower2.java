package vazkii.botania.common.block;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import vazkii.botania.api.state.BotaniaStateProps;

// Silver to black
public class BlockModDoubleFlower2 extends BlockModDoubleFlower {

    public BlockModDoubleFlower2() {
        super(true);
        setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2, EnumDyeColor.SILVER));
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(this, HALF, BotaniaStateProps.DOUBLEFLOWER_VARIANT_2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = ((EnumDyeColor) state.getValue(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2)).getMetadata() - 8;
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        BlockDoublePlant.EnumBlockHalf half = (meta & 8) > 0 ? BlockDoublePlant.EnumBlockHalf.UPPER : BlockDoublePlant.EnumBlockHalf.LOWER;
        meta &= -9;
        return getDefaultState().withProperty(HALF, half).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2, EnumDyeColor.byMetadata(meta + 8));
    }
}
