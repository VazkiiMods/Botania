/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.helper;

import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class DelayHelper {
	public static final int FUNCTIONAL_INHERENT_DELAY = 60;
	public static final int GENERATING_INHERENT_DELAY = FUNCTIONAL_INHERENT_DELAY - 1;
	public static final int RESULT_ITEM_MOVE_DELAY = 5;

	/**
	 * Like {@link #canInteractWith}, but does not use inherent delay
	 */
	public static boolean canInteractWithImmediate(SpecialFlowerBlockEntity flower, ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty()
				&& XplatAbstractions.instance().getItemLifeTime(item) > flower.getModulatedDelay();
	}

	/**
	 * Whether the given flower can act on the given item, taking into account inherent delay and modulating delay.
	 */
	public static boolean canInteractWith(SpecialFlowerBlockEntity flower, ItemEntity item) {
		if (!item.isAlive() || item.getItem().isEmpty()) {
			return false;
		}
		int inherentDelay;
		if (flower instanceof FunctionalFlowerBlockEntity) {
			inherentDelay = FUNCTIONAL_INHERENT_DELAY;
		} else if (flower instanceof GeneratingFlowerBlockEntity) {
			inherentDelay = GENERATING_INHERENT_DELAY;
		} else {
			inherentDelay = 0;
		}
		return XplatAbstractions.instance().getItemLifeTime(item) > inherentDelay + flower.getModulatedDelay();
	}

	/**
	 * Whether the specified item may be moved (in the world or from the world into a container) by the specified
	 * flower.
	 */
	public static boolean canMove(SpecialFlowerBlockEntity flower, ItemEntity item) {
		if (XplatAbstractions.instance().preventsRemoteMovement(item)) {
			return false;
		}

		final Optional<ItemSource> source = XplatAbstractions.instance().getItemSource(item);
		if (source.filter(ItemSource::allowsQuickPickup).isPresent()) {
			return XplatAbstractions.instance().getItemLifeTime(item) >= RESULT_ITEM_MOVE_DELAY + flower.getModulatedDelay();
		}
		return canInteractWith(flower, item);
	}

	/**
	 * Converts a bi-predicate for use as predicate in {@code getEntities()} calls by a particular flower.
	 */
	public static Predicate<ItemEntity> asPredicateFor(BiPredicate<SpecialFlowerBlockEntity, ItemEntity> biPredicate,
			SpecialFlowerBlockEntity flower) {
		return item -> biPredicate.test(flower, item);
	}
}
