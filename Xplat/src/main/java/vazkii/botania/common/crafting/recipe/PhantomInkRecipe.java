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

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.item.PhantomInkable;
import vazkii.botania.common.item.ModItems;

public class PhantomInkRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<PhantomInkRecipe> SERIALIZER = new SimpleRecipeSerializer<>(PhantomInkRecipe::new);

	public PhantomInkRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@NotNull CraftingContainer var1, @NotNull Level var2) {
		boolean foundInk = false;
		boolean foundItem = false;

		for (int i = 0; i < var1.getContainerSize(); i++) {
			ItemStack stack = var1.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(ModItems.phantomInk) && !foundInk) {
					foundInk = true;
				} else if (!foundItem) {
					if (stack.getItem() instanceof PhantomInkable && !stack.getItem().hasCraftingRemainingItem()) {
						foundItem = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return foundInk && foundItem;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingContainer var1) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < var1.getContainerSize(); i++) {
			ItemStack stack = var1.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof PhantomInkable && item.isEmpty()) {
				item = stack;
			}
		}

		PhantomInkable inkable = (PhantomInkable) item.getItem();
		ItemStack copy = item.copy();
		inkable.setPhantomInk(copy, !inkable.hasPhantomInk(item));
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
