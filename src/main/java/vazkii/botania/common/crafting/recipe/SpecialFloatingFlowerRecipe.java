/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 17, 2014, 6:34:36 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class SpecialFloatingFlowerRecipe implements IRecipe {

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundFloatingFlower = false;
		boolean foundSpecialFlower = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() == Item.getItemFromBlock(ModBlocks.floatingFlower))
					foundFloatingFlower = true;

				else if(stack.getItem() == Item.getItemFromBlock(ModBlocks.specialFlower))
					foundSpecialFlower = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundFloatingFlower && foundSpecialFlower;
	}

	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack specialFlower = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && stack.getItem() == Item.getItemFromBlock(ModBlocks.specialFlower))
				specialFlower = stack;
		}

		if(specialFlower == null)
			return null;

		return ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), ItemBlockSpecialFlower.getType(specialFlower));
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	@Nonnull
	@Override
	public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
