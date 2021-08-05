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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import javax.annotation.Nonnull;

public class BlockRFGenerator extends BlockMod implements EntityBlock {

	public BlockRFGenerator(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileRFGenerator(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, ModTiles.FLUXFIELD, TileRFGenerator::serverTick);
		}
		return null;
	}
}
