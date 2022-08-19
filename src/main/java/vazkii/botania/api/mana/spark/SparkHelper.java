/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 7:16:11 PM (GMT)]
 */
package vazkii.botania.api.mana.spark;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public final class SparkHelper {

	public static final int SPARK_SCAN_RANGE = 12;

	public static List<ISparkEntity> getSparksAround(World world, double x, double y, double z) {
		return SparkHelper.getEntitiesAround(ISparkEntity.class, world, x, y, z);
	}

	public static <T> List<T> getEntitiesAround(Class<? extends T> clazz, World world, double x, double y, double z) {
		int r = SPARK_SCAN_RANGE;
		List<T> entities = world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x - r, y - r, z - r, x + r, y + r, z + r));
		return entities;
	}

}
