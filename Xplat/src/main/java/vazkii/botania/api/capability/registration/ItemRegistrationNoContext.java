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

public final class ItemRegistrationNoContext<A> {
	private final Provider<A> provider;
	private final ItemLike[] items;

	private ItemRegistrationNoContext(Provider<A> provider, ItemLike[] items) {
		this.provider = provider;
		this.items = items;
	}

	public static <A> ItemRegistrationNoContext<A> forItems(Provider<A> provider, ItemLike... items) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(items);

		if (items.length == 0) {
			throw new IllegalArgumentException("No items specified");
		}
		return new ItemRegistrationNoContext<>(provider, items);
	}

	public void apply(BiConsumer<Provider<A>, ItemLike[]> listConsumer) {
		listConsumer.accept(this.provider, this.items);
	}

	@FunctionalInterface
	public interface Provider<A> {
		@Nullable
		A getApi(ItemStack stack);

		@Nullable
		default <C> A getApi(ItemStack stack, @SuppressWarnings("unused") @Nullable C context) {
			return getApi(stack);
		}
	}
}
