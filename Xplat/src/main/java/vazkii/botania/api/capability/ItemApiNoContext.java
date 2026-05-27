package vazkii.botania.api.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania item capability API without context type.
 *
 * @param <A> Type of the API.
 */
public final class ItemApiNoContext<A> extends ApiIdItem<A> {
	public ItemApiNoContext(ResourceLocation id, Class<A> apiClass) {
		super(id, apiClass);
	}

	@Nullable
	public A find(ItemStack stack) {
		return BotaniaAPI.instance().findItemApi(this, stack);
	}
}
