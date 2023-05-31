/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;

import java.util.function.Consumer;

public class SmeltingProvider extends BotaniaRecipeProvider {
	public SmeltingProvider(PackOutput packOutput) {
		super(packOutput);
	}

	private static InventoryChangeTrigger.TriggerInstance conditionsFromItem(ItemLike item) {
		return CraftingRecipeProvider.conditionsFromItem(item);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneForest),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneForest, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneForest))
				.save(consumer, "botania:smelting/metamorphic_forest_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestonePlains),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStonePlains, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestonePlains))
				.save(consumer, "botania:smelting/metamorphic_plains_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneMountain),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneMountain, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneMountain))
				.save(consumer, "botania:smelting/metamorphic_mountain_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneFungal),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneFungal, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneFungal))
				.save(consumer, "botania:smelting/metamorphic_fungal_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneSwamp),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneSwamp, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneSwamp))
				.save(consumer, "botania:smelting/metamorphic_swamp_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneDesert),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneDesert, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneDesert))
				.save(consumer, "botania:smelting/metamorphic_desert_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneTaiga),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneTaiga, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneTaiga))
				.save(consumer, "botania:smelting/metamorphic_taiga_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneMesa),
				RecipeCategory.BUILDING_BLOCKS, BotaniaFluffBlocks.biomeStoneMesa, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneMesa))
				.save(consumer, "botania:smelting/metamorphic_mesa_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaBlocks.livingrockBrick),
				RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.livingrockBrickCracked, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrockBrick))
				.save(consumer, "botania:smelting/cracked_livingrock_bricks");
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
