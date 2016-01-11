package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

// Multiblock component that only compares blocks, not blockstates
public class StateInsensitiveComponent extends MultiblockComponent {

    public StateInsensitiveComponent(BlockPos relPos, Block block) {
        super(relPos, block.getDefaultState(), false, null);
    }

    @Override
    public boolean matches(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this.getBlockState().getBlock();
    }

    @Override
    public MultiblockComponent copy() {
        return new StateInsensitiveComponent(getRelativePosition(), getBlockState().getBlock());
    }

}
