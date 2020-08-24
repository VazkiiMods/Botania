/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IManaInfusionRecipe extends IRecipe<IInventory> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_infusion");

	boolean matches(ItemStack stack);

	@Nullable
	BlockState getCatalyst();

	int getManaToConsume();

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.func_241873_b(TYPE_ID).get();
	}

	// Ignored IRecipe stuff

	@Nonnull
	@Override
	default ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return false;
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}
}
