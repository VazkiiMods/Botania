/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 3, 2014, 6:54:18 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemRegenIvy;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class RegenIvyRecipe implements IRecipe {

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		ItemStack tool = null;
		boolean foundIvy = false;
		int materialsFound = 0;
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				Item item = stack.getItem();
				if(item.isRepairable() && !(ItemNBTHelper.detectNBT(stack) && ItemNBTHelper.getBoolean(stack, ItemRegenIvy.TAG_REGEN, false)))
					tool = stack;

				else if(item == ModItems.regenIvy)
					foundIvy = true;

			}
		}
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				Item item = stack.getItem();

				if(tool != null && tool.getItem().getIsRepairable(tool, stack))
					materialsFound++;

				else if(stack != tool && item != ModItems.regenIvy)
					return false;
			}
		}

		return tool != null && foundIvy && materialsFound == 3;
	}

	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack tool = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && stack.getItem().isDamageable())
				tool = stack;
		}

		if(tool == null)
			return null;

		ItemStack toolCopy = tool.copy();
		ItemNBTHelper.setBoolean(toolCopy, ItemRegenIvy.TAG_REGEN, true);
		return toolCopy;
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
