/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 17, 2014, 8:30:41 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.mana.ICompositableLens;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;

public class CompositeLensRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundLens = false;
		boolean foundSecondLens = false;
		boolean foundSlimeball = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICompositableLens && !foundSecondLens) {
					if(foundLens)
						foundSecondLens = true;
					else foundLens = true;
				} else if(stack.getItem() == Items.SLIME_BALL)
					foundSlimeball = true;
				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundSecondLens && foundSlimeball;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack secondLens = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICompositableLens)
					if(lens.isEmpty())
						lens = stack;
					else secondLens = stack;
			}
		}

		if(lens.getItem() instanceof ICompositableLens) {
			ICompositableLens lensItem = (ICompositableLens) lens.getItem();
			if(secondLens.isEmpty() || !lensItem.canCombineLenses(lens, secondLens) || !lensItem.getCompositeLens(lens).isEmpty() || !lensItem.getCompositeLens(secondLens).isEmpty())
				return ItemStack.EMPTY;

			ItemStack lensCopy = lens.copy();
			((ItemLens) ModItems.lens).setCompositeLens(lensCopy, secondLens);

			return lensCopy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 3;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}