/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.ICompositableLens;

import javax.annotation.Nonnull;

public class CompositeLensRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<CompositeLensRecipe> SERIALIZER = new SimpleRecipeSerializer<>(CompositeLensRecipe::new);

	public CompositeLensRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundLens = false;
		boolean foundSecondLens = false;
		boolean foundSlimeball = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack secondLens = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
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
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 3;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
