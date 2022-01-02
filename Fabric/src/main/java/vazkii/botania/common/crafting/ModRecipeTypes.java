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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.mixin.AccessorRecipeManager;

import java.util.Map;
import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModRecipeTypes {
	public static final RecipeType<IManaInfusionRecipe> MANA_INFUSION_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();

	public static final RecipeType<IElvenTradeRecipe> ELVEN_TRADE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SimpleRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SimpleRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	public static final RecipeType<IPureDaisyRecipe> PURE_DAISY_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipePureDaisy> PURE_DAISY_SERIALIZER = new RecipePureDaisy.Serializer();

	public static final RecipeType<IBrewRecipe> BREW_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeBrew> BREW_SERIALIZER = new RecipeBrew.Serializer();

	public static final RecipeType<IPetalRecipe> PETAL_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipePetals> PETAL_SERIALIZER = new RecipePetals.Serializer();

	public static final RecipeType<IRuneAltarRecipe> RUNE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeRuneAltar> RUNE_SERIALIZER = new RecipeRuneAltar.Serializer();
	public static final RecipeSerializer<HeadRecipe> RUNE_HEAD_SERIALIZER = new HeadRecipe.Serializer();

	public static final RecipeType<ITerraPlateRecipe> TERRA_PLATE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeTerraPlate> TERRA_PLATE_SERIALIZER = new RecipeTerraPlate.Serializer();

	public static final RecipeType<IOrechidRecipe> ORECHID_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeOrechid> ORECHID_SERIALIZER = new RecipeOrechid.Serializer();

	public static final RecipeType<IOrechidRecipe> ORECHID_IGNEM_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeOrechidIgnem> ORECHID_IGNEM_SERIALIZER = new RecipeOrechidIgnem.Serializer();

	public static final RecipeType<IOrechidRecipe> MARIMORPHOSIS_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeMarimorphosis> MARIMORPHOSIS_SERIALIZER = new RecipeMarimorphosis.Serializer();

	public static void registerRecipeTypes(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
		ResourceLocation id = IElvenTradeRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, ELVEN_TRADE_TYPE);
		register(r, id, ELVEN_TRADE_SERIALIZER);
		register(r, prefix("elven_trade_lexicon"), LEXICON_ELVEN_TRADE_SERIALIZER);

		id = IManaInfusionRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, MANA_INFUSION_TYPE);
		register(r, id, MANA_INFUSION_SERIALIZER);

		id = IPureDaisyRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, PURE_DAISY_TYPE);
		register(r, id, PURE_DAISY_SERIALIZER);

		id = IBrewRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, BREW_TYPE);
		register(r, id, BREW_SERIALIZER);

		id = IPetalRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, PETAL_TYPE);
		register(r, id, PETAL_SERIALIZER);

		id = IRuneAltarRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, RUNE_TYPE);
		register(r, id, RUNE_SERIALIZER);
		register(r, prefix("runic_altar_head"), RUNE_HEAD_SERIALIZER);

		id = ITerraPlateRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, TERRA_PLATE_TYPE);
		register(r, id, TERRA_PLATE_SERIALIZER);

		id = IOrechidRecipe.TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, ORECHID_TYPE);
		register(r, id, ORECHID_SERIALIZER);

		id = IOrechidRecipe.IGNEM_TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, ORECHID_IGNEM_TYPE);
		register(r, id, ORECHID_IGNEM_SERIALIZER);

		id = IOrechidRecipe.MARIMORPHOSIS_TYPE_ID;
		Registry.register(Registry.RECIPE_TYPE, id, MARIMORPHOSIS_TYPE);
		register(r, id, MARIMORPHOSIS_SERIALIZER);
	}

	private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}

	public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipes(Level world, RecipeType<T> type) {
		return ((AccessorRecipeManager) world.getRecipeManager()).botania_getAll(type);
	}
}
