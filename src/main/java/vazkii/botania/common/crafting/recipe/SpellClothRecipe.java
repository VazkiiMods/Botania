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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class SpellClothRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundCloth = false;
		boolean foundEnchanted = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.isItemEnchanted() && !foundEnchanted && stack.getItem() != ModItems.spellCloth)
					foundEnchanted = true;

				else if(stack.getItem() == ModItems.spellCloth && !foundCloth)
					foundCloth = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundCloth && foundEnchanted;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack stackToDisenchant = ItemStack.EMPTY;
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty() && stack.isItemEnchanted() && stack.getItem() != ModItems.spellCloth) {
				stackToDisenchant = stack.copy();
				stackToDisenchant.setCount(1);
				break;
			}
		}

		if(stackToDisenchant.isEmpty())
			return ItemStack.EMPTY;

		stackToDisenchant.getTagCompound().removeTag("ench"); // Remove enchantments
		return stackToDisenchant;
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
