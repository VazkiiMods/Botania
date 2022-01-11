/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.integration.rei;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BotaniaRecipeDisplay<T extends Recipe<Container>> implements Display {
	protected final T recipe;
	protected List<EntryIngredient> inputs;
	protected EntryIngredient outputs;

	public BotaniaRecipeDisplay(T recipe) {
		this.recipe = recipe;
		this.inputs = EntryIngredients.ofIngredients(recipe.getIngredients());
		this.outputs = EntryIngredients.of(recipe.getResultItem());
	}

	@Override
	public @NotNull List<EntryIngredient> getInputEntries() {
		return this.inputs;
	}

	abstract public int getManaCost();

	@Override
	public @NotNull List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(this.outputs);
	}

	@Override
	public @NotNull Optional<ResourceLocation> getDisplayLocation() {
		return Optional.ofNullable(this.recipe).map(T::getId);
	}
}
