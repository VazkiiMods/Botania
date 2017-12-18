/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 13, 2014, 8:01:14 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ILensControl;
import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;

public class ManaGunLensRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundLens = false;
		boolean foundGun = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemManaGun && ItemManaGun.getLens(stack).isEmpty())
					foundGun = true;

				else if(stack.getItem() instanceof ILens) {
					if(!(stack.getItem() instanceof ILensControl) || !((ILensControl) stack.getItem()).isControlLens(stack))
						foundLens = true;
					else return false;
				}

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundLens && foundGun;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack gun = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemManaGun)
					gun = stack;
				else if(stack.getItem() instanceof ILens)
					lens = stack;
			}
		}

		if(lens.isEmpty() || gun.isEmpty())
			return ItemStack.EMPTY;

		ItemStack gunCopy = gun.copy();
		ItemManaGun.setLens(gunCopy, lens);

		return gunCopy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
