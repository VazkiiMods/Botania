/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 17, 2015, 5:18:06 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import vazkii.botania.common.block.ModBlocks;

import vazkii.botania.api.BotaniaAPI;

public final class ModPureDaisyRecipes {

	public static void init() {
		BotaniaAPI.registerPureDaisyRecipe("stone", ModBlocks.livingrock, 0);
		BotaniaAPI.registerPureDaisyRecipe("logWood", ModBlocks.livingwood, 0);
	}
	
}
