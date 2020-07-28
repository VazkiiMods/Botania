/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.mixin.AccessorRecipeManager;

import java.util.Map;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModRecipeTypes {
	public static final net.minecraft.recipe.RecipeType<IManaInfusionRecipe> MANA_INFUSION_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();

	public static final net.minecraft.recipe.RecipeType<IElvenTradeRecipe> ELVEN_TRADE_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SpecialRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SpecialRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	public static final net.minecraft.recipe.RecipeType<IPureDaisyRecipe> PURE_DAISY_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipePureDaisy> PURE_DAISY_SERIALIZER = new RecipePureDaisy.Serializer();

	public static final net.minecraft.recipe.RecipeType<IBrewRecipe> BREW_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeBrew> BREW_SERIALIZER = new RecipeBrew.Serializer();

	public static final net.minecraft.recipe.RecipeType<IPetalRecipe> PETAL_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipePetals> PETAL_SERIALIZER = new RecipePetals.Serializer();

	public static final net.minecraft.recipe.RecipeType<IRuneAltarRecipe> RUNE_TYPE = new RecipeType<>();
	public static final RecipeSerializer<RecipeRuneAltar> RUNE_SERIALIZER = new RecipeRuneAltar.Serializer();
	public static final RecipeSerializer<HeadRecipe> RUNE_HEAD_SERIALIZER = new HeadRecipe.Serializer();

	public static void registerRecipeTypes() {
		Identifier id = prefix("elven_trade");
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
	}

	private static class RecipeType<T extends Recipe<?>> implements net.minecraft.recipe.RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getId(this).toString();
		}
	}

	public static <C extends Inventory, T extends Recipe<C>> Map<Identifier, Recipe<C>> getRecipes(World world, net.minecraft.recipe.RecipeType<T> type) {
		return ((AccessorRecipeManager) world.getRecipeManager()).callGetAllOfType(type);
	}
}
