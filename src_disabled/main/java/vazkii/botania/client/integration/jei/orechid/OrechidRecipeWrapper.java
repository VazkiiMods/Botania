/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vazkii.botania.client.integration.jei.JEIBotaniaPlugin.doesOreExist;

public class OrechidRecipeWrapper implements IRecipeWrapper, Comparable<OrechidRecipeWrapper> {

	private final int weight;
	private final List<List<ItemStack>> outputStacks;

	protected ItemStack getInputStack() {
		return new ItemStack(Blocks.STONE, 64);
	}

	public OrechidRecipeWrapper(Map.Entry<String, Integer> entry) {
		this.weight = entry.getValue();

		final int amount = Math.max(1, Math.round((float) weight * 64 / getTotalOreWeight()));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = OreDictionary.getOres(entry.getKey()).stream()
				.filter(s -> s.getItem() instanceof ItemBlock)
				.map(ItemStack::copy)
				.collect(Collectors.toList());

		stackList.forEach(s -> s.setCount(amount));

		outputStacks = Collections.singletonList(stackList);
	}

	public Map<String, Integer> getOreWeights() {
		return BotaniaAPI.oreWeights;
	}

	private float getTotalOreWeight() {
		return (getOreWeights().entrySet().stream()
				.filter(e -> doesOreExist(e.getKey()))
				.map(Map.Entry::getValue)
				.reduce(Integer::sum)).orElse(weight * 64 * 64);
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, getInputStack());
		ingredients.setOutputLists(VanillaTypes.ITEM, outputStacks);
	}

	@Override
	public int compareTo(@Nonnull OrechidRecipeWrapper o) {
		return Integer.compare(o.weight, weight);
	}
}
