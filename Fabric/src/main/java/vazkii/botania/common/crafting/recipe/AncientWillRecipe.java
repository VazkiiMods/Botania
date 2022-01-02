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

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.item.ItemAncientWill;

import javax.annotation.Nonnull;

public class AncientWillRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<AncientWillRecipe> SERIALIZER = new SimpleRecipeSerializer<>(AncientWillRecipe::new);

	public AncientWillRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundWill = false;
		boolean foundItem = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemAncientWill && !foundWill) {
					foundWill = true;
				} else if (!foundItem) {
					if (stack.getItem() instanceof IAncientWillContainer) {
						foundItem = true;
					} else {
						return false;
					}
				}
			}
		}

		return foundWill && foundItem;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack item = ItemStack.EMPTY;
		IAncientWillContainer.AncientWillType will = null;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IAncientWillContainer && item.isEmpty()) {
					item = stack;
				} else {
					will = ((ItemAncientWill) stack.getItem()).type; // we already verified this is a will in matches()
				}
			}
		}

		IAncientWillContainer container = (IAncientWillContainer) item.getItem();
		if (container.hasAncientWill(item, will)) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = item.copy();
		container.addAncientWill(copy, will);
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width > 1 || height > 1;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
