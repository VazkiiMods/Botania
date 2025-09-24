package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.block.PoweredFlowerBlock;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.function.Supplier;

public class PoweredSpecialFlowerBlock extends SpecialFlowerBlock implements PoweredFlowerBlock {
	public PoweredSpecialFlowerBlock(Holder<MobEffect> stewEffect, int stewDuration, Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(stewEffect, stewDuration, props, blockEntityType);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		updateRedstonePower(state, world, pos);
	}

	public static void updateRedstonePower(BlockState state, Level world, BlockPos pos) {
		boolean isPowered = world.getBestNeighborSignal(pos) > 0;
		if (isPowered != state.getValue(BlockStateProperties.POWERED)) {
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, isPowered), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (isPowered(state) && rand.nextBoolean()) {
			addRedstoneParticlesInShape(state, world, pos, rand);
		}
	}

	@Override
	public boolean isPowered(BlockState state) {
		return state.getValue(BlockStateProperties.POWERED);
	}

}
