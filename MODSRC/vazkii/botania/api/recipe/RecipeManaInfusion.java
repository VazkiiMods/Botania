/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 30, 2014, 5:57:07 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeManaInfusion {

	ItemStack output;
	Object input;
	int mana;

	public RecipeManaInfusion(ItemStack output, Object input, int mana) {
		this.output = output;
		this.input = input;
		this.mana = mana;
	}

	public boolean matches(ItemStack stack) {
		if(input instanceof ItemStack)
			return stack.isItemEqual((ItemStack) input);

		if(input instanceof String) {
			List<ItemStack> validStacks = OreDictionary.getOres((String) input);
			for(ItemStack stack_ : validStacks)
				if(stack.isItemEqual(stack_))
					return true;
		}

		return false;
	}

	public Object getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	public int getManaToConsume() {
		return mana;
	}
}

