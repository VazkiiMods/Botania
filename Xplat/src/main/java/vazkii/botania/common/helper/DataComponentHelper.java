/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.helper;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.common.component.SingleItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Collection;
import java.util.Optional;

public final class DataComponentHelper {
	/**
	 * Sets an Integer component of the ItemStack to the specified value.
	 * If the value is zero, that component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static void setIntNonZero(ItemStack stack, DataComponentType<Integer> component, int value) {
		if (value == 0) {
			stack.remove(component);
		} else {
			stack.set(component, value);
		}
	}

	/**
	 * Sets an Integer component of the ItemStack to the specified value.
	 * If the value is negative, that component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static void setIntNonNegative(ItemStack stack, DataComponentType<Integer> component, int value) {
		if (value < 0) {
			stack.remove(component);
		} else {
			stack.set(component, value);
		}
	}

	/**
	 * Ensures a {@link Unit} component is present on the ItemStack if the value is true,
	 * otherwise ensures that component is not present.
	 */
	@Contract(mutates = "param1")
	public static void setFlag(ItemStack stack, DataComponentType<Unit> component, boolean value) {
		if (value) {
			stack.set(component, Unit.INSTANCE);
		} else {
			stack.remove(component);
		}
	}

	/**
	 * Sets a component of the ItemStack to a value. If that value is null, the component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static <T> void setOptional(ItemStack stack, DataComponentType<? super T> component, @Nullable T value) {
		if (value == null) {
			stack.remove(component);
		} else {
			stack.set(component, value);
		}
	}

	/**
	 * Sets a component of the ItemStack to a non-default value.
	 * If the value is null or equal to the specified default value, the component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static <T> void setUnlessDefault(ItemStack stack, DataComponentType<? super T> component, @Nullable T value, T defaultValue) {
		if (value == null || value.equals(defaultValue)) {
			stack.remove(component);
		} else {
			stack.set(component, value);
		}
	}

	/**
	 * Sets a collection component of the ItemStack to the specified collection.
	 * If that collection is null or empty, the component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static <C extends Collection<?>> void setNonEmpty(ItemStack stack, DataComponentType<C> component, @Nullable C collection) {
		if (collection == null || collection.isEmpty()) {
			stack.remove(component);
		} else {
			stack.set(component, collection);
		}
	}

	/**
	 * Sets an ItemStack component of the stack to the specified value.
	 * If that value is null or an empty item stack, the component is removed instead.
	 */
	@Contract(mutates = "param1")
	public static void setNonEmpty(ItemStack stack, DataComponentType<SingleItem> component, ItemStack item) {
		if (item.isEmpty()) {
			stack.remove(component);
		} else {
			SingleItem value = new SingleItem(item);
			stack.set(component, value);
		}
	}

	/**
	 * Returns a potentially empty ItemStack for a single-item component.
	 */
	@Contract(pure = true)
	public static ItemStack getSingleItem(ItemStack stack, DataComponentType<SingleItem> component) {
		return Optional.ofNullable(stack.get(component)).map(SingleItem::item).orElse(ItemStack.EMPTY);
	}

	/**
	 * Returns the fullness of the mana item:
	 * 0 if empty, 1 if partially full, 2 if full.
	 */
	@Contract(pure = true)
	public static int getFullness(ManaItem item) {
		int mana = item.getMana();
		if (mana == 0) {
			return 0;
		} else if (mana == item.getMaxMana()) {
			return 2;
		} else {
			return 1;
		}
	}

	@Contract(pure = true)
	public static ItemStack duplicateAndClearMana(ItemStack stack) {
		ItemStack copy = stack.copy();
		ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(copy);
		if (manaItem != null) {
			manaItem.addMana(-manaItem.getMana());
		}
		return copy;
	}

	/**
	 * Checks if two items are the same and have the same NBT. If they are `IManaItems`, their mana property is matched
	 * on whether they are empty, partially full, or full.
	 */
	@Contract(pure = true)
	public static boolean matchTagAndManaFullness(ItemStack stack1, ItemStack stack2) {
		if (!ItemStack.isSameItem(stack1, stack2)) {
			return false;
		}
		ManaItem manaItem1 = XplatAbstractions.INSTANCE.findManaItem(stack1);
		ManaItem manaItem2 = XplatAbstractions.INSTANCE.findManaItem(stack2);
		if (manaItem1 != null && manaItem2 != null) {
			if (getFullness(manaItem1) != getFullness(manaItem2)) {
				return false;
			} else {
				return ItemStack.matches(duplicateAndClearMana(stack1), duplicateAndClearMana(stack2));
			}
		}
		return ItemStack.isSameItemSameComponents(stack1, stack2);
	}

	/**
	 * Increases the current rarity of the stack by a tier.
	 */
	public static void bumpRarity(ItemStack stack) {
		stack.set(DataComponents.RARITY, getNextHigherRarity(stack.getRarity()));
	}

	/**
	 * Returns the next-higher tier for the specified rarity. Returns the highest tier, if it's already the highest
	 * tier.
	 */
	@Contract(pure = true)
	public static Rarity getNextHigherRarity(Rarity rarity) {
		return switch (rarity) {
			case COMMON -> Rarity.UNCOMMON;
			case UNCOMMON -> Rarity.RARE;
			default -> Rarity.EPIC;
		};
	}
}
