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

import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ManaGunClipRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<ManaGunClipRecipe> SERIALIZER = new SimpleRecipeSerializer<>(ManaGunClipRecipe::new);

	public ManaGunClipRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundGun = false;
		boolean foundClip = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemManaGun && !ItemManaGun.hasClip(stack)) {
					foundGun = true;
				} else if (stack.getItem() == ModItems.clip) {
					foundClip = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundGun && foundClip;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemManaGun) {
				gun = stack;
			}
		}

		if (gun.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack lens = ItemManaGun.getLens(gun);
		ItemStack gunCopy = gun.copy();
		ItemManaGun.setLens(gunCopy, ItemStack.EMPTY);
		ItemManaGun.setClip(gunCopy, true);
		ItemManaGun.setLensAtPos(gunCopy, lens, 0);
		return gunCopy;
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
