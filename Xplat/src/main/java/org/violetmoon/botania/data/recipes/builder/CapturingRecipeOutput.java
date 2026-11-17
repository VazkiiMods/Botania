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

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.xplat.XplatAbstractions;

import java.util.Optional;
import java.util.function.Consumer;

public class CapturingRecipeOutput {
	private final MutableObject<@UnknownNullability ResourceLocation> partialRecipeId = new MutableObject<>();
	private final MutableObject<@UnknownNullability Recipe<?>> partialRecipe = new MutableObject<>();
	private final MutableObject<@Nullable AdvancementHolder> partialAdvancementHolder = new MutableObject<>();
	private final RecipeOutput partialOutput;

	public CapturingRecipeOutput(RecipeOutput recipeOutput) {
		partialOutput = XplatAbstractions.INSTANCE.createRecipeOutput(
				(recipeId, recipe, advancement) -> {
					partialRecipeId.setValue(recipeId);
					partialRecipe.setValue(recipe);
					partialAdvancementHolder.setValue(advancement);
				},
				recipeOutput::advancement);
	}

	public Triple<ResourceLocation, Recipe<?>, Optional<AdvancementHolder>> captureSave(Consumer<RecipeOutput> recipeOutputConsumer) {
		recipeOutputConsumer.accept(partialOutput);

		ResourceLocation recipeId = partialRecipeId.getValue();
		Recipe<?> recipe = partialRecipe.getValue();
		AdvancementHolder advancementHolder = partialAdvancementHolder.getValue();

		partialRecipeId.setValue(null);
		partialRecipe.setValue(null);
		partialAdvancementHolder.setValue(null);

		return Triple.of(recipeId, recipe, Optional.ofNullable(advancementHolder));
	}
}
