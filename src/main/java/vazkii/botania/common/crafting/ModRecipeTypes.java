/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;

import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.mixin.AccessorRecipeManager;

import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModRecipeTypes {
	public static final IRecipeType<IManaInfusionRecipe> MANA_INFUSION_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeManaInfusion> MANA_INFUSION_SERIALIZER = new RecipeManaInfusion.Serializer();

	public static final IRecipeType<IElvenTradeRecipe> ELVEN_TRADE_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeElvenTrade> ELVEN_TRADE_SERIALIZER = new RecipeElvenTrade.Serializer();
	public static final SpecialRecipeSerializer<LexiconElvenTradeRecipe> LEXICON_ELVEN_TRADE_SERIALIZER = new SpecialRecipeSerializer<>(LexiconElvenTradeRecipe::new);

	public static final IRecipeType<IPureDaisyRecipe> PURE_DAISY_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipePureDaisy> PURE_DAISY_SERIALIZER = new RecipePureDaisy.Serializer();

	public static final IRecipeType<IBrewRecipe> BREW_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeBrew> BREW_SERIALIZER = new RecipeBrew.Serializer();

	public static final IRecipeType<IPetalRecipe> PETAL_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipePetals> PETAL_SERIALIZER = new RecipePetals.Serializer();

	public static final IRecipeType<IRuneAltarRecipe> RUNE_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<RecipeRuneAltar> RUNE_SERIALIZER = new RecipeRuneAltar.Serializer();
	public static final IRecipeSerializer<HeadRecipe> RUNE_HEAD_SERIALIZER = new HeadRecipe.Serializer();

	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
		ResourceLocation id = prefix("elven_trade");
		Registry.register(Registry.RECIPE_TYPE, id, ELVEN_TRADE_TYPE);
		evt.getRegistry().register(ELVEN_TRADE_SERIALIZER.setRegistryName(id));
		evt.getRegistry().register(LEXICON_ELVEN_TRADE_SERIALIZER.setRegistryName(prefix("elven_trade_lexicon")));

		id = prefix("mana_infusion");
		Registry.register(Registry.RECIPE_TYPE, id, MANA_INFUSION_TYPE);
		evt.getRegistry().register(MANA_INFUSION_SERIALIZER.setRegistryName(id));

		id = prefix("pure_daisy");
		Registry.register(Registry.RECIPE_TYPE, id, PURE_DAISY_TYPE);
		evt.getRegistry().register(PURE_DAISY_SERIALIZER.setRegistryName(id));

		id = prefix("brew");
		Registry.register(Registry.RECIPE_TYPE, id, BREW_TYPE);
		evt.getRegistry().register(BREW_SERIALIZER.setRegistryName(id));

		id = prefix("petal_apothecary");
		Registry.register(Registry.RECIPE_TYPE, id, PETAL_TYPE);
		evt.getRegistry().register(PETAL_SERIALIZER.setRegistryName(id));

		id = prefix("runic_altar");
		Registry.register(Registry.RECIPE_TYPE, id, RUNE_TYPE);
		evt.getRegistry().register(RUNE_SERIALIZER.setRegistryName(id));
		evt.getRegistry().register(RUNE_HEAD_SERIALIZER.setRegistryName(prefix("runic_altar_head")));
	}

	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}

	public static <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> getRecipes(World world, IRecipeType<T> type) {
		return ((AccessorRecipeManager) world.getRecipeManager()).callGetRecipes(type);
	}
}
