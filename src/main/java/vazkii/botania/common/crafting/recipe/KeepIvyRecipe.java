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
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class KeepIvyRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<KeepIvyRecipe> SERIALIZER = new SpecialRecipeSerializer<>(KeepIvyRecipe::new);

	public KeepIvyRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundIvy = false;
		boolean foundItem = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == ModItems.keepIvy) {
					foundIvy = true;
				} else if (!foundItem
						&& !(stack.hasTag() && ItemNBTHelper.getBoolean(stack, ItemKeepIvy.TAG_KEEP, false))
						&& !stack.getItem().hasRecipeRemainder()) {
					foundItem = true;
				} else {
					return false;
				}
			}
		}

		return foundIvy && foundItem;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty() && stack.getItem() != ModItems.keepIvy) {
				item = stack;
			}
		}

		ItemStack copy = item.copy();
		ItemNBTHelper.setBoolean(copy, ItemKeepIvy.TAG_KEEP, true);
		copy.setCount(1);
		return copy;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
