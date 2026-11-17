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

public final class ItemRegistrationNoContext<A> {
	private final Provider<A> provider;
	private final ItemLike @Nullable [] items;
	private final @Nullable Predicate<ItemStack> predicate;

	private ItemRegistrationNoContext(Provider<A> provider, ItemLike @Nullable [] items, @Nullable Predicate<ItemStack> predicate) {
		this.provider = provider;
		this.items = items;
		this.predicate = predicate;
	}

	public static <A> ItemRegistrationNoContext<A> forItems(Provider<A> provider, ItemLike... items) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(items);

		if (items.length == 0) {
			throw new IllegalArgumentException("No items specified");
		}
		return new ItemRegistrationNoContext<>(provider, items, null);
	}

	public static <A> ItemRegistrationNoContext<A> forItemPredicate(Provider<A> provider, Predicate<ItemStack> predicate) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(predicate);

		return new ItemRegistrationNoContext<>(provider, null, predicate);
	}

	public void apply(BiConsumer<Provider<A>, ItemLike[]> listConsumer,
			BiConsumer<Provider<A>, Predicate<ItemStack>> predicateConsumer) {
		if (this.items != null) {
			listConsumer.accept(this.provider, this.items);
		} else if (this.predicate != null) {
			predicateConsumer.accept(this.provider, this.predicate);
		}
	}

	@FunctionalInterface
	public interface Provider<A> {
		@Nullable
		A getApi(ItemStack stack);

		@Nullable
		default <C> A getApi(ItemStack stack, @SuppressWarnings("unused") @Nullable C context) {
			return getApi(stack);
		}

		default ItemRegistrationNoContext.Provider<A> withPredicate(Predicate<ItemStack> predicate) {
			return (stack) -> {
				if (!predicate.test(stack)) {
					return null;
				}
				return ItemRegistrationNoContext.Provider.this.getApi(stack);
			};
		}
	}
}
