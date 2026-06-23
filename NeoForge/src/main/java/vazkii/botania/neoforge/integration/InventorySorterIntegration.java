/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.InterModComms;

import vazkii.botania.common.item.BotaniaItems;

public class InventorySorterIntegration {
	public static void sendImc() {
		// Botania issue 4068, cpw/InventorySorter issue 139
		InterModComms.sendTo("inventorysorter", "containerblacklist",
				() -> BuiltInRegistries.MENU.getKey(BotaniaItems.COLORED_CONTENTS_POUCH_MENU_TYPE));
	}
}
