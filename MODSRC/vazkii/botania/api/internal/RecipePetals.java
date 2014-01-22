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
package vazkii.botania.api.internal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class RecipePetals {

	ItemStack output;
	List<Integer> colors = new ArrayList();
	
	public RecipePetals(ItemStack output, int... colors) {
		this.output = output;
		for(int c : colors)
			this.colors.add(c);
	}
	
	public boolean matches(IInventory inv) {
		List<Integer> colorsMissing = new ArrayList(colors);
		
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null)
				break;
			
			int color = stack.getItemDamage();
			
			if(!colorsMissing.contains(color))
				return false;
			colorsMissing.remove((Integer) color);
		}
		
		return colorsMissing.isEmpty();
	}
	
	public ItemStack getOutput() {
		return output;
	}

}
