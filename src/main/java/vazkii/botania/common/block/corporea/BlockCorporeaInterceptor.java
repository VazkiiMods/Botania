/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaInterceptor;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockCorporeaInterceptor extends BlockMod implements BlockEntityProvider {

	public BlockCorporeaInterceptor(AbstractBlock.Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.POWERED);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(Properties.POWERED, false));
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction side) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createBlockEntity(@Nonnull BlockView world) {
		return new TileCorporeaInterceptor();
	}

}
