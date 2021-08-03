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

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import javax.annotation.Nonnull;

public class TerraPickTippingRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<TerraPickTippingRecipe> SERIALIZER = new SimpleRecipeSerializer<>(TerraPickTippingRecipe::new);

	public TerraPickTippingRecipe(ResourceLocation id) {
		super(id);
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundTerraPick = false;
		boolean foundElementiumPick = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemTerraPick && !ItemTerraPick.isTipped(stack)) {
					foundTerraPick = true;
				} else if (stack.getItem() == ModItems.elementiumPick) {
					foundElementiumPick = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundTerraPick && foundElementiumPick;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack terraPick = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerraPick) {
				terraPick = stack;
			}
		}

		if (terraPick.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack terraPickCopy = terraPick.copy();
		ItemTerraPick.setTipped(terraPickCopy);
		return terraPickCopy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
}
