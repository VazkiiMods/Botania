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
 * Abstract parent class for a Botania capability API identifier.
 * 
 * @param <A> Type of the capability API.
 * @apiNote Each of Botania's capability API interfaces is supposed to provide one or more singleton instance(s) as
 *          lookup helper for an implementation of that capability API.
 *          For example, the Wand HUD capability defines one for blocks and another for entities.
 * @implNote Direct subclasses of this class are base classes for certain types of APIs (e.g. block, item, entity),
 *           while subclasses of those define APIs with a lookup method that does or des not require some form of
 *           context information. Lookup of the APIs represented by instances of those sub-subclasses is routed via
 *           {@link vazkii.botania.api.BotaniaAPI} to a platform-specific implementation for Fabric or NeoForge.
 *           Client-side capability APIs receive no special lookup treatment compared to common APIs, except that their
 *           registration happens in client-side initialization logic.
 */
@ApiStatus.NonExtendable
public abstract class ApiIdBase<A> {
	private final ResourceLocation id;
	private final Class<A> apiClass;

	protected ApiIdBase(ResourceLocation id, Class<A> apiClass) {
		this.id = id;
		this.apiClass = apiClass;
	}

	public ResourceLocation getId() {
		return id;
	}

	public Class<A> getApiClass() {
		return apiClass;
	}
}
