/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;

public class TileTeruTeruBozu extends TileMod implements Tickable {
	private boolean wasRaining = false;

	public TileTeruTeruBozu() {
		super(ModTiles.TERU_TERU_BOZU);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		boolean isRaining = world.isRaining();
		if (isRaining && world.random.nextInt(9600) == 0) {
			world.getLevelProperties().setRaining(false);
			resetRainTime(world);
		}

		if (wasRaining != isRaining) {
			world.updateComparators(pos, getCachedState().getBlock());
		}
		wasRaining = isRaining;
	}

	public static void resetRainTime(World w) {
		int time = w.random.nextInt(w.getLevelProperties().isRaining() ? 12000 : 168000) + 12000;
		((ServerWorld) w).getServer().getSaveProperties().getMainWorldProperties().setRainTime(time);
	}
}
