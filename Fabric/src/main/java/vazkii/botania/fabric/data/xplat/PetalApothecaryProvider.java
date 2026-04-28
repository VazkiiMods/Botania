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

import com.mojang.authlib.properties.PropertyMap;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AllModsLoadedResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.NotResourceCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.PetalApothecaryRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PetalApothecaryProvider extends FabricRecipeProvider {
	@SuppressWarnings("UnstableApiUsage")
	public static final NotResourceCondition GOG_NOT_LOADED_CONDITION =
			new NotResourceCondition(new AllModsLoadedResourceCondition(List.of(BotaniaAPI.GOG_MODID)));

	private static final Ingredient DEFAULT_REAGENT = Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);

	public PetalApothecaryProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public String getName() {
		return "Botania petal apothecary recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		Ingredient white = Ingredient.of(BotaniaTags.Items.PETALS_WHITE);
		Ingredient orange = Ingredient.of(BotaniaTags.Items.PETALS_ORANGE);
		Ingredient magenta = Ingredient.of(BotaniaTags.Items.PETALS_MAGENTA);
		Ingredient lightBlue = Ingredient.of(BotaniaTags.Items.PETALS_LIGHT_BLUE);
		Ingredient yellow = Ingredient.of(BotaniaTags.Items.PETALS_YELLOW);
		Ingredient lime = Ingredient.of(BotaniaTags.Items.PETALS_LIME);
		Ingredient pink = Ingredient.of(BotaniaTags.Items.PETALS_PINK);
		Ingredient gray = Ingredient.of(BotaniaTags.Items.PETALS_GRAY);
		Ingredient lightGray = Ingredient.of(BotaniaTags.Items.PETALS_LIGHT_GRAY);
		Ingredient cyan = Ingredient.of(BotaniaTags.Items.PETALS_CYAN);
		Ingredient purple = Ingredient.of(BotaniaTags.Items.PETALS_PURPLE);
		Ingredient blue = Ingredient.of(BotaniaTags.Items.PETALS_BLUE);
		Ingredient brown = Ingredient.of(BotaniaTags.Items.PETALS_BROWN);
		Ingredient green = Ingredient.of(BotaniaTags.Items.PETALS_GREEN);
		Ingredient red = Ingredient.of(BotaniaTags.Items.PETALS_RED);
		Ingredient black = Ingredient.of(BotaniaTags.Items.PETALS_BLACK);
		Ingredient runeWater = Ingredient.of(BotaniaItems.runeWater);
		Ingredient runeFire = Ingredient.of(BotaniaItems.runeFire);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.runeEarth);
		Ingredient runeAir = Ingredient.of(BotaniaItems.runeAir);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.runeSpring);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.runeSummer);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.runeAutumn);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.runeWinter);
		Ingredient runeMana = Ingredient.of(BotaniaItems.runeMana);
		Ingredient runeLust = Ingredient.of(BotaniaItems.runeLust);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.runeGluttony);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.runeGreed);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.runeSloth);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.runeWrath);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.runeEnvy);
		Ingredient runePride = Ingredient.of(BotaniaItems.runePride);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.of(ConventionalBotaniaTags.Items.PIXIE_DUSTS);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.lifeEssence);

		make(consumer, BotaniaBlocks.pureDaisy, white, white, white, white);
		make(consumer, BotaniaBlocks.manastar, lightBlue, green, red, cyan);

		make(consumer, BotaniaBlocks.endoflame, brown, brown, red, lightGray);
		make(consumer, BotaniaBlocks.hydroangeas, blue, blue, cyan, cyan);
		make(consumer, BotaniaBlocks.thermalily, red, orange, orange, runeEarth, runeFire);
		make(consumer, BotaniaBlocks.rosaArcana, pink, pink, purple, purple, lime, runeMana);
		make(consumer, BotaniaBlocks.munchdew, lime, lime, red, red, green, runeGluttony);
		make(consumer, BotaniaBlocks.entropinnyum, red, red, gray, gray, white, white, runeWrath, runeFire);
		make(consumer, BotaniaBlocks.kekimurus, white, white, orange, orange, brown, brown, runeGluttony, pixieDust);
		make(consumer, BotaniaBlocks.gourmaryllis, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.narslimmus, lime, lime, green, green, black, runeSummer, runeWater);
		make(consumer, BotaniaBlocks.spectrolus, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		make(consumer, BotaniaBlocks.rafflowsia, purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		make(consumer, BotaniaBlocks.shulkMeNot, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		make(consumer, BotaniaBlocks.dandelifeon, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, redstoneRoot, gaiaSpirit);

		make(consumer, BotaniaBlocks.jadedAmaranthus, purple, lime, green, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.bellethorn, red, red, red, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.dreadthorn, black, black, black, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.heiseiDream, magenta, magenta, purple, pink, runeWrath, pixieDust);
		make(consumer, BotaniaBlocks.tigerseye, yellow, brown, orange, lime, runeAutumn);

		make(withConditions(consumer, GOG_NOT_LOADED_CONDITION), BotaniaBlocks.orechid,
				gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.orechidIgnem, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.fallenKanade, white, white, yellow, yellow, orange, runeSpring);
		make(consumer, BotaniaBlocks.exoflame, red, red, gray, lightGray, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.agricarnation, lime, lime, green, yellow, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.hopperhock, gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.tangleberrie, cyan, cyan, gray, lightGray, runeAir, runeEarth);
		make(consumer, BotaniaBlocks.jiyuulia, pink, pink, purple, lightGray, runeWater, runeAir);
		make(consumer, BotaniaBlocks.rannuncarpus, orange, orange, yellow, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.hyacidus, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		make(consumer, BotaniaBlocks.pollidisiac, red, red, pink, pink, orange, runeLust, runeFire);
		make(consumer, BotaniaBlocks.clayconia, lightGray, lightGray, gray, cyan, runeEarth);
		make(consumer, BotaniaBlocks.loonium, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.daffomill, white, white, brown, yellow, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.vinculotus, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		make(consumer, BotaniaBlocks.spectranthemum, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.medumone, brown, brown, gray, gray, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.marimorphosis, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		make(consumer, BotaniaBlocks.bubbell, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		make(consumer, BotaniaBlocks.solegnolia, brown, brown, red, blue, redstoneRoot);
		make(consumer, BotaniaBlocks.bergamute, orange, green, green, redstoneRoot);
		make(consumer, BotaniaBlocks.labellia, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.motifDaybloom, yellow, yellow, orange, lightBlue);
		make(consumer, BotaniaBlocks.motifNightshade, black, black, purple, gray);

		ItemStack vazkiiHead = new ItemStack(Items.PLAYER_HEAD);
		vazkiiHead.set(DataComponents.PROFILE,
				new ResolvableProfile(Optional.of("Vazkii"), Optional.empty(), new PropertyMap()));
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(BotaniaAPI.botaniaRL("vazkii_head").withPrefix("petal_apothecary/"),
				new PetalApothecaryRecipe(vazkiiHead, DEFAULT_REAGENT, inputs), null);
	}

	protected static void make(RecipeOutput consumer, ItemLike output, Ingredient... ingredients) {
		consumer.accept(BuiltInRegistries.ITEM.getKey(output.asItem()).withPrefix("petal_apothecary/"),
				new PetalApothecaryRecipe(new ItemStack(output), DEFAULT_REAGENT, ingredients), null);
	}

}
