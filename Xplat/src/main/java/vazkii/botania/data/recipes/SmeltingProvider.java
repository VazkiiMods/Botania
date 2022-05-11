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

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;

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
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneForest), ModFluffBlocks.biomeStoneForest, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneForest))
				.save(consumer, "botania:smelting/metamorphic_forest_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestonePlains), ModFluffBlocks.biomeStonePlains, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestonePlains))
				.save(consumer, "botania:smelting/metamorphic_plains_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneMountain), ModFluffBlocks.biomeStoneMountain, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneMountain))
				.save(consumer, "botania:smelting/metamorphic_mountain_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneFungal), ModFluffBlocks.biomeStoneFungal, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneFungal))
				.save(consumer, "botania:smelting/metamorphic_fungal_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneSwamp), ModFluffBlocks.biomeStoneSwamp, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneSwamp))
				.save(consumer, "botania:smelting/metamorphic_swamp_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneDesert), ModFluffBlocks.biomeStoneDesert, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneDesert))
				.save(consumer, "botania:smelting/metamorphic_desert_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneTaiga), ModFluffBlocks.biomeStoneTaiga, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneTaiga))
				.save(consumer, "botania:smelting/metamorphic_taiga_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModFluffBlocks.biomeCobblestoneMesa), ModFluffBlocks.biomeStoneMesa, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneMesa))
				.save(consumer, "botania:smelting/metamorphic_mesa_stone");
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.livingrockBrick), ModBlocks.livingrockBrickCracked, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ModBlocks.livingrockBrick))
				.save(consumer, "botania:smelting/cracked_livingrock_bricks");
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
