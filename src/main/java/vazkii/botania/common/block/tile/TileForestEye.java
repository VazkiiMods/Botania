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
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileForestEye extends TileMod implements ITickableTileEntity {
	public int entities = 0;

	public TileForestEye() {
		super(ModTiles.FORSET_EYE);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}
		int range = 6;
		int entityCount = world.getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))).size();
		if (entityCount != entities) {
			entities = entityCount;
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}
	}

}
