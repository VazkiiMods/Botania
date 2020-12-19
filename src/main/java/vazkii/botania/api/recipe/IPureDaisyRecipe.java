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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

public interface IPureDaisyRecipe extends IRecipe<IInventory> {
	static ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "pure_daisy");

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	boolean matches(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy, BlockState state);

	/**
	 * Returns true if the block was placed (and if the Pure Daisy should do particles and stuffs).
	 * Should only place the block if !world.isRemote, but should return true if it would've placed
	 * it otherwise. You may return false to cancel the normal particles and do your own.
	 */
	boolean set(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy);

	StateIngredient getInput();

	BlockState getOutputState();

	int getTime();

	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	@Override
	default boolean matches(IInventory inv, World worldIn) {
		return false;
	}

	@Override
	default ItemStack getCraftingResult(IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isDynamic() {
		return true;
	}
}
