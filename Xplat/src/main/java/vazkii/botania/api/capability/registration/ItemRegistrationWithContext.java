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

public final class ItemRegistrationWithContext<A, C> {
	private final Provider<A, C> provider;
	private final ItemLike[] items;

	private ItemRegistrationWithContext(Provider<A, C> provider, ItemLike... items) {
		this.provider = provider;
		this.items = items;
	}

	public static <A, C> ItemRegistrationWithContext<A, C> forItems(Provider<A, C> provider,
			ItemLike... items) {

		Objects.requireNonNull(provider);
		Objects.requireNonNull(items);

		if (items.length == 0) {
			throw new IllegalArgumentException("No items specified");
		}
		return new ItemRegistrationWithContext<>(provider, items);
	}

	public void apply(BiConsumer<Provider<A, C>, ItemLike[]> listConsumer) {
		listConsumer.accept(this.provider, this.items);
	}

	@FunctionalInterface
	public interface Provider<A, C> {
		@Nullable
		A getApi(ItemStack stack, C context);
	}
}
