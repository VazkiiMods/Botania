/**
 * This class was created by <ToMe25> based on a class by <Vazkii>.
 * It's distributed as part of the Botania Mod.
 * Get the Source Code in github: https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 9, 2020, 12:43 (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import javax.annotation.Nonnull;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemRegenIvy;
import vazkii.botania.common.item.ModItems;

public class RegenIvyRecipe extends SpecialRecipe {
	public static final IRecipeSerializer<RegenIvyRecipe> SERIALIZER = new SpecialRecipeSerializer<>(RegenIvyRecipe::new);

	public RegenIvyRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		ItemStack tool = ItemStack.EMPTY;
		boolean foundIvy = false;
		int materialsFound = 0;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() == ModItems.regenIvy)
					foundIvy = true;
				else if(tool.isEmpty()
						&& !(stack.hasTag() && ItemNBTHelper.getBoolean(stack, ItemRegenIvy.TAG_REGEN, false))
						&& stack.isDamageable())
					tool = stack;
			}
		}
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(materialsFound < 3 && !tool.isEmpty() && tool.getItem().getIsRepairable(tool, stack))
					materialsFound++;
			}
			else if(!stack.isEmpty() && stack != tool && stack.getItem() != ModItems.regenIvy)
				return false;
		}

		return foundIvy && !tool.isEmpty() && materialsFound == 3;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack item = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.isDamageable())
				item = stack;
		}

		ItemStack copy = item.copy();
		ItemNBTHelper.setBoolean(copy, ItemRegenIvy.TAG_REGEN, true);
		copy.setCount(1);
		return copy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 5;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

}