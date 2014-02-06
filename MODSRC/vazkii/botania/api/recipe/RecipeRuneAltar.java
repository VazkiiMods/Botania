/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 5, 2014, 1:41:14 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeRuneAltar {

	ItemStack output;
	List<String> inputs = new ArrayList();
	int mana;

	public RecipeRuneAltar(ItemStack output, int mana, String... inputs) {
		this.output = output;
		for(String i : inputs)
			this.inputs.add(i);
		this.mana = mana;
	}

	public boolean matches(IInventory inv) {
		List<String> inputsMissing = new ArrayList(inputs);

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null)
				break;

			String ore = OreDictionary.getOreName(OreDictionary.getOreID(stack));

			if(!inputsMissing.contains(ore))
				return false;
			inputsMissing.remove(ore);
		}

		return inputsMissing.isEmpty();
	}

	public ItemStack getOutput() {
		return output;
	}
	
	public int getManaUsage() {
		return mana;
	}
	
}
