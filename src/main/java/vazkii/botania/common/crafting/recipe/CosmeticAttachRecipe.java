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

import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;

import javax.annotation.Nonnull;

public class CosmeticAttachRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<CosmeticAttachRecipe> SERIALIZER = new SpecialRecipeSerializer<>(CosmeticAttachRecipe::new);

	public CosmeticAttachRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundCosmetic = false;
		boolean foundAttachable = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICosmeticBauble && !foundCosmetic) {
					foundCosmetic = true;
				} else if (!foundAttachable) {
					if (stack.getItem() instanceof ICosmeticAttachable && !(stack.getItem() instanceof ICosmeticBauble) && ((ICosmeticAttachable) stack.getItem()).getCosmeticItem(stack).isEmpty()) {
						foundAttachable = true;
					} else {
						return false;
					}
				}
			}
		}

		return foundCosmetic && foundAttachable;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack cosmeticItem = ItemStack.EMPTY;
		ItemStack attachableItem = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICosmeticBauble && cosmeticItem.isEmpty()) {
					cosmeticItem = stack;
				} else {
					attachableItem = stack;
				}
			}
		}

		if (!(attachableItem.getItem() instanceof ICosmeticAttachable)) {
			return ItemStack.EMPTY;
		}

		ICosmeticAttachable attachable = (ICosmeticAttachable) attachableItem.getItem();
		if (!attachable.getCosmeticItem(attachableItem).isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = attachableItem.copy();
		attachable.setCosmeticItem(copy, cosmeticItem);
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
