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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.UUID;

public class AesirRingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean isHidden() {
		return true;
	}

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
		UUID soulbindUUID = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof IRelic && ((IRelic) stack.getItem()).hasUUID(stack)) {
					UUID bindUUID = ((IRelic) stack.getItem()).getSoulbindUUID(stack);
					if(soulbindUUID == null)
						soulbindUUID = bindUUID;
					else if(!soulbindUUID.equals(bindUUID))
						return ItemStack.EMPTY;
				} else return ItemStack.EMPTY;
			}
		}

		ItemStack stack = new ItemStack(ModItems.aesirRing);
		((IRelic) ModItems.aesirRing).bindToUUID(soulbindUUID, stack);
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
