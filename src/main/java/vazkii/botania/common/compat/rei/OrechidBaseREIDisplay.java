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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.internal.OrechidOutput;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import vazkii.botania.common.crafting.StateIngredientHelper;

@Environment(EnvType.CLIENT)
public abstract class OrechidBaseREIDisplay implements Display {
	protected List<EntryIngredient> stone;
	private final List<EntryIngredient> ores;

	public OrechidBaseREIDisplay(OrechidOutput recipe, int totalWeight) {
		final int myWeight = recipe.getWeight();
		final int amount =  Math.max(1, Math.round((float) (myWeight * 64) / totalWeight));

		List<ItemStack> stackList = StateIngredientHelper.toStackList(recipe.getOutput());
		for (ItemStack stack : stackList) {
			stack.setCount(amount);
		}
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
