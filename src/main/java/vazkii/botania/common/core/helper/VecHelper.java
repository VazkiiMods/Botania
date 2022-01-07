/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class VecHelper {
	public static final Vec3 ONE = new Vec3(1, 1, 1);

	private VecHelper() {}

	public static Vec3 fromEntityCenter(Entity e) {
		return new Vec3(e.getX(), e.getY() + e.getBbHeight() / 2, e.getZ());
	}

	/**
	 * Rotates {@code v} by {@code theta} radians around {@code axis}
	 */
	public static Vec3 rotate(Vec3 v, double theta, Vec3 axis) {
		if (Mth.equal(theta, 0)) {
			return v;
		}

		// Rodrigues rotation formula
		Vec3 k = axis.normalize();

		float cosTheta = Mth.cos((float) theta);
		Vec3 firstTerm = v.scale(cosTheta);
		Vec3 secondTerm = k.cross(v).scale(Mth.sin((float) theta));
		Vec3 thirdTerm = k.scale(k.dot(v) * (1 - cosTheta));
		return new Vec3(firstTerm.x + secondTerm.x + thirdTerm.x,
				firstTerm.y + secondTerm.y + thirdTerm.y,
				firstTerm.z + secondTerm.z + thirdTerm.z);
	}

	public static AABB boxForRange(Vec3 v, double range) {
		return boxForRange(v, range, range, range);
	}

	public static AABB boxForRange(Vec3 v, double rangeX, double rangeY, double rangeZ) {
		return new AABB(v.x - rangeX, v.y - rangeY, v.z - rangeZ, v.x + rangeX, v.y + rangeY, v.z + rangeZ);
	}
}
