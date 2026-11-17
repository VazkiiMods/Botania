/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaWaterloggedEntityBlock;
import vazkii.botania.common.block.block_entity.LifeImbuerBlockEntity;

public class LifeImbuerBlock extends BotaniaWaterloggedEntityBlock {

	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 2, 14);

	public LifeImbuerBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.ACTIVE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LifeImbuerBlockEntity(pos, state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
			BlockPos pos, BlockPos neighborPos) {
		return super.updateShape(
				// immediately stop looking active if block below no longer is a spawner
				direction == Direction.DOWN && state.getValue(BotaniaStateProperties.ACTIVE) && !neighborState.is(Blocks.SPAWNER)
						? state.setValue(BotaniaStateProperties.ACTIVE, false)
						: state,
				direction, neighborState, level, pos, neighborPos);
	}
}
