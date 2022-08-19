/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [15/11/2015, 19:13:03 (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IRedirectable;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.common.core.helper.Vector3;

public class LensRedirect extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
		if(!entity.worldObj.isRemote && pos.entityHit == null && coords.posY != -1 && (pos.blockX != coords.posX || pos.blockY != coords.posY || pos.blockZ != coords.posZ)) {
			TileEntity tile = entity.worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
			if(tile != null && tile instanceof IRedirectable) {
				if(!burst.isFake()) {
					IRedirectable redir = (IRedirectable) tile;
					Vector3 tileVec = Vector3.fromTileEntityCenter(tile);
					Vector3 sourceVec = new Vector3(coords.posX + 0.5, coords.posY + 0.5, coords.posZ + 0.5);

					AxisAlignedBB axis = entity.worldObj.getBlock(coords.posX, coords.posY, coords.posZ).getCollisionBoundingBoxFromPool(entity.worldObj, coords.posX, coords.posY, coords.posZ);
					if(axis == null)
						axis = AxisAlignedBB.getBoundingBox(coords.posX, coords.posY, coords.posZ, coords.posX + 1, coords.posY + 1, coords.posZ + 1);

					if(!sourceVec.isInside(axis))
						sourceVec = new Vector3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

					Vector3 diffVec =  sourceVec.copy().sub(tileVec);
					Vector3 diffVec2D = new Vector3(diffVec.x, diffVec.z, 0);
					Vector3 rotVec = new Vector3(0, 1, 0);
					double angle = rotVec.angle(diffVec2D) / Math.PI * 180.0;

					if(sourceVec.x < tileVec.x)
						angle = -angle;

					redir.setRotationX((float) angle + 90F);

					rotVec = new Vector3(diffVec.x, 0, diffVec.z);
					angle = diffVec.angle(rotVec) * 180F / Math.PI;
					if(sourceVec.y < tileVec.y)
						angle = -angle;
					redir.setRotationY((float) angle);

					redir.commitRedirection();
					if(redir instanceof IThrottledPacket)
						((IThrottledPacket) redir).markDispatchable();
				}
			}
		}

		if(!isManaBlock) {
			dead = false;
			burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
		}

		return dead;
	}

}
