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
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaInterceptor;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockCorporeaInterceptor extends BlockMod implements EntityBlock {

	public BlockCorporeaInterceptor(BlockBehaviour.Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public TileCorporeaBase newBlockEntity(@Nonnull BlockGetter world) {
		return new TileCorporeaInterceptor();
	}

}
