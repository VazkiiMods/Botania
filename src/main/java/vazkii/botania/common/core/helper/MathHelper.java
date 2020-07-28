/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class MathHelper {

	public static double angleBetween(Vec3d a, Vec3d b) {
		double projection = a.normalize().dotProduct(b.normalize());
		return Math.acos(net.minecraft.util.math.MathHelper.clamp(projection, -1, 1));
	}

	public static float pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
	}

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static void setEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier) {
		Vector3 entityVector = Vector3.fromEntityCenter(entity);
		Vector3 finalVector = originalPosVector.subtract(entityVector);

		if (finalVector.mag() > 1) {
			finalVector = finalVector.normalize();
		}

		entity.setVelocity(finalVector.multiply(modifier).toVector3d());
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
