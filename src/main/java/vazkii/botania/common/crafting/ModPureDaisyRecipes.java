/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModPureDaisyRecipes {
	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("livingrock"), Tags.Blocks.STONE, ModBlocks.livingrock.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("livingwood"), BlockTags.LOGS, ModBlocks.livingwood.getDefaultState()));

		evt.pureDaisy().accept(new RecipePureDaisy(prefix("cobblestone"), Tags.Blocks.NETHERRACK, Blocks.COBBLESTONE.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("sand"), Blocks.SOUL_SAND, Blocks.SAND.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("packed_ice"), Blocks.ICE, Blocks.PACKED_ICE.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("blue_ice"), Blocks.PACKED_ICE, Blocks.BLUE_ICE.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("obsidian"), ModBlocks.blazeBlock, Blocks.OBSIDIAN.getDefaultState()));
		evt.pureDaisy().accept(new RecipePureDaisy(prefix("snow_block"), Blocks.WATER, Blocks.SNOW_BLOCK.getDefaultState()));
	}
}
