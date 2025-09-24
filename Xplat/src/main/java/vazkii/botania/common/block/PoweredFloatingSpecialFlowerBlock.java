package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.block.PoweredFlowerBlock;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.function.Supplier;

public class PoweredFloatingSpecialFlowerBlock extends FloatingSpecialFlowerBlock implements PoweredFlowerBlock {
	public PoweredFloatingSpecialFlowerBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(props, blockEntityType);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		PoweredSpecialFlowerBlock.updateRedstonePower(state, world, pos);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (isPowered(state) && rand.nextBoolean()) {
			PoweredSpecialFlowerBlock.addRedstoneParticlesInShape(state, world, pos, rand);
		}
	}

	@Override
	public boolean isPowered(BlockState state) {
		return state.getValue(BlockStateProperties.POWERED);
	}

}
