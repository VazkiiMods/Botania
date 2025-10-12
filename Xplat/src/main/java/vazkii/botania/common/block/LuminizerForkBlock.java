package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import vazkii.botania.common.block.block_entity.LuminizerBlockEntity;

public class LuminizerForkBlock extends LuminizerBlock {

	public LuminizerForkBlock(Properties builder) {
		super(builder);
	}

	@Nullable
	public BlockPos getNextDestination(Level level, BlockState state, BlockPos blockPos, LuminizerBlockEntity blockEntity) {
		BlockPos torchPos = null;
		BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
		for (int i = -2; i < 3; i++) {
			testPos.setWithOffset(blockPos, 0, i, 0);

			BlockState testState = level.getBlockState(testPos);
			if (testState.is(BotaniaBlocks.animatedTorch)) {
				torchPos = testPos.immutable();
				break;
			}
		}

		if (torchPos != null && level.getBlockEntity(torchPos) instanceof AnimatedTorchBlockEntity torch) {
			Direction side = AnimatedTorchBlockEntity.SIDES[torch.side].getOpposite();
			testPos.set(blockPos);
			for (int i = 1; i < LuminizerBlockEntity.MAX_DIST; i++) {
				testPos.move(side);
				BlockState testState = level.getBlockState(testPos);
				if (testState.getBlock() instanceof LuminizerBlock) {
					return testPos.immutable();
				}
			}
		}

		return super.getNextDestination(level, state, blockPos, blockEntity);
	}
}
