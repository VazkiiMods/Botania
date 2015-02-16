/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 8, 2015, 2:17:01 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;

public class LensWarp extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(burst.isFake())
			return dead;

		Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		if(block == ModBlocks.pistonRelay) {
			String key = BlockPistonRelay.mappedPositions.get(BlockPistonRelay.getCoordsAsString(entity.worldObj.provider.dimensionId, pos.blockX, pos.blockY, pos.blockZ));
			if(key != null) {
				String[] tokens = key.split(":");
				int worldId = Integer.parseInt(tokens[0]), x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]), z = Integer.parseInt(tokens[3]);
				if(worldId == entity.worldObj.provider.dimensionId) {
					entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
					burst.setCollidedAt(x, y, z);
					return false;
				}
			}
		}
		return dead;
	}

}
