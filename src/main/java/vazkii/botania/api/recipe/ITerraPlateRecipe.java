/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface ITerraPlateRecipe extends IRecipe<IInventory> {
	ResourceLocation TERRA_PLATE_ID = new ResourceLocation(BotaniaAPI.MODID, "terra_plate");
	ResourceLocation TYPE_ID = TERRA_PLATE_ID;

	int getMana();

	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}

	@Nonnull
	@Override
	default ItemStack getIcon() {
		return Registry.ITEM.getOptional(TERRA_PLATE_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@Override
	default boolean isDynamic() {
		return true;
	}
}
