package vazkii.botania.common.block;

import net.minecraft.block.WallSkullBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import vazkii.botania.common.block.tile.TileGaiaHead;

import javax.annotation.Nonnull;

public class BlockGaiaHeadWall extends WallSkullBlock {
    public BlockGaiaHeadWall(Properties builder) {
        super(BlockGaiaHead.GAIA_TYPE, builder);
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileGaiaHead();
    }
}
