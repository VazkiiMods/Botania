/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability.registration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class ItemRegistrationWithContext<A, C> {
	private final Provider<A, C> provider;
	private final ItemLike @Nullable [] items;
	private final @Nullable Predicate<ItemStack> predicate;

	private ItemRegistrationWithContext(Provider<A, C> provider, ItemLike @Nullable [] items, @Nullable Predicate<ItemStack> predicate) {
		this.provider = provider;
		this.items = items;
		this.predicate = predicate;
	}

	public static <A, C> ItemRegistrationWithContext<A, C> forItems(Provider<A, C> provider,
			ItemLike... items) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(items);

		if (items.length == 0) {
			throw new IllegalArgumentException("No items specified");
		}
		return new ItemRegistrationWithContext<>(provider, items, null);
	}

	public static <A, C> ItemRegistrationWithContext<A, C> forItemPredicate(Provider<A, C> provider,
			Predicate<ItemStack> predicate) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new ItemRegistrationWithContext<>(provider, null, predicate);
	}

	public void apply(BiConsumer<Provider<A, C>, ItemLike[]> listConsumer,
			BiConsumer<Provider<A, C>, Predicate<ItemStack>> predicateConsumer) {
		if (this.items != null) {
			listConsumer.accept(this.provider, this.items);
		} else if (this.predicate != null) {
			predicateConsumer.accept(this.provider, this.predicate);
		}
	}

	@FunctionalInterface
	public interface Provider<A, C> {
		@Nullable
		A getApi(ItemStack stack, C context);

		default ItemRegistrationWithContext.Provider<A, C> withPredicate(Predicate<ItemStack> predicate) {
			return (stack, context) -> {
				if (!predicate.test(stack)) {
					return null;
				}
				return ItemRegistrationWithContext.Provider.this.getApi(stack, context);
			};
		}
	}
}
