/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IDirectioned;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.common.core.helper.MathHelper;

public class LensRedirect extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if (!entity.world.isClient && pos.getType() == HitResult.Type.BLOCK
				&& coords.getY() != -1
				&& !((BlockHitResult) pos).getBlockPos().equals(coords)) {
			BlockEntity tile = entity.world.getBlockEntity(((BlockHitResult) pos).getBlockPos());
			if (tile instanceof IDirectioned) {
				if (!burst.isFake()) {
					IDirectioned redir = (IDirectioned) tile;
					Vec3d tileVec = Vec3d.ofCenter(tile.getPos());
					Vec3d sourceVec = Vec3d.ofCenter(coords);

					Box axis;
					VoxelShape collideShape = entity.world.getBlockState(coords).getCollisionShape(entity.world, coords);
					if (collideShape.isEmpty()) {
						axis = new Box(coords, coords.add(1, 1, 1));
					} else {
						axis = collideShape.getBoundingBox().offset(coords);
					}

					if (!axis.contains(sourceVec)) {
						sourceVec = new Vec3d(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);
					}

					Vec3d diffVec = sourceVec.subtract(tileVec);
					Vec3d diffVec2D = new Vec3d(diffVec.x, diffVec.z, 0);
					Vec3d rotVec = new Vec3d(0, 1, 0);
					double angle = MathHelper.angleBetween(rotVec, diffVec2D) / Math.PI * 180.0;

					if (sourceVec.x < tileVec.x) {
						angle = -angle;
					}

					redir.setRotationX((float) angle + 90F);

					rotVec = new Vec3d(diffVec.x, 0, diffVec.z);
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
