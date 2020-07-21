/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;

public class TileForestEye extends TileMod implements Tickable {
	public int entities = 0;

	public TileForestEye() {
		super(ModTiles.FORSET_EYE);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}
		int range = 6;
		int entityCount = world.getNonSpectatingEntities(AnimalEntity.class, new Box(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))).size();
		if (entityCount != entities) {
			entities = entityCount;
			world.updateComparators(pos, getCachedState().getBlock());
		}
	}

}
