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

import net.minecraft.init.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibOreDict;

public final class ModPureDaisyRecipes {

	public static void init() {
		BotaniaAPI.registerPureDaisyRecipe("stone", ModBlocks.livingrock.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe("logWood", ModBlocks.livingwood.getDefaultState());

		BotaniaAPI.registerPureDaisyRecipe("netherrack", Blocks.COBBLESTONE.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe("soulSand", Blocks.SAND.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe("ice", Blocks.PACKED_ICE.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(LibOreDict.BLAZE_BLOCK, Blocks.OBSIDIAN.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(Blocks.WATER, Blocks.SNOW.getDefaultState());
	}

}
