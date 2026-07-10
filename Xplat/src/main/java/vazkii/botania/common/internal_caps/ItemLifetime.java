/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.api.attachment.DataHolderId;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Automatically attached to all item entities. Denotes the entity's lifetime in ticks as checked for by flowers.
 * The special value {@link #INFINITE} is never incremented automatically every tick and means flowers will never
 * interact with the item entity.
 */
public final class ItemLifetime {
	public static final ResourceLocation ID = botaniaRL("item_lifetime");
	public static final DataHolderId<Short> HOLDER = new DataHolderId<>(ID, Codec.SHORT);
	/**
	 * Special value for infinite lifetime that also implies no flower interactions at all.
	 */
	public static final short INFINITE = Short.MIN_VALUE;

	public static final int FUNCTIONAL_INHERENT_DELAY = 60;
	public static final int GENERATING_INHERENT_DELAY = FUNCTIONAL_INHERENT_DELAY - 1;
	public static final int RESULT_ITEM_MOVE_DELAY = 5;

	public static short get(ItemEntity item) {
		Short lifetime = HOLDER.getFor(item);
		return lifetime != null ? lifetime : 0;
	}

	/**
	 * Sets the item lifetime. Note: Setting a lifetime less than or equal to {@link #INFINITE} is functionally
	 * equivalent to calling {@link #setInfinite(ItemEntity)}.
	 */
	public static void set(ItemEntity item, int lifetime) {
		HOLDER.setFor(item, (short) Math.clamp(lifetime, Short.MIN_VALUE, Short.MAX_VALUE));
	}

	public static void increment(ItemEntity item) {
		short lifetime = HOLDER.getOrDefault(item, (short) 0);
		if (lifetime != INFINITE && lifetime < Short.MAX_VALUE) {
			HOLDER.setFor(item, (short) (lifetime + 1));
		}
	}

	public static boolean isInfinite(ItemEntity item) {
		return get(item) == INFINITE;
	}

	public static void setInfinite(ItemEntity item) {
		HOLDER.setFor(item, INFINITE);
	}

	/**
	 * Like {@link #canInteractWith}, but does not use inherent delay
	 */
	public static boolean canInteractWithImmediate(SpecialFlowerBlockEntity flower, ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty()
				&& get(item) > flower.getModulatedDelay();
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
		return get(item) > inherentDelay + flower.getModulatedDelay();
	}

	/**
	 * Whether the specified item may be moved (in the world or from the world into a container) by the specified
	 * flower.
	 */
	public static boolean canMove(SpecialFlowerBlockEntity flower, ItemEntity item) {
		if (XplatAbstractions.instance().preventsRemoteMovement(item)) {
			return false;
		}

		final ItemSource source = ItemSource.HOLDER.getFor(item);
		if (source != null && source.allowsQuickPickup()) {
			return get(item) >= RESULT_ITEM_MOVE_DELAY + flower.getModulatedDelay();
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

	private ItemLifetime() {}
}
