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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.mana.BasicLensItem;

public class SplitLensRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<SplitLensRecipe> SERIALIZER = new SimpleRecipeSerializer<>(SplitLensRecipe::new);

	public SplitLensRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(@NotNull CraftingContainer inv, @NotNull Level worldIn) {
		return !assemble(inv).isEmpty();
	}

	@NotNull
	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack found = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack candidate = inv.getItem(i);
			if (candidate.isEmpty()) {
				continue;
			}
			if (!found.isEmpty() || (found = getComposite(candidate)).isEmpty()) {
				return ItemStack.EMPTY;
			}
		}
		if (!found.isEmpty()) {
			found = found.copy();
			found.setCount(1);
		}
		return found;
	}

	private ItemStack getComposite(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof BasicLensItem basicLens)) {
			return ItemStack.EMPTY;
		}
		return basicLens.getCompositeLens(stack);
	}

	@NotNull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack candidate = inv.getItem(i);
			if (candidate.getItem() instanceof BasicLensItem basicLensItem) {
				ItemStack newLens = candidate.copy();
				newLens.setCount(1);
				basicLensItem.setCompositeLens(newLens, ItemStack.EMPTY);
				remaining.set(i, newLens);
			}
		}
		return remaining;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 1;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
