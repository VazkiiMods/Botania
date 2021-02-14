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

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.item.brew.ItemBrewBase;

import javax.annotation.Nonnull;

import java.util.Objects;

public class MergeVialRecipe extends SpecialCraftingRecipe {
	public static final SpecialRecipeSerializer<MergeVialRecipe> SERIALIZER = new SpecialRecipeSerializer<>(MergeVialRecipe::new);

	public MergeVialRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, @Nonnull World worldIn) {
		int count = 0;
		Brew brew = null;

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack stack = inv.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}

			if (!(stack.getItem() instanceof ItemBrewBase)) {
				return false;
			}

			ItemBrewBase vial = (ItemBrewBase) stack.getItem();

			if (brew == null) {
				brew = vial.getBrew(stack);
			} else if (brew != vial.getBrew(stack)) {
				return false;
			}
			count++;
		}

		return count > 1;
	}

	@Nonnull
	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack firstStack = ItemStack.EMPTY;
		ItemBrewBase brew = null;
		int swigs = 0;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}

			if (brew == null) {
				firstStack = stack.copy();
				brew = ((ItemBrewBase) stack.getItem());
			}
			swigs += brew.getSwigsLeft(stack);
			if (swigs >= brew.getSwigs()) {
				swigs = brew.getSwigs();
				break;
			}
		}

		Objects.requireNonNull(brew).setSwigsLeft(firstStack, swigs);
		return firstStack;
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
		DefaultedList<ItemStack> remaining = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

		boolean foundFirst = false;
		int swigs = 0;
		int maxSwigs = 0;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}

			ItemBrewBase brew = ((ItemBrewBase) stack.getItem());
			if (!foundFirst) {
				foundFirst = true;
				swigs = brew.getSwigsLeft(stack);
				maxSwigs = brew.getSwigs();
				continue;
			}

			swigs += brew.getSwigsLeft(stack);
			if (swigs > maxSwigs) {
				brew.setSwigsLeft(stack, swigs - maxSwigs);
				swigs = maxSwigs;
				remaining.set(i, stack.copy());
			} else {
				remaining.set(i, brew.getBaseStack());
			}
		}

		return remaining;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height > 2;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
