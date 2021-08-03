/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

import vazkii.botania.common.block.tile.ModTiles;

public class TileRedStringDispenser extends TileRedStringContainer {
	public TileRedStringDispenser() {
		super(ModTiles.RED_STRING_DISPENSER);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return level.getBlockEntity(pos) instanceof DispenserBlockEntity;
	}

	public void tickDispenser() {
		BlockPos bind = getBinding();
		if (bind != null) {
			BlockEntity tile = level.getBlockEntity(bind);
			if (tile instanceof DispenserBlockEntity) {
				level.getBlockTicks().scheduleTick(bind, tile.getBlockState().getBlock(), 4);
			}
		}
	}

}
