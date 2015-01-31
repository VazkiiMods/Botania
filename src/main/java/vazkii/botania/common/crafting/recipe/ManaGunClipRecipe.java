/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 8:37:33 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;

public class ManaGunClipRecipe  implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundGun = false;
		boolean foundClip = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() instanceof ItemManaGun && !ItemManaGun.hasClip(stack))
					foundGun = true;

				else if(stack.getItem() == ModItems.clip)
					foundClip = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundGun && foundClip;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack gun = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof ItemManaGun)
				gun = stack;
		}

		if(gun == null)
			return null;

		ItemStack lens = ItemManaGun.getLens(gun);
		ItemManaGun.setLens(gun, null);
		ItemStack gunCopy = gun.copy();
		ItemManaGun.setClip(gunCopy, true);
		ItemManaGun.setLensAtPos(gunCopy, lens, 0);
		return gunCopy;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}


}
