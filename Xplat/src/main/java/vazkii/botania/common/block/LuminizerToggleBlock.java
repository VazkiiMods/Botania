package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.LuminizerBlockEntity;

public class LuminizerToggleBlock extends LuminizerPoweredBlock {

	public LuminizerToggleBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide) {
			if (state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, false));
			} else if (!state.getValue(POWERED) && worldIn.hasNeighborSignal(pos)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, true));
			}
		}
	}

	@Nullable
	public BlockPos getNextDestination(Level level, BlockState state, BlockPos blockPos, LuminizerBlockEntity blockEntity) {
		return state.getValue(POWERED)
				? null
				: super.getNextDestination(level, state, blockPos, blockEntity);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED)) {
			addRedstoneParticle(level, pos, random);
		}
	}
}
