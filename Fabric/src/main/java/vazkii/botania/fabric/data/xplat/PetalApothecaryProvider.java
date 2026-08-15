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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.PetalApothecaryRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.data.util.BotaniaRecipeHelper;
import vazkii.botania.fabric.data.FabricDatagenInitializer;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PetalApothecaryProvider extends FabricRecipeProvider {
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
		Ingredient runeWater = Ingredient.of(BotaniaItems.RUNE_OF_WATER);
		Ingredient runeFire = Ingredient.of(BotaniaItems.RUNE_OF_FIRE);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.RUNE_OF_EARTH);
		Ingredient runeAir = Ingredient.of(BotaniaItems.RUNE_OF_AIR);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.RUNE_OF_SPRING);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.RUNE_OF_SUMMER);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.RUNE_OF_AUTUMN);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.RUNE_OF_WINTER);
		Ingredient runeMana = Ingredient.of(BotaniaItems.RUNE_OF_MANA);
		Ingredient runeLust = Ingredient.of(BotaniaItems.RUNE_OF_LUST);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.RUNE_OF_GLUTTONY);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.RUNE_OF_GREED);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.RUNE_OF_SLOTH);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.RUNE_OF_WRATH);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.RUNE_OF_ENVY);
		Ingredient runePride = Ingredient.of(BotaniaItems.RUNE_OF_PRIDE);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.REDSTONE_ROOT);
		Ingredient pixieDust = Ingredient.of(ConventionalBotaniaTags.Items.PIXIE_DUSTS);
		Ingredient enderEssence = Ingredient.of(BotaniaTags.Items.ENDER_ESSENCES);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.GAIA_SPIRIT);

		make(consumer, BotaniaBlocks.PURE_DAISY, white, white, white, white);
		make(consumer, BotaniaBlocks.MANASTAR, lightBlue, green, red, cyan);

		make(consumer, BotaniaBlocks.ENDOFLAME, brown, brown, red, lightGray);
		make(consumer, BotaniaBlocks.HYDROANGEAS, blue, blue, cyan, cyan);
		make(consumer, BotaniaBlocks.THERMALILY, red, orange, orange, runeEarth, runeFire);
		make(consumer, BotaniaBlocks.ROSA_ARCANA, pink, pink, purple, purple, lime, runeMana);
		make(consumer, BotaniaBlocks.MUNCHDEW, lime, lime, red, red, green, runeGluttony);
		make(consumer, BotaniaBlocks.ENTROPINNYUM, red, red, gray, gray, white, white, runeWrath, runeFire);
		make(consumer, BotaniaBlocks.KEKIMURUS, white, white, orange, orange, brown, brown, runeGluttony, pixieDust);
		make(consumer, BotaniaBlocks.GOURMARYLLIS, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.NARSLIMMUS, lime, lime, green, green, black, runeSummer, runeWater);
		make(consumer, BotaniaBlocks.SPECTROLUS, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		make(consumer, BotaniaBlocks.RAFFLOWSIA, purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		make(consumer, BotaniaBlocks.SHULK_ME_NOT, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		make(consumer, BotaniaBlocks.DANDELIFEON, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, redstoneRoot, gaiaSpirit);

		make(consumer, BotaniaBlocks.JADED_AMARANTHUS, purple, lime, green, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.BELLETHORNE, red, red, red, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.DREADTHORNE, black, black, black, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.HEISEI_DREAM, magenta, magenta, purple, pink, runeWrath, pixieDust);
		make(consumer, BotaniaBlocks.TIGERSEYE, yellow, brown, orange, lime, runeAutumn);

		make(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), BotaniaBlocks.ORECHID,
				gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.ORECHID_IGNEM, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.FALLEN_KANADE, white, white, yellow, yellow, orange, runeSpring);
		make(consumer, BotaniaBlocks.EXOFLAME, red, red, gray, lightGray, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.AGRICARNATION, lime, lime, green, yellow, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.HOPPERHOCK, gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.TANGLEBERRIE, cyan, cyan, gray, lightGray, runeAir, runeEarth);
		make(consumer, BotaniaBlocks.JIYUULIA, pink, pink, purple, lightGray, runeWater, runeAir);
		make(consumer, BotaniaBlocks.RANNUNCARPUS, orange, orange, yellow, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.HYACIDUS, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		make(consumer, BotaniaBlocks.POLLIDISIAC, red, red, pink, pink, orange, runeLust, runeFire);
		make(consumer, BotaniaBlocks.CLAYCONIA, lightGray, lightGray, gray, cyan, runeEarth);
		make(consumer, BotaniaBlocks.LOONIUM, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.DAFFOMILL, white, white, brown, yellow, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.VINCULOTUS, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		make(consumer, BotaniaBlocks.SPECTRANTHEMUM, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, enderEssence);
		make(consumer, BotaniaBlocks.MEDUMONE, brown, brown, gray, gray, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.MARIMORPHOSIS, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		make(consumer, BotaniaBlocks.BUBBELL, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		make(consumer, BotaniaBlocks.SOLEGNOLIA, brown, brown, red, blue, redstoneRoot);
		make(consumer, BotaniaBlocks.BERGAMUTE, orange, green, green, redstoneRoot);
		make(consumer, BotaniaBlocks.LABELLIA, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.DAYBLOOM_MOTIF, yellow, yellow, orange, lightBlue);
		make(consumer, BotaniaBlocks.NIGHTSHADE_MOTIF, black, black, purple, gray);

		ItemStack vazkiiHead = new ItemStack(Items.PLAYER_HEAD);
		vazkiiHead.set(DataComponents.PROFILE,
				new ResolvableProfile(Optional.of("Vazkii"), Optional.empty(), new PropertyMap()));
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE, "vazkii_head"),
				new PetalApothecaryRecipe(vazkiiHead, DEFAULT_REAGENT, inputs), null);
	}

	protected static void make(RecipeOutput consumer, ItemLike output, Ingredient... ingredients) {
		consumer.accept(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE, output),
				new PetalApothecaryRecipe(new ItemStack(output), DEFAULT_REAGENT, ingredients), null);
	}

}
