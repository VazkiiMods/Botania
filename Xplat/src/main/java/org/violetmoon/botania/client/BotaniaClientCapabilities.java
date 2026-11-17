/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client;

import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.client.capabilities.ClientBlockCapabilities;
import vazkii.botania.client.capabilities.ClientEntityCapabilities;

/**
 * Central entry point for Botania's own client-side capability API registration logic.
 *
 * @implNote Botania's item capabilities are all registered in common code, so no calls for that happen here.
 * @see vazkii.botania.common.BotaniaCapabilities
 */
public final class BotaniaClientCapabilities {

	public static void registerCapabilityTypes(ApiIdRegistration registration) {
		ClientBlockCapabilities.registerCapabilityTypes(registration);
		ClientEntityCapabilities.registerCapabilityTypes(registration);
	}

	public static void registerCapabilityProviders(ApiProviderRegistration registration) {
		ClientBlockCapabilities.registerCapabilityProviders(registration);
		ClientEntityCapabilities.registerCapabilityProviders(registration);
	}

	public static void registerCapabilityFallbackProviders(ApiProviderRegistration registration) {
		ClientBlockCapabilities.registerCapabilityFallbackProviders(registration);
		ClientEntityCapabilities.registerCapabilityFallbackProviders(registration);
	}

	private BotaniaClientCapabilities() {}
}
