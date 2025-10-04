package vazkii.botania.common.block.flower;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.function.Supplier;

public class DaffomillBlock extends PoweredSpecialFlowerBlock {

	public DaffomillBlock(Holder<MobEffect> stewEffect, int stewDuration,
			Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(stewEffect, stewDuration, props, blockEntityType);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return super.mirror(state, mirror).setValue(BlockStateProperties.HORIZONTAL_FACING,
				mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return super.rotate(state, rotation).setValue(BlockStateProperties.HORIZONTAL_FACING,
				rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}
}
