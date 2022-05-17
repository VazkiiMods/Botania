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
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;

public class TileTinyPlanet extends TileMod {
	public TileTinyPlanet(BlockPos pos, BlockState state) {
		super(ModTiles.TINY_PLANET, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileTinyPlanet self) {
		ItemTinyPlanet.applyEffect(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
	}

}
