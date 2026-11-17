/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.item.SparkEntity;

/**
 * Base interface for block/entity capabilities that allow attaching some form of spark entity.
 * 
 * @param <T> The related spark entity type.
 */
public interface SparkAttachable<T extends SparkEntity> {
	/**
	 * Can this block have a Spark attached to it. Note that this will not
	 * unattach the Spark if it's changed later.
	 */
	boolean canAttachSpark(ItemStack stack);

	/**
	 * Called when a Spark is attached.
	 */
	default void attachSpark(T entity) {}

	/**
	 * Tests whether an attached spark is allowed to be upgraded with the specified augment.
	 * 
	 * @param augment The spark augment item stack.
	 * @return <code>true</code> to allow the augment item, <code>false</code> to deny it.
	 */
	default boolean canHaveAugment(ItemStack augment) {
		return false;
	}
}
