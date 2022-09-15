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
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;

import java.util.function.Consumer;

public class SmeltingProvider extends BotaniaRecipeProvider {
	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	private static InventoryChangeTrigger.TriggerInstance conditionsFromItem(ItemLike item) {
		return RecipeProvider.conditionsFromItem(item);
	}

	@Override
	public void registerRecipes(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneForest), BotaniaFluffBlocks.biomeStoneForest, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneForest))
				.save(consumer, "botania:smelting/metamorphic_forest_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestonePlains), BotaniaFluffBlocks.biomeStonePlains, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestonePlains))
				.save(consumer, "botania:smelting/metamorphic_plains_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneMountain), BotaniaFluffBlocks.biomeStoneMountain, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneMountain))
				.save(consumer, "botania:smelting/metamorphic_mountain_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneFungal), BotaniaFluffBlocks.biomeStoneFungal, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneFungal))
				.save(consumer, "botania:smelting/metamorphic_fungal_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneSwamp), BotaniaFluffBlocks.biomeStoneSwamp, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneSwamp))
				.save(consumer, "botania:smelting/metamorphic_swamp_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneDesert), BotaniaFluffBlocks.biomeStoneDesert, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneDesert))
				.save(consumer, "botania:smelting/metamorphic_desert_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneTaiga), BotaniaFluffBlocks.biomeStoneTaiga, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneTaiga))
				.save(consumer, "botania:smelting/metamorphic_taiga_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaFluffBlocks.biomeCobblestoneMesa), BotaniaFluffBlocks.biomeStoneMesa, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaFluffBlocks.biomeCobblestoneMesa))
				.save(consumer, "botania:smelting/metamorphic_mesa_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BotaniaBlocks.livingrockBrick), BotaniaBlocks.livingrockBrickCracked, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(BotaniaBlocks.livingrockBrick))
				.save(consumer, "botania:smelting/cracked_livingrock_bricks");
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
