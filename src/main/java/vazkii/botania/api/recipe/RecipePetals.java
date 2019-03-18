/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 2:02:44 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipePetals {

	private final ItemStack output;
	private final ImmutableList<Ingredient> inputs;

	public RecipePetals(ItemStack output, Ingredient... inputs) {
		this.output = output;
		this.inputs = ImmutableList.copyOf(inputs);
	}

	public boolean matches(IItemHandler inv) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack input = inv.getStackInSlot(i);
			if(input.isEmpty())
				break;

			int stackIndex = -1;

			for(int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if(ingr.test(input)) {
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				ingredientsMissing.remove(stackIndex);
			else return false;
		}

		return ingredientsMissing.isEmpty();
	}

	public List<Ingredient> getInputs() {
		return inputs;
	}

	public ItemStack getOutput() {
		return output;
	}

}
