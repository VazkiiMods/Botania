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

@Environment(EnvType.CLIENT)
public abstract class OrechidBaseREIDisplay implements Display {
	protected List<EntryIngredient> stone;
	private final List<EntryIngredient> ores;

	public OrechidBaseREIDisplay(OrechidRecipeWrapper recipe) {
		final int myWeight = recipe.entry.getValue();
		final int amount = 0; // todo fix this Math.max(1, Math.round((float) (myWeight * 64) / CategoryUtils.getTotalOreWeight(getOreWeights(), myWeight)));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = BlockTags.getAllTags().getTagOrEmpty(recipe.entry.getKey())
				.getValues()
				.stream()
				.filter(s -> s.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		for (ItemStack stack : stackList) {
			stack.setCount(amount);
		}
		ores = Collections.singletonList(EntryIngredient.of(stackList.stream().map(EntryStacks::of).collect(Collectors.toList())));
	}

	public static float getTotalOreWeight(List<OrechidOutput> weights, int myWeight) {
		return (weights.stream()
				.map(OrechidOutput::getWeight)
				.reduce(Integer::sum)).orElse(myWeight * 64 * 64);
	}

	protected abstract List<OrechidOutput> getOreWeights();

	@Override
	public @NotNull List<EntryIngredient> getInputEntries() {
		return stone;
	}

	@Override
	public @NotNull List<EntryIngredient> getOutputEntries() {
		return ores;
	}
}
