/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import net.minecraft.commands.CommandFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

public interface PureDaisyRecipe extends Recipe<Container> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "pure_daisy");

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state);

	/**
	 * Returns true if the block was placed (and if the Pure Daisy should do particles and stuffs).
	 * Should only place the block if !world.isRemote, but should return true if it would've placed
	 * it otherwise. You may return false to cancel the normal particles and do your own.
	 */
	boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy);

	StateIngredient getInput();

	BlockState getOutputState();

	CommandFunction.CacheableFunction getSuccessFunction();

	int getTime();

	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}

	@Override
	default boolean matches(Container p_77569_1_, Level p_77569_2_) {
		return false;
	}

	@Override
	default ItemStack assemble(Container p_77572_1_) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
		return false;
	}

	@Override
	default ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
