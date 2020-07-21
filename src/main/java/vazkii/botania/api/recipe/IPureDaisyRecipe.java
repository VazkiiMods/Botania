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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

public interface IPureDaisyRecipe extends Recipe<Inventory> {
	static Identifier TYPE_ID = new Identifier(BotaniaAPI.MODID, "pure_daisy");

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
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrEmpty(TYPE_ID).get();
	}

	@Override
	default boolean matches(Inventory p_77569_1_, World p_77569_2_) {
		return false;
	}

	@Override
	default ItemStack craft(Inventory p_77572_1_) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean fits(int p_194133_1_, int p_194133_2_) {
		return false;
	}

	@Override
	default ItemStack getOutput() {
		return ItemStack.EMPTY;
	}
}
