/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.item.AncientWillItem;

import java.util.Objects;

public class AncientWillRecipe extends CustomRecipe {
	public static final RecipeSerializer<AncientWillRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(AncientWillRecipe::new);

	public AncientWillRecipe(CraftingBookCategory craftingBookCategory) {
		super(craftingBookCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, Level world) {
		AncientWillItem foundWill = null;
		AncientWillContainer foundContainer = null;
		ItemStack containerStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof AncientWillItem item) {
					if (foundWill != null) {
						return false;
					}
					foundWill = item;
				} else if (stack.getItem() instanceof AncientWillContainer container) {
					if (foundContainer != null) {
						return false;
					}
					foundContainer = container;
					containerStack = stack;
				} else {
					return false;
				}
			}
		}

		return foundWill != null && foundContainer != null && !foundContainer.hasAncientWill(containerStack, foundWill.type);
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
		ItemStack item = ItemStack.EMPTY;
		AncientWillContainer.AncientWillType will = null;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof AncientWillContainer && item.isEmpty()) {
					item = stack;
				} else {
					will = ((AncientWillItem) stack.getItem()).type; // we already verified this is a will in matches()
				}
			}
		}

		AncientWillContainer container = (AncientWillContainer) item.getItem();
		if (container.hasAncientWill(item, Objects.requireNonNull(will))) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = item.copy();
		container.addAncientWill(copy, will);

		if (BuiltInRegistries.ITEM.stream()
				.filter(AncientWillItem.class::isInstance)
				.map(AncientWillItem.class::cast)
				.allMatch(willItem -> container.hasAncientWill(copy, willItem.type))) {
			DataComponentHelper.bumpRarity(copy);
		}

		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width > 1 || height > 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
