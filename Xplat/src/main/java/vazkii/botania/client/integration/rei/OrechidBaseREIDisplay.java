/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.rei;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.OrechidRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class OrechidBaseREIDisplay<T extends OrechidRecipe> implements Display {
	private final List<EntryIngredient> stone;
	private final List<EntryIngredient> ores;
	private final T recipe;

	public OrechidBaseREIDisplay(T recipe) {
		stone = Collections.singletonList(EntryIngredient.of(recipe.getInput().getDisplayedStacks().stream().map(EntryStacks::of).collect(Collectors.toList())));
		ores = Collections.singletonList(EntryIngredient.of(recipe.getOutput().getDisplayedStacks().stream().map(EntryStacks::of).collect(Collectors.toList())));
		this.recipe = recipe;
	}

	@Override
	public @NotNull List<EntryIngredient> getInputEntries() {
		return stone;
	}

	@Override
	public @NotNull List<EntryIngredient> getOutputEntries() {
		return ores;
	}

	public T getRecipe() {
		return recipe;
	}
}
