/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class MathHelper {

	public static double angleBetween(Vec3 a, Vec3 b) {
		double projection = a.normalize().dot(b.normalize());
		return Math.acos(net.minecraft.util.Mth.clamp(projection, -1, 1));
	}

	public static float pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
	}

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static void setEntityMotionFromVector(Entity entity, Vec3 originalPosVector, float modifier) {
		Vec3 entityVector = VecHelper.fromEntityCenter(entity);
		Vec3 finalVector = originalPosVector.subtract(entityVector);

		if (finalVector.length() > 1) {
			finalVector = finalVector.normalize();
		}

		entity.setDeltaMovement(finalVector.scale(modifier));
	}

	public static int multiplyColor(int c1, int c2) {
		int r1 = (c1 & 0xFF0000) >> 16;
		int r2 = (c2 & 0xFF0000) >> 16;
		int g1 = (c1 & 0x00FF00) >> 8;
		int g2 = (c2 & 0x00FF00) >> 8;
		int b1 = (c1 & 0x0000FF);
		int b2 = (c2 & 0x0000FF);
		int r = (int) (r1 * (r2 / 255.0F));
		int g = (int) (g1 * (g2 / 255.0F));
		int b = (int) (b1 * (b2 / 255.0F));
		return c1 & ~0xFFFFFF | r << 16 | g << 8 | b;
	}

}
