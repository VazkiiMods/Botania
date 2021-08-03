/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.core.NonNullList;
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

public class ManaGunRemoveLensRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<ManaGunRemoveLensRecipe> SERIALIZER = new SimpleRecipeSerializer<>(ManaGunRemoveLensRecipe::new);

	public ManaGunRemoveLensRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundGun = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemManaGun && !ItemManaGun.getLens(stack).isEmpty()) {
					foundGun = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundGun;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemManaGun) {
					gun = stack;
				}
			}
		}

		ItemStack gunCopy = gun.copy();
		gunCopy.setCount(1);
		ItemManaGun.setLens(gunCopy, ItemStack.EMPTY);

		return gunCopy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height > 0;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.getItem() == ModItems.manaGun) {
				ItemStack stack = ItemManaGun.getLens(s);
				stack.setCount(1);
				return stack;
			}
			return null;
		});
	}
}
