/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IDirectioned;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;

public class LensRedirect extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if (!entity.world.isRemote && pos.getType() == RayTraceResult.Type.BLOCK
				&& coords.getY() != -1
				&& !((BlockRayTraceResult) pos).getPos().equals(coords)) {
			TileEntity tile = entity.world.getTileEntity(((BlockRayTraceResult) pos).getPos());
			if (tile instanceof IDirectioned) {
				if (!burst.isFake()) {
					IDirectioned redir = (IDirectioned) tile;
					Vector3d tileVec = Vector3d.func_237489_a_(tile.getPos());
					Vector3d sourceVec = Vector3d.func_237489_a_(coords);

					AxisAlignedBB axis;
					VoxelShape collideShape = entity.world.getBlockState(coords).getCollisionShape(entity.world, coords);
					if (collideShape.isEmpty()) {
						axis = new AxisAlignedBB(coords, coords.add(1, 1, 1));
					} else {
						axis = collideShape.getBoundingBox().offset(coords); // todo 1.13 more granular collisions?
					}

					if (!axis.contains(sourceVec)) {
						sourceVec = new Vector3d(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);
					}

					Vector3d diffVec = sourceVec.subtract(tileVec);
					Vector3d diffVec2D = new Vector3d(diffVec.x, diffVec.z, 0);
					Vector3d rotVec = new Vector3d(0, 1, 0);
					double angle = MathHelper.angleBetween(rotVec, diffVec2D) / Math.PI * 180.0;

					if (sourceVec.x < tileVec.x) {
						angle = -angle;
					}

					redir.setRotationX((float) angle + 90F);

					rotVec = new Vector3d(diffVec.x, 0, diffVec.z);
					angle = MathHelper.angleBetween(diffVec, rotVec) * 180F / Math.PI;
					if (sourceVec.y < tileVec.y) {
						angle = -angle;
					}
					redir.setRotationY((float) angle);

					redir.commitRedirection();
					if (redir instanceof IThrottledPacket) {
						((IThrottledPacket) redir).markDispatchable();
					}
				}
			}
		}

		return dead;
	}

}
