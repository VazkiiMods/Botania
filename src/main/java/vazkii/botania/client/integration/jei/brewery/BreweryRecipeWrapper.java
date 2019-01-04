/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class BreweryRecipeWrapper implements IRecipeWrapper {

	private final List<List<ItemStack>> input;
	private final List<ItemStack> output;

	private static final List<ItemStack> inputs = Arrays.asList(new ItemStack(ModItems.vial),
		new ItemStack(ModItems.vial, 1, 1), new ItemStack(ModItems.incenseStick), new ItemStack(ModItems.bloodPendant));

	public BreweryRecipeWrapper(RecipeBrew recipeBrew) {
		ImmutableList.Builder<List<ItemStack>> inputBuilder = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> outputBuilder = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> containers = ImmutableList.builder();

		for(ItemStack stack : inputs) {
			ItemStack brewed = recipeBrew.getOutput(stack);
			if(!brewed.isEmpty()) {
				containers.add(stack);
				outputBuilder.add(brewed);
			}
		}
		inputBuilder.add(containers.build());

		for(Object o : recipeBrew.getInputs()) {
			if(o instanceof ItemStack) {
				inputBuilder.add(ImmutableList.of((ItemStack) o));
			}
			if(o instanceof String) {
				inputBuilder.add(OreDictionary.getOres((String) o));
			}
		}

		input = inputBuilder.build();
		output = outputBuilder.build();
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, input);
		ingredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(output));
	}
}
