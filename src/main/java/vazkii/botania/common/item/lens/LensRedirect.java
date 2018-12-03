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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IDirectioned;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.common.core.helper.Vector3;

public class LensRedirect extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if(!entity.world.isRemote && pos.entityHit == null && coords.getY() != -1 && (pos.getBlockPos() == null || !pos.getBlockPos().equals(coords))) {
			TileEntity tile = entity.world.getTileEntity(pos.getBlockPos());
			if(tile instanceof IDirectioned) {
				if(!burst.isFake()) {
					IDirectioned redir = (IDirectioned) tile;
					Vector3 tileVec = Vector3.fromTileEntityCenter(tile);
					Vector3 sourceVec = new Vector3(coords.getX() + 0.5, coords.getY() + 0.5, coords.getZ() + 0.5);

					AxisAlignedBB axis = entity.world.getBlockState(coords).getCollisionBoundingBox(entity.world, coords).offset(coords);
					if(axis == null)
						axis = new AxisAlignedBB(coords, coords.add(1, 1, 1));

					if(!sourceVec.isInside(axis))
						sourceVec = new Vector3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

					Vector3 diffVec =  sourceVec.subtract(tileVec);
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

		return dead;
	}

}
