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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ManaGunClipRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<ManaGunClipRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ManaGunClipRecipe::new);

	public ManaGunClipRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundGun = false;
		boolean foundClip = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
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
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
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
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public DefaultedList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
