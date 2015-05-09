/**
 * This class was created by <Lazersmoke>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 6, 2015, 9:45:56 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import net.minecraft.nbt.NBTTagList;

public class HelmRevealingRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		
		boolean foundGoggles = false;
		boolean foundHelm = false;
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(checkHelm(stack) != 0)
					foundHelm = true;

				else if(stack.getItem() == (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles"))
					foundGoggles = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}
		return foundGoggles && foundHelm;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack helm = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && checkHelm(stack) != 0)
				helm = stack;
		}

		if(helm == null)
			return null;

		ItemStack helmCopy = helm.copy();
		ItemStack newHelm;
		switch (checkHelm(helmCopy)) {
			case 1:	newHelm = new ItemStack(ModItems.manasteelHelmRevealing);
				break;
			case 2: newHelm = new ItemStack(ModItems.terrasteelHelmRevealing);
				break;
			case 3: newHelm = new ItemStack(ModItems.elementiumHelmRevealing);
				break;
			default: newHelm = null;
				break;
		}
		for(int i = 0; i < 6; i++){
			if(ItemNBTHelper.getBoolean(helmCopy, "AncientWill" + i, false)){
				ItemNBTHelper.setBoolean(newHelm, "AncientWill" + i, true);
			}
		}
		NBTTagList enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true);
		if(enchList != null){
			ItemNBTHelper.setList(newHelm, "ench", enchList);
		}
		return newHelm;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
	
	private int checkHelm(ItemStack helmStack) {
		if(helmStack.getUnlocalizedName().contains("item.manasteelHelm") && !helmStack.getUnlocalizedName().contains("Reveal")){return 1;}
		if(helmStack.getUnlocalizedName().contains("item.terrasteelHelm") && !helmStack.getUnlocalizedName().contains("Reveal")){return 2;}
		if(helmStack.getUnlocalizedName().contains("item.elementiumHelm") && !helmStack.getUnlocalizedName().contains("Reveal")){return 3;}
		return 0;
	}

}
