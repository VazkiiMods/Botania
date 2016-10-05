/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2015, 6:16:13 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.common.item.ModItems;

public class SpellClothRecipe implements IRecipe {

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundCloth = false;
		boolean foundEnchanted = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.isItemEnchanted() && !foundEnchanted && stack.getItem() != ModItems.spellCloth)
					foundEnchanted = true;

				else if(stack.getItem() == ModItems.spellCloth && !foundCloth)
					foundCloth = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundCloth && foundEnchanted;
	}

	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack stackToDisenchant = null;
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && stack.isItemEnchanted()) {
				stackToDisenchant = stack.copy();
				break;
			}
		}

		if(stackToDisenchant == null)
			return null;

		NBTTagCompound cmp = stackToDisenchant.getTagCompound().copy();
		cmp.removeTag("ench"); // Remove enchantments
		stackToDisenchant.setTagCompound(cmp);

		return stackToDisenchant;
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
