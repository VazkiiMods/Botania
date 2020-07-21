/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.common.item.ItemBlackHoleTalisman;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlackHoleTalismanExtractRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<BlackHoleTalismanExtractRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BlackHoleTalismanExtractRecipe::new);

	public BlackHoleTalismanExtractRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundTalisman = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == ModItems.blackHoleTalisman && !foundTalisman) {
					foundTalisman = true;
				} else {
					return false;
				}
			}
		}

		return foundTalisman;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		ItemStack talisman = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				talisman = stack;
			}
		}

		int count = ItemBlackHoleTalisman.getBlockCount(talisman);
		if (count > 0) {
			Block block = ItemBlackHoleTalisman.getBlock(talisman);
			if (block != null) {
				return new ItemStack(block, Math.min(64, count));
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height > 0;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
