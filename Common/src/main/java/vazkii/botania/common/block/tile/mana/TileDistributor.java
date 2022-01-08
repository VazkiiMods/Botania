/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

import java.util.ArrayList;
import java.util.List;

public class TileDistributor extends TileMod implements IManaReceiver {
	private final List<IManaReceiver> validPools = new ArrayList<>();

	public TileDistributor(BlockPos pos, BlockState state) {
		super(ModTiles.DISTRIBUTOR, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TileDistributor self) {
		self.validPools.clear();
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			BlockPos pos = worldPosition.relative(dir);
			if (level.hasChunkAt(pos)) {
				BlockEntity tileAt = level.getBlockEntity(pos);
				if (tileAt instanceof IManaPool && !tileAt.isRemoved()) {
					IManaReceiver receiver = (IManaReceiver) tileAt;
					if (!receiver.isFull()) {
						self.validPools.add(receiver);
					}
				}
			}
		}
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return validPools.isEmpty();
	}

	@Override
	public void receiveMana(int mana) {
		int tiles = validPools.size();
		if (tiles != 0) {
			int manaForEach = mana / tiles;
			for (IManaReceiver pool : validPools) {
				pool.receiveMana(manaForEach);
			}
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}
}
