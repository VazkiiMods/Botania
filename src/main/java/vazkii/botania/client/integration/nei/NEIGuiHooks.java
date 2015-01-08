/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Dec 15, 2014, 6:09:38 PM (GMT)]
 */
package vazkii.botania.client.integration.nei;

import vazkii.botania.client.gui.crafting.GuiCraftingHalo;
import net.minecraft.client.gui.inventory.GuiCrafting;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class NEIGuiHooks {

	public static void init() {
		API.registerGuiOverlay(GuiCraftingHalo.class, "crafting");
		API.registerGuiOverlayHandler(GuiCraftingHalo.class, new DefaultOverlayHandler(), "crafting");
	}
	
}
