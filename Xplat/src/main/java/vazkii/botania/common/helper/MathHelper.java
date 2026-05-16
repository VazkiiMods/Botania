/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class MathHelper {

	public static double angleBetween(Vec3 a, Vec3 b) {
		double projection = a.normalize().dot(b.normalize());
		return Math.acos(net.minecraft.util.Mth.clamp(projection, -1, 1));
	}

	public static float pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
	}

	public static float pointDistancePlane(double x1, double z1, double x2, double z2) {
		return (float) Math.hypot(x1 - x2, z1 - z2);
	}

	public static void setEntityMotionFromVector(Entity entity, Vec3 originalPosVector, float modifier) {
		Vec3 entityVector = VecHelper.fromEntityCenter(entity);
		Vec3 finalVector = originalPosVector.subtract(entityVector);

		if (finalVector.length() > 1) {
			finalVector = finalVector.normalize();
		}

		entity.setDeltaMovement(finalVector.scale(modifier));
	}

	public static long distSqr(Vec3i a, Vec3i b) {
		//Vec3i#distSqr, while convenient, offsets the second argument by (0.5, 0.5, 0.5).
		//Longs are used because "dx * dx" overflows with distances longer than about 46,300 blocks when using integers.
		long dx = a.getX() - b.getX();
		long dy = a.getY() - b.getY();
		long dz = a.getZ() - b.getZ();
		return dx * dx + dy * dy + dz * dz;
	}

	public static long distSqr2D(int xDiff, int zDiff) {
		return (long) xDiff * xDiff + (long) zDiff * zDiff;
	}

	public static double distSqr2D(double xDiff, double zDiff) {
		return xDiff * xDiff + zDiff * zDiff;
	}

	public static AABB inflateBoxAround(Vec3i pos, int amount) {
		return inflateBoxAround(pos, amount, amount, amount, amount);
	}

	public static AABB inflateBoxAround(Vec3i pos, int amountH, int amountV) {
		return inflateBoxAround(pos, amountH, amountH, amountV, amountV);
	}

	public static AABB inflateBoxAround(Vec3i pos, int amountX, int amountZ, int amountDown, int amountUp) {
		return new AABB(
				pos.getX() - amountX, pos.getY() - amountDown, pos.getZ() - amountZ,
				pos.getX() + amountX + 1, pos.getY() + amountUp + 1, pos.getZ() + amountZ + 1
		);
	}

	public static Iterable<BlockPos> aroundPosClosed(BlockPos pos, int range) {
		return aroundPosClosed(pos, range, range, range);
	}

	public static Iterable<BlockPos> aroundPosClosed(BlockPos pos, int rangeH, int rangeV) {
		return aroundPosClosed(pos, rangeH, rangeV, rangeV);
	}

	public static Iterable<BlockPos> aroundPosClosed(BlockPos pos, int rangeH, int rangeDown, int rangeUp) {
		return BlockPos.betweenClosed(
				pos.getX() - rangeH, pos.getY() - rangeDown, pos.getZ() - rangeH,
				pos.getX() + rangeH, pos.getY() + rangeUp, pos.getZ() + rangeH);
	}
}
