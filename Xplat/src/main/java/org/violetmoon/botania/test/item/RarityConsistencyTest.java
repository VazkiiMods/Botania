/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.test.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.api.recipe.BotanicalBreweryRecipe;
import vazkii.botania.api.recipe.ElvenTradeRecipe;
import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.LexiconElvenTradeRecipe;
import vazkii.botania.test.TestingUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Tests the consistency of recipe output item rarities using several rules.
 */
public class RarityConsistencyTest {
	private static final Set<RecipeType<?>> RARITY_UPGRADE_TYPES = Set.of(BotaniaRecipeTypes.TERRA_PLATE_TYPE);
	private static final Set<RecipeSerializer<?>> RARITY_UPGRADE_SERIALIZERS = Set.of(LexiconElvenTradeRecipe.SERIALIZER);
	private static final List<Rarity> ORDERED_RARITIES = List.of(Rarity.values());
	private static final Map<Item, Rarity> RARITY_OVERRIDES = Map.of(
			Items.GOLDEN_APPLE, Rarity.COMMON
	);

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE, timeoutTicks = 1)
	public void checkRecipeOutputConsistency(GameTestHelper helper) {
		boolean failedAnyChecks = false;
		Set<Item> craftableItems = new HashSet<>();
		for (RecipeHolder<?> recipeHolder : helper.getLevel().getRecipeManager().getRecipes()) {
			Rarity highestIngredientRarity = Rarity.COMMON;
			Set<ItemStack> highestRarityIngredients = new LinkedHashSet<>();
			Recipe<?> recipe = recipeHolder.value();
			Collection<ItemStack> resultItems = getResultItems(helper, recipe);
			if (resultItems.isEmpty() && recipeHolder.id().getNamespace().equals(BotaniaAPI.MODID)) {
				BotaniaAPI.LOGGER.warn("Empty recipe output for {} ({})", recipeHolder, recipe.getType());
			}
			for (ItemStack resultItem : resultItems) {
				craftableItems.add(resultItem.getItem());

				if (!resultItem.getItemHolder().unwrapKey().orElseThrow().location().getNamespace()
						.equals(BotaniaAPI.MODID)) {
					// only check recipes that make Botania items
					continue;
				}
				for (Ingredient ingredient : recipe.getIngredients()) {
					ItemStack[] items = ingredient.getItems();
					Rarity ingredientRarity = Stream.of(items)
							.map(this::getRarity)
							.min(Comparator.naturalOrder())
							.orElse(Rarity.COMMON);
					int rarityDifference = ingredientRarity.compareTo(highestIngredientRarity);
					if (rarityDifference > 0) {
						highestIngredientRarity = ingredientRarity;
						highestRarityIngredients.clear();
						highestRarityIngredients.addAll(Arrays.asList(items));
					} else if (rarityDifference == 0) {
						highestRarityIngredients.addAll(Arrays.asList(items));
					}
				}
				Rarity outputRarity = resultItem.getRarity();
				int rarityDifference = compareRarities(outputRarity, highestIngredientRarity);

				if (rarityDifference !=
						(RARITY_UPGRADE_TYPES.contains(recipe.getType()) ||
								RARITY_UPGRADE_SERIALIZERS.contains(recipe.getSerializer()) ? 1 : 0)) {
					BotaniaAPI.LOGGER.error(
							"Item {} obtained from recipe {} ({}) has rarity {} while the highest ingredient rarity is {} ({})",
							resultItem.getItem(), recipeHolder, recipe.getType(), outputRarity, highestIngredientRarity,
							highestRarityIngredients);
					failedAnyChecks = true;
				} else if (rarityDifference != 0) {
					BotaniaAPI.LOGGER.info("Rarity of {} is changed by recipe {} ({}) to {} from {} ({})",
							resultItem.getItem(), recipeHolder, recipe.getType(), outputRarity, highestIngredientRarity,
							highestRarityIngredients);
				}
			}
		}

		for (Item item : BuiltInRegistries.ITEM) {
			if (craftableItems.contains(item)) {
				continue;
			}
			if (!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(BotaniaAPI.MODID)) {
				continue;
			}

			if (new ItemStack(item).getRarity() == Rarity.COMMON) {
				BotaniaAPI.LOGGER.warn("Uncraftable common item: {}", item);
				//failedAnyChecks = true;
			}
		}

		helper.assertFalse(failedAnyChecks, "There are inconsistent rarities. See log for details.");
		helper.succeed();
	}

	private Rarity getRarity(ItemStack stack) {
		return RARITY_OVERRIDES.getOrDefault(stack.getItem(), stack.getRarity());
	}

	private static Collection<ItemStack> getResultItems(GameTestHelper helper, Recipe<?> recipe) {
		switch (recipe) {
			case ElvenTradeRecipe tradeRecipe -> {
				return tradeRecipe.getOutputs();
			}
			case OrechidRecipe orechidRecipe -> {
				return orechidRecipe.getOutput().getDisplayedStacks();
			}
			case BotanicalBreweryRecipe brewRecipe -> {
				return BuiltInRegistries.ITEM.stream()
						.filter(BrewContainer.class::isInstance)
						.filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(BotaniaAPI.MODID))
						.map(ItemStack::new)
						.map(brewRecipe::getOutput)
						.toList();
			}
			case PureDaisyRecipe pureDaisyRecipe -> {
				return pureDaisyRecipe.getOutput().getDisplayedStacks();
			}
			default -> {
				ItemStack singleResultItem = recipe.getResultItem(helper.getLevel().registryAccess());
				if (singleResultItem.isEmpty()) {
					return List.of();
				}
				return List.of(singleResultItem);
			}
		}
	}

	private static int compareRarities(Rarity a, Rarity b) {
		return ORDERED_RARITIES.indexOf(a) - ORDERED_RARITIES.indexOf(b);
	}
}
