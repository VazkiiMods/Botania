/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.mixin.AccessorRecipeManager;

import java.util.Map;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModRecipeTypes {
	public static final net.minecraft.world.item.crafting.RecipeType<IManaInfusionRecipe> MANA_INFUSION_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();

	public static final net.minecraft.world.item.crafting.RecipeType<IElvenTradeRecipe> ELVEN_TRADE_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SimpleRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SimpleRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	public static final net.minecraft.world.item.crafting.RecipeType<IPureDaisyRecipe> PURE_DAISY_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipePureDaisy> PURE_DAISY_SERIALIZER = new RecipePureDaisy.Serializer();

	public static final net.minecraft.world.item.crafting.RecipeType<IBrewRecipe> BREW_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeBrew> BREW_SERIALIZER = new RecipeBrew.Serializer();

	public static final net.minecraft.world.item.crafting.RecipeType<IPetalRecipe> PETAL_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipePetals> PETAL_SERIALIZER = new RecipePetals.Serializer();

	public static final net.minecraft.world.item.crafting.RecipeType<IRuneAltarRecipe> RUNE_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeRuneAltar> RUNE_SERIALIZER = new RecipeRuneAltar.Serializer();
	public static final RecipeSerializer<HeadRecipe> RUNE_HEAD_SERIALIZER = new HeadRecipe.Serializer();

	public static final RecipeType<ITerraPlateRecipe> TERRA_PLATE_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeTerraPlate> TERRA_PLATE_SERIALIZER = new RecipeTerraPlate.Serializer();

	public static void registerRecipeTypes() {
		ResourceLocation id = prefix("elven_trade");
		Registry.register(Registry.RECIPE_TYPE, id, ELVEN_TRADE_TYPE);
		Registry<RecipeSerializer<?>> r = Registry.RECIPE_SERIALIZER;
		register(r, id, ELVEN_TRADE_SERIALIZER);
		register(r, prefix("elven_trade_lexicon"), LEXICON_ELVEN_TRADE_SERIALIZER);

		id = prefix("mana_infusion");
		Registry.register(Registry.RECIPE_TYPE, id, MANA_INFUSION_TYPE);
		register(r, id, MANA_INFUSION_SERIALIZER);

		id = prefix("pure_daisy");
		Registry.register(Registry.RECIPE_TYPE, id, PURE_DAISY_TYPE);
		register(r, id, PURE_DAISY_SERIALIZER);

		id = prefix("brew");
		Registry.register(Registry.RECIPE_TYPE, id, BREW_TYPE);
		register(r, id, BREW_SERIALIZER);

		id = prefix("petal_apothecary");
		Registry.register(Registry.RECIPE_TYPE, id, PETAL_TYPE);
		register(r, id, PETAL_SERIALIZER);

		id = prefix("runic_altar");
		Registry.register(Registry.RECIPE_TYPE, id, RUNE_TYPE);
		register(r, id, RUNE_SERIALIZER);
		register(r, prefix("runic_altar_head"), RUNE_HEAD_SERIALIZER);

		id = prefix("terra_plate");
		Registry.register(Registry.RECIPE_TYPE, id, TERRA_PLATE_TYPE);
		register(r, id, TERRA_PLATE_SERIALIZER);
	}

	private static class RecipeType<T extends Recipe<?>> implements net.minecraft.world.item.crafting.RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}

	public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipes(Level world, net.minecraft.world.item.crafting.RecipeType<T> type) {
		return ((AccessorRecipeManager) world.getRecipeManager()).botania_getAll(type);
	}
}
