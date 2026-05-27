package vazkii.botania.api.capability;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract parent class for a Botania entity capability API identifier.
 *
 * @param <A> Type of the API.
 */
@ApiStatus.NonExtendable
public abstract class ApiIdEntity<A> extends ApiIdBase<A> {
	protected ApiIdEntity(ResourceLocation id, Class<A> apiClass) {
		super(id, apiClass);
	}
}
