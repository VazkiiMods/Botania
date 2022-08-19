/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:46:03 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class LensFire extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
		if((coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) && !burst.isFake() && !isManaBlock) {
			ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);

			int x = pos.blockX + dir.offsetX;
			int y = pos.blockY + dir.offsetY;
			int z = pos.blockZ + dir.offsetZ;

			Block blockAt = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			Block blockAt_ = entity.worldObj.getBlock(x, y, z);

			if(blockAt == Blocks.portal)
				entity.worldObj.setBlock(pos.blockX, pos.blockY, pos.blockZ, Blocks.air);
			else if(blockAt == ModBlocks.incensePlate) {
				TileIncensePlate plate = (TileIncensePlate) entity.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				plate.ignite();
			} else if(blockAt_.isAir(entity.worldObj, x, y, z))
				entity.worldObj.setBlock(x, y, z, Blocks.fire);
		}

		return dead;
	}

}
