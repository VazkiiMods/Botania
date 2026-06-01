/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.common.BotaniaCapabilities;

public final class BotaniaClientCapabilities {
	public static void registerClientCapabilities(BotaniaCapabilities.ApiIdRegistration registration) {
		registration.register(WandHUD.BLOCK_LOOKUP);
		registration.register(WandHUD.ENTITY_LOOKUP);

		registration.register(MonocleHud.BLOCK_LOOKUP);
		registration.register(MonocleHud.ENTITY_LOOKUP);
	}

	private BotaniaClientCapabilities() {}
}
