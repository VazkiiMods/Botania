/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 22, 2015, 8:55:03 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;

import javax.annotation.Nonnull;

public class CosmeticRemoveRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<CosmeticRemoveRecipe> SERIALIZER = new SpecialRecipeSerializer<>(CosmeticRemoveRecipe::new);

	public CosmeticRemoveRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundAttachable = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ICosmeticAttachable && !(stack.getItem() instanceof ICosmeticBauble) && !((ICosmeticAttachable) stack.getItem()).getCosmeticItem(stack).isEmpty())
					foundAttachable = true;
				else return false;
			}
		}

		return foundAttachable;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack attachableItem = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty())
				attachableItem = stack;
		}

		ICosmeticAttachable attachable = (ICosmeticAttachable) attachableItem.getItem();
		if(attachable.getCosmeticItem(attachableItem).isEmpty())
			return ItemStack.EMPTY;

		ItemStack copy = attachableItem.copy();
		attachable.setCosmeticItem(copy, ItemStack.EMPTY);
		return copy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height > 0;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
