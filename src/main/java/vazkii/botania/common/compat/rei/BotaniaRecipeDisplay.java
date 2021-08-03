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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

@Environment(EnvType.CLIENT)
public abstract class BotaniaRecipeDisplay<T extends Recipe<Container>> implements RecipeDisplay {
	protected T recipe;
	protected List<List<EntryStack>> inputs;
	protected List<EntryStack> outputs;

	public BotaniaRecipeDisplay(T recipe) {
		this.recipe = recipe;
		this.inputs = EntryStack.ofIngredients(recipe.getIngredients());
		this.outputs = Collections.singletonList(EntryStack.create(recipe.getResultItem()));
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
	public @NotNull Optional<ResourceLocation> getRecipeLocation() {
		return Optional.ofNullable(this.recipe).map(T::getId);
	}
}
