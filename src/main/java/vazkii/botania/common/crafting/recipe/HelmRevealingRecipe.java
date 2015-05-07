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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.item.interaction.thaumcraft.ItemElementiumHelmRevealing;
import vazkii.botania.common.item.interaction.thaumcraft.ItemManasteelHelmRevealing;
import vazkii.botania.common.item.interaction.thaumcraft.ItemTerrasteelHelmRevealing;

public class HelmRevealingRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundGoggles = false;
		boolean foundHelm = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(checkIfHelm(stack))
					foundHelm = true;

				else if(stack.getItem() == (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");)
					foundGoggles = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundGoggles && foundHelm;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack Helm = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && checkIfHelm(stack))
				Helm = stack;
		}

		if(Helm == null)
			return null;

		ItemStack helmCopy = Helm.copy();
		switch (checkHelm(helmCopy)):
			case 1:	ItemStack newHelm = new ItemManasteelHelmRevealing(helmCopy);
				break;
			case 2: ItemStack newHelm = new ItemTerrasteelHelmRevealing(helmCopy);
				break;
			case 3: ItemStack newHelm = new ItemElementiumHelmRevealing(helmCopy);
				break;
			default: ItemStack newHelm = null;
				break;
		return newHelm;
	}

	@Override
	public int getRecipeSize() {
		return 10; //WTF does this do?
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
	
	private int checkHelm(ItemStack helmStack) { //Tried to do a switch/case but got too complex
	if(helmStack instanceof ItemManasteelHelm){return 1;}
	if(helmStack instanceof ItemTerrasteelHelm){return 2;}
	if(helmStack instanceof ItemElementiumHelm){return 3;}
	return 0;
	}

}
