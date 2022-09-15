/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileCraftCrate;

public class BlockCraftyCrate extends BlockOpenCrate {

	public BlockCraftyCrate(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.CRATE_PATTERN, CraftyCratePattern.NONE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProperties.CRATE_PATTERN);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		BlockEntity block = world.getBlockEntity(pos);
		if (block instanceof TileCraftCrate crate) {
			return crate.getSignal();
		}
		return 0;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TileCraftCrate(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, ModTiles.CRAFT_CRATE, TileCraftCrate::serverTick);
		}
		return null;
	}
}
