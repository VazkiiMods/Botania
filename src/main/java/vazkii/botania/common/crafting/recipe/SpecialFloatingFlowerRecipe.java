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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;

public class SpecialFloatingFlowerRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	public final String type;

	public SpecialFloatingFlowerRecipe(String type) {
		this.type = type;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundFloatingFlower = false;
		boolean foundSpecialFlower = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() == Item.getItemFromBlock(ModBlocks.floatingFlower))
					foundFloatingFlower = true;

				else if(stack.getItem() == Item.getItemFromBlock(ModBlocks.specialFlower) && ItemBlockSpecialFlower.getType(stack).equals(type))
					foundSpecialFlower = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundFloatingFlower && foundSpecialFlower;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack specialFlower = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(ModBlocks.specialFlower) && ItemBlockSpecialFlower.getType(stack).equals(type))
				specialFlower = stack;
		}

		if(specialFlower.isEmpty())
			return ItemStack.EMPTY;

		return ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), type);
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
