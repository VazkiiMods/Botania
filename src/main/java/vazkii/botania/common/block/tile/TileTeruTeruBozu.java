/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;

public class TileTeruTeruBozu extends TileMod implements TickableBlockEntity {
	private boolean wasRaining = false;

	public TileTeruTeruBozu(BlockPos pos, BlockState state) {
		super(ModTiles.TERU_TERU_BOZU, pos, state);
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			return;
		}

		boolean isRaining = level.isRaining();
		if (isRaining && level.random.nextInt(9600) == 0) {
			level.getLevelData().setRaining(false);
			resetRainTime(level);
		}

		if (wasRaining != isRaining) {
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}
		wasRaining = isRaining;
	}

	public static void resetRainTime(Level w) {
		int time = w.random.nextInt(w.getLevelData().isRaining() ? 12000 : 168000) + 12000;
		LevelData info = w.getLevelData();
		if (info instanceof ServerLevelData) {
			((ServerLevelData) info).setRainTime(time);
		}
	}
}
