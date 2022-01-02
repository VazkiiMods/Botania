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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringComparator;

import javax.annotation.Nonnull;

public class BlockRedStringComparator extends BlockRedString {

	public BlockRedStringComparator(BlockBehaviour.Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN));
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((TileRedStringComparator) world.getBlockEntity(pos)).getComparatorValue();
	}

	@Nonnull
	@Override
	public TileRedString newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileRedStringComparator(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.RED_STRING_COMPARATOR, TileRedStringComparator::commonTick);
	}
}
