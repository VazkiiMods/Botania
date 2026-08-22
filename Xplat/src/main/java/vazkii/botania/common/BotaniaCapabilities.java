/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common;

import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.common.capabilities.BlockCapabilities;
import vazkii.botania.common.capabilities.ItemCapabilities;

/**
 * Central entry point for Botania's own common capability API registration logic.
 *
 * @implNote Botania's only entity capabilities are client-side only, so no calls for that happen here.
 * @see vazkii.botania.client.BotaniaClientCapabilities
 */
public final class BotaniaCapabilities {

	/**
	 * Defines the capability/API types. Should be called before Botania or any add-on attempts to use these.
	 *
	 * @implNote On NeoForge this happens during the highest priority RegisterCapabilitiesEvent.
	 */
	public static void registerCapabilityTypes(ApiIdRegistration registration) {
		ItemCapabilities.registerLookups(registration);
		BlockCapabilities.registerLookups(registration);
	}

	/**
	 * Registers regular capability providers, i.e. anything with a fixed set of target objects.
	 */
	public static void registerCapabilityProviders(ApiProviderRegistration registration) {
		ItemCapabilities.registerProviders(registration);
		BlockCapabilities.registerProviders(registration);
	}

	/**
	 * Registers "fallback" capability providers, i.e. anything where we can't define the exact set of target objects,
	 * e.g. because we don't know them or add-ons might register more specific providers we don't want to affect.
	 *
	 * @implNote On NeoForge this happens during the lowest priority RegisterCapabilitiesEvent,
	 *           while on Fabric this is implemented using the various registerFallback methods.
	 */
	public static void registerCapabilityFallbackProviders(ApiProviderRegistration registration) {
		BlockCapabilities.registerFallbackProviders(registration);
	}

	private BotaniaCapabilities() {}
}
