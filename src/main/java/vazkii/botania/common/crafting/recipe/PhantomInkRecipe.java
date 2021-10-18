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

import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class PhantomInkRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<PhantomInkRecipe> SERIALIZER = new SimpleRecipeSerializer<>(PhantomInkRecipe::new);

	public PhantomInkRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer var1, @Nonnull Level var2) {
		boolean foundInk = false;
		boolean foundItem = false;

		for (int i = 0; i < var1.getContainerSize(); i++) {
			ItemStack stack = var1.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(ModItems.phantomInk) && !foundInk) {
					foundInk = true;
				} else if (!foundItem) {
					if (stack.getItem() instanceof IPhantomInkable && !stack.getItem().hasCraftingRemainingItem()) {
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

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer var1) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < var1.getContainerSize(); i++) {
			ItemStack stack = var1.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof IPhantomInkable && item.isEmpty()) {
				item = stack;
			}
		}

		IPhantomInkable inkable = (IPhantomInkable) item.getItem();
		ItemStack copy = item.copy();
		inkable.setPhantomInk(copy, !inkable.hasPhantomInk(item));
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
