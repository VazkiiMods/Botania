/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 5:49:50 PM (GMT)]
 */
package vazkii.botania.common.core.helper;

import net.minecraft.entity.Entity;
import vazkii.botania.api.internal.VanillaPacketDispatcher;

public final class MathHelper {
	public static float pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
	}

	// Backwards compatibility
	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return VanillaPacketDispatcher.pointDistancePlane(x1, y1, x2, y2);
	}

	public static void setEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier) {
		Vector3 entityVector = Vector3.fromEntityCenter(entity);
		Vector3 finalVector = originalPosVector.subtract(entityVector);

		if(finalVector.mag() > 1)
			finalVector = finalVector.normalize();

		entity.motionX = finalVector.x * modifier;
		entity.motionY = finalVector.y * modifier;
		entity.motionZ = finalVector.z * modifier;
	}

	private static final String[] ORDINAL_SUFFIXES = new String[]{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	public static String numberToOrdinal(int i) {
		return i % 100 == 11 || i % 100 == 12 || i % 100 == 13 ? i + "th" : i + ORDINAL_SUFFIXES[i % 10];
	}

}
