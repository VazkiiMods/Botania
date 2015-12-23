package vazkii.botania.common.block;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import vazkii.botania.api.state.BotaniaStateProps;

// White to Gray
public class BlockModDoubleFlower1 extends BlockModDoubleFlower {
    public BlockModDoubleFlower1() {
        super(false);
        setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, EnumDyeColor.WHITE));
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(this, HALF, BotaniaStateProps.DOUBLEFLOWER_VARIANT_1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = ((EnumDyeColor) state.getValue(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1)).getMetadata();
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        BlockDoublePlant.EnumBlockHalf half = (meta & 8) > 0 ? BlockDoublePlant.EnumBlockHalf.UPPER : BlockDoublePlant.EnumBlockHalf.LOWER;
        meta &= -9;
        return getDefaultState().withProperty(HALF, half).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, EnumDyeColor.byMetadata(meta));
    }
}
