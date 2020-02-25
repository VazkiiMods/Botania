/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [2020-02-12, 00:02 (UTC+2)]
 */
package vazkii.botania.data;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.crafting.Ingredient;
import vazkii.botania.common.block.ModFluffBlocks;

import java.util.function.Consumer;

public class SmeltingProvider extends RecipeProvider {
	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneForest), ModFluffBlocks.biomeStoneForest, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneForest))
				.build(consumer, "botania:smelting/metamorphic_forest_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestonePlains), ModFluffBlocks.biomeStonePlains, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestonePlains))
				.build(consumer, "botania:smelting/metamorphic_plains_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneMountain), ModFluffBlocks.biomeStoneMountain, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneMountain))
				.build(consumer, "botania:smelting/metamorphic_mountain_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneFungal), ModFluffBlocks.biomeStoneFungal, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneFungal))
				.build(consumer, "botania:smelting/metamorphic_fungal_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneSwamp), ModFluffBlocks.biomeStoneSwamp, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneSwamp))
				.build(consumer, "botania:smelting/metamorphic_swamp_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneDesert), ModFluffBlocks.biomeStoneDesert, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneDesert))
				.build(consumer, "botania:smelting/metamorphic_desert_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneTaiga), ModFluffBlocks.biomeStoneTaiga, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneTaiga))
				.build(consumer, "botania:smelting/metamorphic_taiga_stone");
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneMesa), ModFluffBlocks.biomeStoneMesa, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneMesa))
				.build(consumer, "botania:smelting/metamorphic_mesa_stone");
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
