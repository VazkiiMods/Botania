/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.capability.BlockApiNoContext;

import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * A block entity with this capability can have a mana spark attached to it.
 * For the Spark to be allowed to have upgrades, the same block position must also have an ManaPool capability
 */
public interface SparkAttachable {

	ResourceLocation ID = botaniaRL("spark_attachable");
	BlockApiNoContext<SparkAttachable> LOOKUP = new BlockApiNoContext<>(ID, SparkAttachable.class);

	/**
	 * Can this block have a Spark attached to it. Note that this will not
	 * unattach the Spark if it's changed later.
	 */
	boolean canAttachSpark(ItemStack stack);

	/**
	 * Called when the Spark is attached.
	 */
	default void attachSpark(ManaSpark entity) {}

	/**
	 * Returns how much space for mana is available in this block, normally the total - the current.
	 * Should NEVER return negative values. Make sure to check against that.
	 */
	int getAvailableSpaceForMana();

	/**
	 * Gets the Spark that is attached to this block. The default implementation is
	 * to check for Spark entities above using world.getEntitiesWithinAABB()
	 */
	@Nullable
	static ManaSpark getAttachedSpark(Level level, BlockPos blockPos) {
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(blockPos.above()), Predicates.instanceOf(ManaSpark.class));
		if (sparks.size() == 1) {
			Entity e = sparks.getFirst();
			return (ManaSpark) e;
		}

		return null;
	}

	/**
	 * Return true if this Tile no longer requires mana and all Sparks
	 * transferring mana to it should cancel their transfer.
	 */
	boolean areIncomingTransfersDone();
}
