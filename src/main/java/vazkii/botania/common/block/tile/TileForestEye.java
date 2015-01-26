/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 10, 2014, 5:50:01 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;

public class TileForestEye extends TileMod {

	public int entities = 0;

	@Override
	public void updateEntity() {
		int range = 6;
		int entityCount = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range + 1, yCoord + range + 1, zCoord + range + 1)).size();
		if(entityCount != entities) {
			entities = entityCount;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
	}

}
