/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.item.equipment.bauble.TinyPlanetItem;

public class TinyPlanetBlockEntity extends BotaniaBlockEntity {
	public TinyPlanetBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TINY_PLANET, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TinyPlanetBlockEntity self) {
		TinyPlanetItem.applyEffect(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
	}

}
