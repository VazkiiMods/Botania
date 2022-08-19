/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:46:39 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.IManaBurst;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
		if((coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) && !burst.isFake() && !isManaBlock && !entity.worldObj.isRemote) {
			ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit).getOpposite();
			int x = pos.blockX + dir.offsetX;
			int y = pos.blockY + dir.offsetY;
			int z = pos.blockZ + dir.offsetZ;

			if(entity.worldObj.isAirBlock(x, y, z) || entity.worldObj.getBlock(x, y, z).isReplaceable(entity.worldObj, x, y, z)) {
				Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				int meta = entity.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
				TileEntity tile = entity.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);

				if(block.getMobilityFlag() == 0 && block != Blocks.obsidian && block.getBlockHardness(entity.worldObj, pos.blockX, pos.blockY, pos.blockZ) >= 0 && tile == null) {
					entity.worldObj.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
					entity.worldObj.setBlock(x, y, z, block, meta, 1 | 2);
					entity.worldObj.playAuxSFX(2001, pos.blockX, pos.blockY, pos.blockZ, Block.getIdFromBlock(block) + (meta << 12));
				}
			}
		}

		return dead;
	}

}
