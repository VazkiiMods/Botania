package vazkii.botania.api.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania entity capability API without context type.
 *
 * @param <A> Type of the API.
 */
public final class EntityApiNoContext<A> extends ApiIdEntity<A> {
	public EntityApiNoContext(ResourceLocation id, Class<A> apiClass) {
		super(id, apiClass);
	}

	@Nullable
	public A find(Entity entity) {
		return BotaniaAPI.instance().findEntityApi(this, entity);
	}
}
