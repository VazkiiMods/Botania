/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public final class SparkHelper {

	public static final int SPARK_SCAN_RANGE = 12;

	public static List<ManaSpark> getSparksAround(Level world, double x, double y, double z, DyeColor color) {
		int r = SPARK_SCAN_RANGE;
		Predicate<Entity> predicate = e -> e instanceof ManaSpark spark && spark.getNetwork() == color;
		@SuppressWarnings("unchecked")
		List<ManaSpark> entities = (List<ManaSpark>) (List<?>) world.getEntitiesOfClass(Entity.class, new AABB(x - r, y - r, z - r, x + r, y + r, z + r), predicate);
		return entities;
	}

}
