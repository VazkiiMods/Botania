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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.OpenCrateBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.SpreaderTurntableBlockEntity;

public class SpreaderTurntableBlock extends BotaniaBlock implements EntityBlock {

	public SpreaderTurntableBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.POWERED, false)
				.setValue(BotaniaStateProperties.SPEED, 1)
				.setValue(BotaniaStateProperties.BACKWARDS, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProperties.SPEED, BotaniaStateProperties.BACKWARDS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		RedstoneSensitiveBlock.updateRedstonePower(state, world, pos);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpreaderTurntableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.SPREADER_TURNTABLE, SpreaderTurntableBlockEntity::commonTick);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (state.getValue(BlockStateProperties.POWERED) && rand.nextDouble() < 0.2) {
			OpenCrateBlock.redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}
}
