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
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.item.brew.ItemBrewBase;

import javax.annotation.Nonnull;

import java.util.Objects;

public class MergeVialRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<MergeVialRecipe> SERIALIZER = new SpecialRecipeSerializer<>(MergeVialRecipe::new);

	public MergeVialRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, @Nonnull World worldIn) {
		int count = 0;
		Brew brew = null;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
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
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack firstStack = ItemStack.EMPTY;
		ItemBrewBase brew = null;
		int swigs = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
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

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		boolean foundFirst = false;
		int swigs = 0;
		int maxSwigs = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
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
	public boolean canFit(int width, int height) {
		return width * height > 2;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
