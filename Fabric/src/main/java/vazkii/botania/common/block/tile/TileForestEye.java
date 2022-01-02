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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileForestEye extends TileMod {
	public int entities = 0;

	public TileForestEye(BlockPos pos, BlockState state) {
		super(ModTiles.FORSET_EYE, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TileForestEye self) {
		int range = 6;
		int entityCount = level.getEntitiesOfClass(Animal.class, new AABB(worldPosition.offset(-range, -range, -range), worldPosition.offset(range + 1, range + 1, range + 1))).size();
		if (entityCount != self.entities) {
			self.entities = entityCount;
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}
	}

}
