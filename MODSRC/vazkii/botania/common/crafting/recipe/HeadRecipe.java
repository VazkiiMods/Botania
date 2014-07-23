/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 23, 2014, 3:02:17 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class HeadRecipe extends RecipeRuneAltar {

	String name = "";
	
	public HeadRecipe(ItemStack output, int mana, Object... inputs) {
		super(output, mana, inputs);
	}
	
	@Override
	public boolean matches(IInventory inv) {
		boolean matches = super.matches(inv);
		
		if(matches) {
			for(int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if(stack == null)
					break;

				if(stack.getItem() == Items.name_tag) {
					name = stack.getDisplayName();
					if(name.equals(StatCollector.translateToLocal("item.nameTag.name")))
						return false;
				}
			}
		}
		
		return matches;
	}
	
	@Override
	public ItemStack getOutput() {
		ItemStack stack = new ItemStack(Items.skull, 1, 3);
		if(!name.isEmpty())
			ItemNBTHelper.setString(stack, "SkullOwner", name);
		return stack;
	}

}
