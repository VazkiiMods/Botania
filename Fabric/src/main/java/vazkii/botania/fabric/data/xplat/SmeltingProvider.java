/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.fabric.data.xplat;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.data.util.BotaniaRecipeHelper.deriveRecipeId;

public class SmeltingProvider extends BotaniaRecipeProvider {
	public SmeltingProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	private static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(ItemLike item) {
		return CraftingRecipeProvider.conditionsFromItem(item);
	}

	@Override
	public void buildRecipes(RecipeOutput recipeOutput) {
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_FUCHSITE, BotaniaBlocks.FUCHSITE);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_TALC, BotaniaBlocks.TALC);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_GNEISS, BotaniaBlocks.GNEISS);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_MYCELITE, BotaniaBlocks.MYCELITE);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_CATACLASITE, BotaniaBlocks.CATACLASITE);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_SOLITE, BotaniaBlocks.SOLITE);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_LUNITE, BotaniaBlocks.LUNITE);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.COBBLED_ROSY_TALC, BotaniaBlocks.ROSY_TALC);
		makeDefaultSmeltingRecipe(recipeOutput, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.CRACKED_LIVINGROCK_BRICKS);

		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {
			Block base = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BLOCK_SUFFIX));
			Block smooth = BuiltInRegistries.BLOCK.get(
					botaniaRL(LibBlockNames.SMOOTH_PREFIX + variant + LibBlockNames.BLOCK_SUFFIX));
			makeDefaultSmeltingRecipe(recipeOutput, base, smooth);
		}
	}

	private static void makeDefaultSmeltingRecipe(RecipeOutput consumer, Block input, Block output) {
		SimpleCookingRecipeBuilder
				.smelting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(input))
				.save(consumer, deriveRecipeId(RecipeType.SMELTING, output));
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
