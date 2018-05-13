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
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrechidRecipeWrapper implements IRecipeWrapper, Comparable<OrechidRecipeWrapper> {
    public String oreDictKey;
    public int weight;

    static ItemStack inputStack;
    List<List<ItemStack>> outputStacks;

    public ItemStack getInputStack() {
        if (inputStack == null) inputStack = new ItemStack(Blocks.STONE, 64);
        return inputStack;
    }

    public List<List<ItemStack>> getOutputStacks() {
        if (outputStacks == null) {
            final int amount = Math.max(1, Math.round((float) weight * 64 / getTotalOreWeight()));

            // Shouldn't ever return an empty list since the ore weight
            // list is filtered to only have ores with ItemBlocks
            List<ItemStack> stackList = OreDictionary.getOres(oreDictKey).stream()
                    .filter(s -> s.getItem() instanceof ItemBlock)
                    .map(ItemStack::copy)
                    .collect(Collectors.toList());

            stackList.forEach(s -> s.setCount(amount));

            outputStacks = Collections.singletonList(stackList);
        }
        return outputStacks;
    }

    public OrechidRecipeWrapper(Map.Entry<String, Integer> entry) {
        this.oreDictKey = entry.getKey();
        this.weight = entry.getValue();
    }

    public Map<String, Integer> getOreWeights() {
        return BotaniaAPI.oreWeights;
    }

    public float getTotalOreWeight() {
        return (getOreWeights().entrySet().stream()
                .filter(JEIBotaniaPlugin::doesOreExist)
                .map(Map.Entry::getValue)
                .reduce(Integer::sum)).orElse(weight * 64 * 64);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        ingredients.setInput(ItemStack.class, getInputStack());
        ingredients.setOutputLists(ItemStack.class, getOutputStacks());
    }

    @Override
    public int compareTo(OrechidRecipeWrapper o) {
        return Integer.compare(o.weight, weight);
    }
}
