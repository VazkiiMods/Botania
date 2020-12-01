/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

@Environment(EnvType.CLIENT)
public abstract class BotaniaRecipeDisplay<T extends Recipe<Inventory>> implements RecipeDisplay {
	protected T recipe;
	protected List<List<EntryStack>> inputs;
	protected List<EntryStack> outputs;

	public BotaniaRecipeDisplay(T recipe) {
		this.recipe = recipe;
		this.inputs = EntryStack.ofIngredients(recipe.getPreviewInputs());
		this.outputs = Collections.singletonList(EntryStack.create(recipe.getOutput()));
	}

	@Override
	public @NotNull List<List<EntryStack>> getInputEntries() {
		return this.inputs;
	}

	abstract public int getManaCost();

	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(this.outputs);
	}

	@Override
	public @NotNull Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.recipe).map(T::getId);
	}
}
