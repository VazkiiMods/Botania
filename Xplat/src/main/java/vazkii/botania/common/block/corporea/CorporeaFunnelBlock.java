/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.BotaniaEntityBlock;
import vazkii.botania.common.block.block_entity.corporea.BaseCorporeaBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaFunnelBlockEntity;

public class CorporeaFunnelBlock extends BotaniaEntityBlock {

	public CorporeaFunnelBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		boolean power = level.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), Block.UPDATE_INVISIBLE);
			if (level.getBlockEntity(pos) instanceof CorporeaFunnelBlockEntity funnel) {
				funnel.doRequest();
			}
		} else if (!power && powered) {
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), Block.UPDATE_INVISIBLE);
		}
	}

	@Override
	public BaseCorporeaBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CorporeaFunnelBlockEntity(pos, state);
	}

}
