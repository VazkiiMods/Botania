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
import vazkii.botania.common.crafting.recipe.StateCopyPureDaisyRecipe;
import vazkii.botania.mixin.AccessorRecipeManager;

import java.util.Map;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModRecipeTypes {
	public static final RecipeType<IManaInfusionRecipe> MANA_INFUSION_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();

	public static final RecipeType<IElvenTradeRecipe> ELVEN_TRADE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SimpleRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SimpleRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	public static final RecipeType<IPureDaisyRecipe> PURE_DAISY_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipePureDaisy> PURE_DAISY_SERIALIZER = new RecipePureDaisy.Serializer();
	public static final RecipeSerializer<StateCopyPureDaisyRecipe> COPYING_PURE_DAISY_SERIALIZER = new StateCopyPureDaisyRecipe.Serializer();

	public static final RecipeType<IBrewRecipe> BREW_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeBrew> BREW_SERIALIZER = new RecipeBrew.Serializer();

	public static final RecipeType<IPetalRecipe> PETAL_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipePetals> PETAL_SERIALIZER = new RecipePetals.Serializer();

	public static final RecipeType<IRuneAltarRecipe> RUNE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeRuneAltar> RUNE_SERIALIZER = new RecipeRuneAltar.Serializer();
	public static final RecipeSerializer<HeadRecipe> RUNE_HEAD_SERIALIZER = new HeadRecipe.Serializer();

	public static final RecipeType<ITerraPlateRecipe> TERRA_PLATE_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeTerraPlate> TERRA_PLATE_SERIALIZER = new RecipeTerraPlate.Serializer();

	public static final RecipeType<RecipeOrechid> ORECHID_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeOrechid> ORECHID_SERIALIZER = new RecipeOrechid.Serializer();

	public static final RecipeType<RecipeOrechidIgnem> ORECHID_IGNEM_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeOrechidIgnem> ORECHID_IGNEM_SERIALIZER = new RecipeOrechidIgnem.Serializer();

	public static final RecipeType<RecipeMarimorphosis> MARIMORPHOSIS_TYPE = new ModRecipeType<>();
	public static final RecipeSerializer<RecipeMarimorphosis> MARIMORPHOSIS_SERIALIZER = new RecipeMarimorphosis.Serializer();

	public static void submitRecipeTypes(BiConsumer<RecipeType<?>, ResourceLocation> r) {
		r.accept(ELVEN_TRADE_TYPE, IElvenTradeRecipe.TYPE_ID);
		r.accept(MANA_INFUSION_TYPE, IManaInfusionRecipe.TYPE_ID);
		r.accept(PURE_DAISY_TYPE, IPureDaisyRecipe.TYPE_ID);
		r.accept(BREW_TYPE, IBrewRecipe.TYPE_ID);
		r.accept(PETAL_TYPE, IPetalRecipe.TYPE_ID);
		r.accept(RUNE_TYPE, IRuneAltarRecipe.TYPE_ID);
		r.accept(TERRA_PLATE_TYPE, ITerraPlateRecipe.TYPE_ID);
		r.accept(ORECHID_TYPE, IOrechidRecipe.TYPE_ID);
		r.accept(ORECHID_IGNEM_TYPE, IOrechidRecipe.IGNEM_TYPE_ID);
		r.accept(MARIMORPHOSIS_TYPE, IOrechidRecipe.MARIMORPHOSIS_TYPE_ID);
	}

	public static void submitRecipeSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
		r.accept(ELVEN_TRADE_SERIALIZER, IElvenTradeRecipe.TYPE_ID);
		r.accept(LEXICON_ELVEN_TRADE_SERIALIZER, prefix("elven_trade_lexicon"));
		r.accept(MANA_INFUSION_SERIALIZER, IManaInfusionRecipe.TYPE_ID);
		r.accept(PURE_DAISY_SERIALIZER, IPureDaisyRecipe.TYPE_ID);
		r.accept(COPYING_PURE_DAISY_SERIALIZER, prefix("state_copying_pure_daisy"));
		r.accept(BREW_SERIALIZER, IBrewRecipe.TYPE_ID);
		r.accept(PETAL_SERIALIZER, IPetalRecipe.TYPE_ID);
		r.accept(RUNE_SERIALIZER, IRuneAltarRecipe.TYPE_ID);
		r.accept(RUNE_HEAD_SERIALIZER, prefix("runic_altar_head"));
		r.accept(TERRA_PLATE_SERIALIZER, ITerraPlateRecipe.TYPE_ID);
		r.accept(ORECHID_SERIALIZER, IOrechidRecipe.TYPE_ID);
		r.accept(ORECHID_IGNEM_SERIALIZER, IOrechidRecipe.IGNEM_TYPE_ID);
		r.accept(MARIMORPHOSIS_SERIALIZER, IOrechidRecipe.MARIMORPHOSIS_TYPE_ID);
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
