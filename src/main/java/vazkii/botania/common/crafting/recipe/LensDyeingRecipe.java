/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 23, 2015, 4:10:24 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.LibOreDict;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class LensDyeingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private static final List<String> DYES = Arrays.asList("dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", "dyeYellow", "dyeLime", "dyePink", "dyeGray", "dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue", "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack", LibOreDict.MANA_PEARL);

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundLens = false;
		boolean foundDye = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ILens && !foundLens)
					foundLens = true;
				else if(!foundDye) {
					int color = getStackColor(stack);
					if(color > -1)
						foundDye = true;
					else return false;
				}
				else return false;//This means we have an additional item in the recipe after the lens and dye
			}
		}

		return foundLens && foundDye;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack lens = ItemStack.EMPTY;
		int color = -1;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ILens && lens.isEmpty())
					lens = stack;
				else color = getStackColor(stack);//We can assume if its not a lens its a dye because we checked it in matches()
			}
		}

		if(lens.getItem() instanceof ILens) {
			lens.getItem();
			ItemStack lensCopy = lens.copy();
			ItemLens.setLensColor(lensCopy, color);

			return lensCopy;
		}

		return ItemStack.EMPTY;
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

	int getStackColor(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		for(int i : ids) {
			int index = DYES.indexOf(OreDictionary.getOreName(i));
			if(index >= 0)
				return index;
		}

		return -1;
	}
}
