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

import net.minecraft.block.Blocks;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibOreDict;

public final class ModPureDaisyRecipes {

	public static void init() {
		BotaniaAPI.registerPureDaisyRecipe(Tags.Blocks.STONE, ModBlocks.livingrock.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(BlockTags.LOGS, ModBlocks.livingwood.getDefaultState());

		BotaniaAPI.registerPureDaisyRecipe(Blocks.NETHERRACK, Blocks.COBBLESTONE.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(Blocks.SOUL_SAND, Blocks.SAND.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(Blocks.ICE, Blocks.PACKED_ICE.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(Blocks.PACKED_ICE, Blocks.BLUE_ICE.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(ModBlocks.blazeBlock, Blocks.OBSIDIAN.getDefaultState());
		BotaniaAPI.registerPureDaisyRecipe(Blocks.WATER, Blocks.SNOW.getDefaultState());
	}

}
