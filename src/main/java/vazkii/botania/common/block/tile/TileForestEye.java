/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.phys.AABB;

public class TileForestEye extends TileMod implements TickableBlockEntity {
	public int entities = 0;

	public TileForestEye() {
		super(ModTiles.FORSET_EYE);
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			return;
		}
		int range = 6;
		int entityCount = level.getEntitiesOfClass(Animal.class, new AABB(worldPosition.offset(-range, -range, -range), worldPosition.offset(range + 1, range + 1, range + 1))).size();
		if (entityCount != entities) {
			entities = entityCount;
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}
	}

}
