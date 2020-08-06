/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TileTeruTeruBozu extends TileMod implements ITickableTileEntity {
	private boolean wasRaining = false;

	public TileTeruTeruBozu() {
		super(ModTiles.TERU_TERU_BOZU);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}

		boolean isRaining = world.isRaining();
		if (isRaining && world.rand.nextInt(9600) == 0) {
			world.getWorldInfo().setRaining(false);
			resetRainTime(world);
		}

		if (wasRaining != isRaining) {
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}
		wasRaining = isRaining;
	}

	public static void resetRainTime(World w) {
		int time = w.rand.nextInt(w.getWorldInfo().isRaining() ? 12000 : 168000) + 12000;
		((ServerWorld) w).getServer().func_240793_aU_().func_230407_G_().setRainTime(time);
	}
}
