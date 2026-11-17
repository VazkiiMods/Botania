/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania item capability API with a context type.
 *
 * @param <A> Type of the API.
 * @param <C> Type of the context.
 */
public final class ItemApiWithContext<A, C> extends ApiIdItem<A> {
	private final Class<C> contextClass;

	public ItemApiWithContext(ResourceLocation id, Class<A> apiClass, Class<C> contextClass) {
		super(id, apiClass);
		this.contextClass = contextClass;
	}

	@Nullable
	public A find(ItemStack stack, @Nullable C context) {
		return BotaniaAPI.instance().findItemApi(this, stack, context);
	}

	public Class<C> getContextClass() {
		return contextClass;
	}
}
