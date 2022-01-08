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
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class KeepIvyRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<KeepIvyRecipe> SERIALIZER = new SimpleRecipeSerializer<>(KeepIvyRecipe::new);

	public KeepIvyRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundIvy = false;
		boolean foundItem = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(ModItems.keepIvy)) {
					foundIvy = true;
				} else if (!foundItem
						&& !(stack.hasTag() && ItemNBTHelper.getBoolean(stack, ItemKeepIvy.TAG_KEEP, false))
						&& !stack.getItem().hasCraftingRemainingItem()) {
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && !stack.is(ModItems.keepIvy)) {
				item = stack;
			}
		}

		ItemStack copy = item.copy();
		ItemNBTHelper.setBoolean(copy, ItemKeepIvy.TAG_KEEP, true);
		copy.setCount(1);
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
