/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 2:02:44 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import java.util.ArrayList;
import java.util.List;

import vazkii.botania.api.internal.MappableStackWrapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipePetals {

	ItemStack output;
	List<Object> inputs;

	public RecipePetals(ItemStack output, Object... inputs) {
		this.output = output;
		
		List<Object> inputsToSet = new ArrayList();
		for(Object obj : inputs) {
			if(obj instanceof String)
				inputsToSet.add(obj);
			else if(obj instanceof MappableStackWrapper)
				inputsToSet.add(obj);
			else if(obj instanceof ItemStack)
				inputsToSet.add(new MappableStackWrapper((ItemStack) obj));
			else throw new IllegalArgumentException("Invalid input");
		}
		
		this.inputs = inputsToSet;
	}

	public boolean matches(IInventory inv) {
		List<Object> inputsMissing = new ArrayList(inputs);

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null)
				break;

			String oredict = OreDictionary.getOreName(OreDictionary.getOreID(stack));
			
			if(!inputsMissing.contains(stack) && !inputsMissing.contains(oredict))
				return false;
			
			if(inputsMissing.contains(stack))
				inputsMissing.remove(stack);
			else inputsMissing.remove(oredict);
		}

		return inputsMissing.isEmpty();
	}
	
	public List<Object> getInputs() {
		return new ArrayList(inputs);
	}

	public ItemStack getOutput() {
		return output;
	}

}
