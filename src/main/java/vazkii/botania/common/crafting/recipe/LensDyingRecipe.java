/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 23, 2015, 4:10:24 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.ItemLens;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class LensDyingRecipe implements IRecipe {

	private static final List<String> DYES = Arrays.asList(new String[] {
		"dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", "dyeYellow", "dyeLime", "dyePink", "dyeGray", "dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue", "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack", LibOreDict.MANA_PEARL
	});

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundLens = false;
		boolean foundDye = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ILens && !foundLens)
					foundLens = true;
				else if(!foundDye) {
					int color = getStackColor(stack);
					if(color > -1)
						foundDye = true;
					else return false;
				}
			}
		}

		return foundLens && foundDye;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack lens = null;
		int color = -1;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ILens && lens == null)				
					lens = stack;
				else color = getStackColor(stack);
			}
		}

		if(lens.getItem() instanceof ILens) {
			ILens lensItem = (ILens) lens.getItem();
			ItemStack lensCopy = lens.copy();
			((ItemLens) ModItems.lens).setLensColor(lensCopy, color);

			return lensCopy;
		}

		return null;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	int getStackColor(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		for(int i : ids) {
			int index = DYES.indexOf(OreDictionary.getOreName(i));
			if(index > 0)
				return index;
		}
	
		return -1;
	}

}
