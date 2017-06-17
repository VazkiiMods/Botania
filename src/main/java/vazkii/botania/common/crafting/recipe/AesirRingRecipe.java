/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 31, 2015, 10:39:58 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;

public class AesirRingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundThorRing = false;
		boolean foundOdinRing = false;
		boolean foundLokiRing = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() == ModItems.thorRing && !foundThorRing)
					foundThorRing = true;
				else if(stack.getItem() == ModItems.odinRing && !foundOdinRing)
					foundOdinRing = true;
				else if(stack.getItem() == ModItems.lokiRing && !foundLokiRing)
					foundLokiRing = true;
				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundThorRing && foundOdinRing && foundLokiRing;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		String soulbind = null;
		UUID soulbindUUID = null;
		boolean hasUUID = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof IRelic) {
					if(((IRelic) stack.getItem()).hasUUID(stack)) {
						hasUUID = true;
						UUID bindUUID = ((IRelic) stack.getItem()).getSoulbindUUID(stack);
						if(soulbindUUID == null)
							soulbindUUID = bindUUID;
						else if(!soulbindUUID.equals(bindUUID))
							return ItemStack.EMPTY;
					}
					else {
						String bind = ((IRelic) stack.getItem()).getSoulbindUsername(stack);
						if(soulbind == null)
							soulbind = bind;
						else if(!soulbind.equals(bind))
							return ItemStack.EMPTY;
					}
				} else return ItemStack.EMPTY;
			}
		}

		ItemStack stack = new ItemStack(ModItems.aesirRing);
		if(hasUUID)
			((IRelic) ModItems.aesirRing).bindToUUID(soulbindUUID, stack);
		else
			((IRelic) ModItems.aesirRing).bindToUsername(soulbind, stack);
		return stack;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 3;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
