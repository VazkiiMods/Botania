/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.orechid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.compat.rei.CategoryUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

@Environment(EnvType.CLIENT)
public abstract class OrechidBaseREIDisplay implements RecipeDisplay {
	protected List<List<EntryStack>> stone;
	List<List<EntryStack>> ores;

	public OrechidBaseREIDisplay(OrechidRecipeWrapper recipe) {
		final int myWeight = recipe.entry.getValue();
		final int amount = Math.max(1, Math.round((float) (myWeight * 64) / CategoryUtils.getTotalOreWeight(getOreWeights(), myWeight)));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = BlockTags.getTagGroup().getTagOrEmpty(recipe.entry.getKey())
				.values()
				.stream()
				.filter(s -> s.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		for (ItemStack stack : stackList) {
			stack.setCount(amount);
		}
		ores = Collections.singletonList(EntryStack.ofItemStacks(stackList));
	}

	protected abstract Map<Identifier, Integer> getOreWeights();

	@Override
	public @NotNull List<List<EntryStack>> getInputEntries() {
		return stone;
	}

	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return ores;
	}
}
