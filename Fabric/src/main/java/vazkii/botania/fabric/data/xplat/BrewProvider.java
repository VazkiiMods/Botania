/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.crafting.BotanicalBreweryRecipe;
import vazkii.botania.common.item.BotaniaItems;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BrewProvider extends FabricRecipeProvider {
	public BrewProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public String getName() {
		return "Botania Brew recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput recipeOutput) {
		make(recipeOutput, BotaniaBrews.FLEETFEET,
				Items.NETHER_WART, Items.SUGAR, Items.REDSTONE);
		make(recipeOutput, BotaniaBrews.VIGOR,
				Items.NETHER_WART, Items.BLAZE_POWDER, Items.GLOWSTONE_DUST);
		make(recipeOutput, BotaniaBrews.ADRENALINE,
				Items.NETHER_WART, Items.SUGAR, Items.GOLD_NUGGET);
		make(recipeOutput, BotaniaBrews.MENDING,
				Items.NETHER_WART, Items.GLISTERING_MELON_SLICE, Items.POTATO);
		make(recipeOutput, BotaniaBrews.UPSURGING,
				Items.NETHER_WART, Items.FEATHER, Items.CARROT);
		make(recipeOutput, BotaniaBrews.REVITALIZATION,
				Items.NETHER_WART, Items.GHAST_TEAR, Items.GLOWSTONE_DUST);
		make(recipeOutput, BotaniaBrews.RESTORATION,
				Items.NETHER_WART, Items.GHAST_TEAR, Items.REDSTONE);
		make(recipeOutput, BotaniaBrews.FORTITUDE,
				Items.NETHER_WART, Items.IRON_INGOT, Items.LEATHER);
		make(recipeOutput, BotaniaBrews.MAGMASKIN,
				Items.NETHER_WART, Items.MAGMA_CREAM, Blocks.NETHERRACK);
		make(recipeOutput, BotaniaBrews.GILLS,
				Items.NETHER_WART, Items.PRISMARINE_CRYSTALS, Items.GLOWSTONE_DUST);
		make(recipeOutput, BotaniaBrews.CLOAKING,
				Items.NETHER_WART, Items.SNOWBALL, Items.GLOWSTONE_DUST);
		make(recipeOutput, BotaniaBrews.OWLSIGHT,
				Items.NETHER_WART, Items.SPIDER_EYE, Items.GOLDEN_CARROT);
		make(recipeOutput, BotaniaBrews.SHIELDING,
				Items.NETHER_WART, Items.GOLDEN_APPLE, Items.POTATO);

		make(recipeOutput, BotaniaBrews.OVERLOAD,
				Items.NETHER_WART, Items.BLAZE_POWDER, Items.SUGAR, Items.GLOWSTONE_DUST,
				BotaniaItems.MANASTEEL_INGOT, Items.SPIDER_EYE);
		make(recipeOutput, BotaniaBrews.CROSSED_SOULS,
				Items.NETHER_WART, Blocks.SOUL_SAND, Items.PAPER, Items.APPLE, Items.BONE);
		make(recipeOutput, BotaniaBrews.FEATHER_FEET,
				Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FEATHER),
				Ingredient.of(Items.LEATHER), Ingredient.of(ItemTags.WOOL));
		make(recipeOutput, BotaniaBrews.VANITYS_EMPTINESS,
				Items.NETHER_WART, Items.GUNPOWDER, Items.ROTTEN_FLESH, Items.BONE, Items.STRING, Items.ENDER_PEARL);
		make(recipeOutput, BotaniaBrews.CRIMSON_SHADE,
				Items.NETHER_WART, Items.FERMENTED_SPIDER_EYE, Items.LAPIS_LAZULI, Items.FIRE_CHARGE, Items.IRON_INGOT);
		make(recipeOutput, BotaniaBrews.MARINE_ALLURE,
				Items.NETHER_WART, Items.COD, Items.QUARTZ, Items.GOLDEN_CARROT);
		make(recipeOutput, BotaniaBrews.ABSOLUTION,
				Items.NETHER_WART, Items.QUARTZ, Items.EMERALD, Items.MELON_SLICE);
	}

	private void make(RecipeOutput recipeOutput, Brew brew, ItemLike... inputs) {
		make(recipeOutput, brew, Arrays.stream(inputs).map(Ingredient::of).toArray(Ingredient[]::new));
	}

	private void make(RecipeOutput recipeOutput, Brew brew, Ingredient... inputs) {
		recipeOutput.accept(idFor(BotaniaAPI.instance().getBrewRegistry().getKey(brew).getPath()),
				new BotanicalBreweryRecipe(brew, inputs),
				null);
	}

	private static ResourceLocation idFor(String s) {
		return botaniaRL("brew/" + s);
	}
}
