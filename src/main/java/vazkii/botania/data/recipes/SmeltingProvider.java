/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.Ingredient;
import vazkii.botania.common.block.ModFluffBlocks;

import java.util.function.Consumer;

/*
public class SmeltingProvider extends RecipesProvider {
	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void generate(Consumer<RecipeJsonProvider> consumer) {
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneForest), ModFluffBlocks.biomeStoneForest, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneForest))
				.offerTo(consumer, "botania:smelting/metamorphic_forest_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestonePlains), ModFluffBlocks.biomeStonePlains, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestonePlains))
				.offerTo(consumer, "botania:smelting/metamorphic_plains_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneMountain), ModFluffBlocks.biomeStoneMountain, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneMountain))
				.offerTo(consumer, "botania:smelting/metamorphic_mountain_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneFungal), ModFluffBlocks.biomeStoneFungal, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneFungal))
				.offerTo(consumer, "botania:smelting/metamorphic_fungal_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneSwamp), ModFluffBlocks.biomeStoneSwamp, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneSwamp))
				.offerTo(consumer, "botania:smelting/metamorphic_swamp_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneDesert), ModFluffBlocks.biomeStoneDesert, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneDesert))
				.offerTo(consumer, "botania:smelting/metamorphic_desert_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneTaiga), ModFluffBlocks.biomeStoneTaiga, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneTaiga))
				.offerTo(consumer, "botania:smelting/metamorphic_taiga_stone");
		CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ModFluffBlocks.biomeCobblestoneMesa), ModFluffBlocks.biomeStoneMesa, 0.1f, 200)
				.criterion("has_item", conditionsFromItem(ModFluffBlocks.biomeCobblestoneMesa))
				.offerTo(consumer, "botania:smelting/metamorphic_mesa_stone");
	}

	@Override
	public String getName() {
		return "Botania smelting recipes";
	}
}
*/
