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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.api.mana.ILens;

import javax.annotation.Nonnull;

public class SplitLensRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<SplitLensRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SplitLensRecipe::new);

	public SplitLensRecipe(Identifier idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn) {
		return !craft(inv).isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack found = ItemStack.EMPTY;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack candidate = inv.getStack(i);
			if (candidate.isEmpty()) {
				continue;
			}
			if (!found.isEmpty() || (found = getComposite(candidate)).isEmpty()) {
				return ItemStack.EMPTY;
			}
		}
		return found;
	}

	private ItemStack getComposite(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ILens)) {
			return ItemStack.EMPTY;
		}
		return ((ILens) item).getCompositeLens(stack);
	}

	@Nonnull
	@Override
	public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
		DefaultedList<ItemStack> remaining = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack candidate = inv.getStack(i);
			if (candidate.getItem() instanceof ILens) {
				ItemStack newLens = candidate.copy();
				ILens lens = (ILens) candidate.getItem();
				lens.setCompositeLens(newLens, ItemStack.EMPTY);
				remaining.set(i, newLens);
			}
		}
		return remaining;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
