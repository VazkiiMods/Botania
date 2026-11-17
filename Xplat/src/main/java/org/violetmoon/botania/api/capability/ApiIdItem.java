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

import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract parent class for a Botania item capability API identifier.
 *
 * @param <A> Type of the API.
 */
@ApiStatus.NonExtendable
public abstract class ApiIdItem<A> extends ApiIdBase<A> {
	protected ApiIdItem(ResourceLocation id, Class<A> apiClass) {
		super(id, apiClass);
	}
}
