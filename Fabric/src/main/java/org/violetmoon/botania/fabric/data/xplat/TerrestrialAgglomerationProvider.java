/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package org.violetmoon.botania.fabric.data.xplat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.TerrestrialAgglomerationRecipe;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.lib.ConventionalBotaniaTags;
import org.violetmoon.botania.data.recipes.BotaniaRecipeProvider;
import org.violetmoon.botania.data.util.BotaniaRecipeHelper;

import java.util.concurrent.CompletableFuture;

public class TerrestrialAgglomerationProvider extends BotaniaRecipeProvider {
	public TerrestrialAgglomerationProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public String getName() {
		return "Botania Terra Plate recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		consumer.accept(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.TERRA_PLATE_TYPE,
				BotaniaItems.TERRASTEEL_INGOT),
				new TerrestrialAgglomerationRecipe(
						ManaPoolBlock.MAX_MANA / 2,
						new ItemStack(BotaniaItems.TERRASTEEL_INGOT),
						Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS),
						Ingredient.of(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS),
						Ingredient.of(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				),
				null);
	}
}
