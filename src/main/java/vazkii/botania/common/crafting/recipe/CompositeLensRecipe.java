/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.api.mana.ICompositableLens;

import javax.annotation.Nonnull;

public class CompositeLensRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<CompositeLensRecipe> SERIALIZER = new SpecialRecipeSerializer<>(CompositeLensRecipe::new);

	public CompositeLensRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundLens = false;
		boolean foundSecondLens = false;
		boolean foundSlimeball = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICompositableLens && !foundSecondLens) {
					if (foundLens) {
						foundSecondLens = true;
					} else {
						foundLens = true;
					}
				} else if (stack.getItem() == Items.SLIME_BALL) {
					foundSlimeball = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundSecondLens && foundSlimeball;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack secondLens = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICompositableLens) {
					if (lens.isEmpty()) {
						lens = stack;
					} else {
						secondLens = stack;
					}
				}
			}
		}

		if (lens.getItem() instanceof ICompositableLens) {
			ICompositableLens lensItem = (ICompositableLens) lens.getItem();
			if (secondLens.isEmpty() || !lensItem.canCombineLenses(lens, secondLens) || !lensItem.getCompositeLens(lens).isEmpty() || !lensItem.getCompositeLens(secondLens).isEmpty()) {
				return ItemStack.EMPTY;
			}

			ItemStack lensCopy = lens.copy();
			lensItem.setCompositeLens(lensCopy, secondLens);

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
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
