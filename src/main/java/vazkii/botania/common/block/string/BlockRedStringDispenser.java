/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringDispenser;

import javax.annotation.Nonnull;

public class BlockRedStringDispenser extends BlockRedString {

	public BlockRedStringDispenser(BlockBehaviour.Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN).setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			((TileRedStringDispenser) world.getBlockEntity(pos)).tickDispenser();
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Nonnull
	@Override
	public TileRedString newBlockEntity(@Nonnull BlockGetter world) {
		return new TileRedStringDispenser();
	}
}
