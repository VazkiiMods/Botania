/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.recipes.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.common.crafting.recipe.TiaraWingsRecipe;
import vazkii.botania.common.item.BotaniaItems;

import java.util.LinkedHashMap;
import java.util.Map;

public class TiaraWingsRecipeBuilder {

	private final RecipeCategory category;
	private final Ingredient material;
	private final int variant;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	public TiaraWingsRecipeBuilder(RecipeCategory category, Ingredient material, int variant) {
		this.category = category;
		this.material = material;
		this.variant = variant;
	}

	public static TiaraWingsRecipeBuilder with(Ingredient material, int variant) {
		return new TiaraWingsRecipeBuilder(RecipeCategory.TOOLS, material, variant);
	}

	public TiaraWingsRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		ResourceLocation tiaraId = BuiltInRegistries.ITEM.getKey(BotaniaItems.FLUEGEL_TIARA);
		ResourceLocation id = tiaraId.withSuffix("_" + variant);
		Advancement.Builder builder = recipeOutput.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(builder::addCriterion);
		TiaraWingsRecipe recipe = new TiaraWingsRecipe(RecipeBuilder.determineBookCategory(category), material, variant);
		recipeOutput.accept(id, recipe, builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}
}
