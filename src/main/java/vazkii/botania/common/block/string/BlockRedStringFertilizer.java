/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockRedStringFertilizer extends BlockRedString implements Fertilizable {

	public BlockRedStringFertilizer(AbstractBlock.Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.FACING, Direction.DOWN));
	}

	@Override
	public boolean isFertilizable(@Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
		return ((TileRedStringFertilizer) world.getBlockEntity(pos)).canGrow(world, isClient);
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return ((TileRedStringFertilizer) world.getBlockEntity(pos)).canUseBonemeal(world, rand);
	}

	@Override
	public void grow(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		((TileRedStringFertilizer) world.getBlockEntity(pos)).grow(world, rand);
	}

	@Nonnull
	@Override
	public TileRedString createBlockEntity(@Nonnull BlockView world) {
		return new TileRedStringFertilizer();
	}
}
