/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.common.block.tile.ModTiles;

public class TileRedStringDispenser extends TileRedStringContainer {
	public TileRedStringDispenser() {
		super(ModTiles.RED_STRING_DISPENSER);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockEntity(pos) instanceof DispenserBlockEntity;
	}

	public void tickDispenser() {
		BlockPos bind = getBinding();
		if (bind != null) {
			BlockEntity tile = world.getBlockEntity(bind);
			if (tile instanceof DispenserBlockEntity) {
				world.getBlockTickScheduler().schedule(bind, tile.getCachedState().getBlock(), 4);
			}
		}
	}

}
