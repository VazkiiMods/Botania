/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.IOrechidRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public abstract class OrechidBaseREIDisplay<T extends IOrechidRecipe> implements Display {
	private final List<EntryIngredient> stone;
	private final List<EntryIngredient> ores;

	public OrechidBaseREIDisplay(T recipe, int totalWeight) {
		final int myWeight = recipe.getWeight();
		final int amount = Math.max(1, Math.round((float) (myWeight * 64) / totalWeight));

		List<ItemStack> stackList = recipe.getOutput().getDisplayedStacks();
		for (ItemStack stack : stackList) {
			stack.setCount(amount);
		}
		stone = Collections.singletonList(EntryIngredient.of(EntryStacks.of(recipe.getInput(), 64)));
		ores = Collections.singletonList(EntryIngredient.of(stackList.stream().map(EntryStacks::of).collect(Collectors.toList())));
	}

	@Override
	public @NotNull List<EntryIngredient> getInputEntries() {
		return stone;
	}

	@Override
	public @NotNull List<EntryIngredient> getOutputEntries() {
		return ores;
	}
}
