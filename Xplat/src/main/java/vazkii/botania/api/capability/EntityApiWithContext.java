package vazkii.botania.api.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;

/**
 * Abstract ID for a Botania entity capability API with a context type.
 *
 * @param <A> Type of the API.
 * @param <C> Type of the context.
 */
public final class EntityApiWithContext<A, C> extends ApiIdEntity<A> {
	private final Class<C> contextClass;

	public EntityApiWithContext(ResourceLocation id, Class<A> apiClass, Class<C> contextClass) {
		super(id, apiClass);
		this.contextClass = contextClass;
	}

	@Nullable
	public A find(Entity entity, @Nullable C context) {
		return BotaniaAPI.instance().findEntityApi(this, entity, context);
	}

	public Class<C> getContextClass() {
		return contextClass;
	}
}
