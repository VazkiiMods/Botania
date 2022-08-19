/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 15, 2014, 6:09:38 PM (GMT)]
 */
package vazkii.botania.client.integration.nei;

import vazkii.botania.client.gui.crafting.GuiCraftingHalo;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class NEIGuiHooks {

	public static void init() {
		API.registerGuiOverlay(GuiCraftingHalo.class, "crafting");
		API.registerGuiOverlayHandler(GuiCraftingHalo.class, new DefaultOverlayHandler(), "crafting");
	}

}
