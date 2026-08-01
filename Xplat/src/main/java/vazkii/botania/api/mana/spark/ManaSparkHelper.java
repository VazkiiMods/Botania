/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.ManaPool;

import java.util.List;
import java.util.function.Predicate;

public final class ManaSparkHelper {

	public static final int SPARK_SCAN_RANGE = 12;

	public static List<ManaSpark> getSparksAround(Level world, double x, double y, double z, DyeColor color) {
		int r = SPARK_SCAN_RANGE;
		Predicate<Entity> predicate = e -> e instanceof ManaSpark spark && spark.getNetwork() == color;
		@SuppressWarnings("unchecked")
		List<ManaSpark> entities = (List<ManaSpark>) (List<?>) world.getEntitiesOfClass(Entity.class, new AABB(x - r, y - r, z - r, x + r, y + r, z + r), predicate);
		return entities;
	}

	public static void registerTransferFromSparksAround(@Nullable ManaSpark spark, Level level, BlockPos worldPosition) {
		if (spark == null) {
			return;
		}
		var otherSparks = getSparksAround(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, spark.getNetwork());
		for (var otherSpark : otherSparks) {
			if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
				otherSpark.registerTransfer(spark);
			}
		}
	}

	/**
	 * Gets the Spark that is attached to this block. The default implementation is
	 * to check for Spark entities above using world.getEntitiesWithinAABB()
	 */
	@Nullable
	public static ManaSpark getAttachedSpark(Level level, BlockPos blockPos) {
		BlockPos sparkPos = blockPos.above();
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(sparkPos),
				entity -> entity instanceof ManaSpark && sparkPos.equals(entity.blockPosition()));
		if (sparks.size() == 1) {
			Entity e = sparks.getFirst();
			return (ManaSpark) e;
		}

		return null;
	}
}
