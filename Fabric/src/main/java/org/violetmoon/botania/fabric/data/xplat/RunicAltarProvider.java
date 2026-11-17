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
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.RunicAltarRecipe;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.data.util.BotaniaRecipeHelper;

import java.util.concurrent.CompletableFuture;

public class RunicAltarProvider extends FabricRecipeProvider {

	private static final Ingredient[] NO_CATALYSTS = {};
	private static final Ingredient DEFAULT_REAGENT = Ingredient.of(BotaniaBlocks.LIVINGROCK);

	public RunicAltarProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public String getName() {
		return "Botania runic altar recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		Ingredient manaSteel = Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS);
		Ingredient manaPowder = Ingredient.of(ConventionalBotaniaTags.Items.MANA_DUSTS);

		defaultReagent(consumer, BotaniaItems.RUNE_OF_WATER, 2, costTier1,
				manaPowder, manaSteel, Ingredient.of(Items.BONE_MEAL), Ingredient.of(Blocks.SUGAR_CANE),
				Ingredient.of(ConventionalItemTags.FISHING_ROD_TOOLS));
		defaultReagent(consumer, BotaniaItems.RUNE_OF_FIRE, 2, costTier1,
				manaPowder, manaSteel, Ingredient.of(ConventionalItemTags.NETHER_BRICKS),
				Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.COAL, Items.CHARCOAL));
		defaultReagent(consumer, BotaniaItems.RUNE_OF_EARTH, 2, costTier1,
				manaPowder, manaSteel, Ingredient.of(Blocks.STONE), Ingredient.of(Blocks.MUD),
				Ingredient.of(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM));
		defaultReagent(consumer, BotaniaItems.RUNE_OF_AIR, 2, costTier1,
				manaPowder, manaSteel, Ingredient.of(ItemTags.WOOL_CARPETS), Ingredient.of(Items.FEATHER),
				Ingredient.of(Items.STRING));

		Ingredient fire = Ingredient.of(BotaniaItems.RUNE_OF_FIRE);
		Ingredient water = Ingredient.of(BotaniaItems.RUNE_OF_WATER);
		Ingredient earth = Ingredient.of(BotaniaItems.RUNE_OF_EARTH);
		Ingredient air = Ingredient.of(BotaniaItems.RUNE_OF_AIR);

		Ingredient sapling = Ingredient.of(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.of(ItemTags.LEAVES);
		Ingredient sand = Ingredient.of(ItemTags.SAND);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_SPRING, costTier2,
				new Ingredient[] { sapling, sapling, sapling, Ingredient.of(Items.WHEAT) }, water, fire);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_SUMMER, costTier2,
				new Ingredient[] { sand, sand, Ingredient.of(Items.SLIME_BALL), Ingredient.of(Items.MELON_SLICE) },
				earth, air);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_AUTUMN, costTier2,
				new Ingredient[] { leaves, leaves, leaves, Ingredient.of(Items.SPIDER_EYE) }, fire, air);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_WINTER, costTier2,
				new Ingredient[] { Ingredient.of(Blocks.SNOW_BLOCK), Ingredient.of(Blocks.SNOW_BLOCK),
						Ingredient.of(ItemTags.WOOL), Ingredient.of(Blocks.CAKE) },
				water, earth);

		Ingredient spring = Ingredient.of(BotaniaItems.RUNE_OF_SPRING);
		Ingredient summer = Ingredient.of(BotaniaItems.RUNE_OF_SUMMER);
		Ingredient autumn = Ingredient.of(BotaniaItems.RUNE_OF_AUTUMN);
		Ingredient winter = Ingredient.of(BotaniaItems.RUNE_OF_WINTER);

		defaultReagent(consumer, BotaniaItems.RUNE_OF_MANA, 1, costTier2,
				manaSteel, manaSteel, manaSteel, manaSteel, manaSteel,
				Ingredient.of(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS));

		Ingredient manaDiamond = Ingredient.of(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS);
		Ingredient manaQuartz = Ingredient.of(ConventionalBotaniaTags.Items.MANA_QUARTZ_GEMS);

		Ingredient[] tier3Ingredients = { manaDiamond, manaDiamond, manaQuartz };
		defaultReagent(consumer, BotaniaItems.RUNE_OF_LUST, costTier3, tier3Ingredients, summer, air);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_GLUTTONY, costTier3, tier3Ingredients, winter, fire);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_GREED, costTier3, tier3Ingredients, spring, water);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_SLOTH, costTier3, tier3Ingredients, autumn, air);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_WRATH, costTier3, tier3Ingredients, winter, earth);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_ENVY, costTier3, tier3Ingredients, winter, water);
		defaultReagent(consumer, BotaniaItems.RUNE_OF_PRIDE, costTier3, tier3Ingredients, summer, fire);

		consumer.accept(idFor(Items.PLAYER_HEAD),
				new HeadRecipe(new ItemStack(Items.PLAYER_HEAD), DEFAULT_REAGENT, 22500,
						Ingredient.of(Items.SKELETON_SKULL), Ingredient.of(ConventionalBotaniaTags.Items.PIXIE_DUSTS),
						Ingredient.of(ConventionalItemTags.PRISMARINE_GEMS),
						Ingredient.of(Items.NAME_TAG, Items.WRITTEN_BOOK), Ingredient.of(Items.GOLDEN_APPLE)),
				null);
	}

	private static ResourceLocation idFor(ItemLike item) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.RUNIC_ALTAR_TYPE, item);
	}

	protected void defaultReagent(RecipeOutput consumer, ItemLike output, int count, int mana,
			Ingredient[] ingredients, Ingredient[] catalysts) {
		consumer.accept(idFor(output),
				new RunicAltarRecipe(new ItemStack(output, count), DEFAULT_REAGENT, mana, ingredients, catalysts),
				null);
	}

	protected void defaultReagent(RecipeOutput consumer, ItemLike output, int count, int mana,
			Ingredient... ingredients) {
		defaultReagent(consumer, output, count, mana, ingredients, NO_CATALYSTS);
	}

	protected void defaultReagent(RecipeOutput consumer, ItemLike output, int mana,
			Ingredient[] ingredients, Ingredient... catalysts) {
		defaultReagent(consumer, output, 1, mana, ingredients, catalysts);
	}
}
