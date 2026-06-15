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

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.RunicAltarRecipe;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class RunicAltarProvider extends BotaniaRecipeProvider {
	public RunicAltarProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
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

		Ingredient stone = Ingredient.of(Blocks.STONE);
		defaultReagent(consumer, idFor("water"), new ItemStack(BotaniaItems.RUNE_OF_WATER, 2), costTier1,
				manaPowder, manaSteel, Ingredient.of(Items.BONE_MEAL), Ingredient.of(Blocks.SUGAR_CANE),
				Ingredient.of(ConventionalItemTags.FISHING_ROD_TOOLS));
		defaultReagent(consumer, idFor("fire"), new ItemStack(BotaniaItems.RUNE_OF_FIRE, 2), costTier1,
				manaPowder, manaSteel, Ingredient.of(ConventionalItemTags.NETHER_BRICKS),
				Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.COAL, Items.CHARCOAL));
		defaultReagent(consumer, idFor("earth"), new ItemStack(BotaniaItems.RUNE_OF_EARTH, 2), costTier1,
				manaPowder, manaSteel, stone, Ingredient.of(Blocks.MUD),
				Ingredient.of(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM));
		defaultReagent(consumer, idFor("air"), new ItemStack(BotaniaItems.RUNE_OF_AIR, 2), costTier1,
				manaPowder, manaSteel, Ingredient.of(ItemTags.WOOL_CARPETS), Ingredient.of(Items.FEATHER),
				Ingredient.of(Items.STRING));

		Ingredient fire = Ingredient.of(BotaniaItems.RUNE_OF_FIRE);
		Ingredient water = Ingredient.of(BotaniaItems.RUNE_OF_WATER);
		Ingredient earth = Ingredient.of(BotaniaItems.RUNE_OF_EARTH);
		Ingredient air = Ingredient.of(BotaniaItems.RUNE_OF_AIR);

		Ingredient sapling = Ingredient.of(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.of(ItemTags.LEAVES);
		Ingredient sand = Ingredient.of(ItemTags.SAND);
		defaultReagent(consumer, idFor("spring"), new ItemStack(BotaniaItems.RUNE_OF_SPRING), costTier2,
				new Ingredient[] { sapling, sapling, sapling, Ingredient.of(Items.WHEAT) }, water, fire);
		defaultReagent(consumer, idFor("summer"), new ItemStack(BotaniaItems.RUNE_OF_SUMMER), costTier2,
				new Ingredient[] { sand, sand, Ingredient.of(Items.SLIME_BALL), Ingredient.of(Items.MELON_SLICE) },
				earth, air);
		defaultReagent(consumer, idFor("autumn"), new ItemStack(BotaniaItems.RUNE_OF_AUTUMN), costTier2,
				new Ingredient[] { leaves, leaves, leaves, Ingredient.of(Items.SPIDER_EYE) }, fire, air);
		defaultReagent(consumer, idFor("winter"), new ItemStack(BotaniaItems.RUNE_OF_WINTER), costTier2,
				new Ingredient[] { Ingredient.of(Blocks.SNOW_BLOCK), Ingredient.of(Blocks.SNOW_BLOCK),
						Ingredient.of(ItemTags.WOOL), Ingredient.of(Blocks.CAKE) },
				water, earth);

		Ingredient spring = Ingredient.of(BotaniaItems.RUNE_OF_SPRING);
		Ingredient summer = Ingredient.of(BotaniaItems.RUNE_OF_SUMMER);
		Ingredient autumn = Ingredient.of(BotaniaItems.RUNE_OF_AUTUMN);
		Ingredient winter = Ingredient.of(BotaniaItems.RUNE_OF_WINTER);

		defaultReagent(consumer, idFor("mana"), new ItemStack(BotaniaItems.RUNE_OF_MANA), costTier2,
				manaSteel, manaSteel, manaSteel, manaSteel, manaSteel, Ingredient.of(
						ConventionalBotaniaTags.Items.MANA_PEARL_GEMS));

		Ingredient manaDiamond = Ingredient.of(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS);
		Ingredient manaQuartz = Ingredient.of(ConventionalBotaniaTags.Items.MANA_QUARTZ_GEMS);

		defaultReagent(consumer, idFor("lust"), new ItemStack(BotaniaItems.RUNE_OF_LUST), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, summer, air);
		defaultReagent(consumer, idFor("gluttony"), new ItemStack(BotaniaItems.RUNE_OF_GLUTTONY), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, winter, fire);
		defaultReagent(consumer, idFor("greed"), new ItemStack(BotaniaItems.RUNE_OF_GREED), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, spring, water);
		defaultReagent(consumer, idFor("sloth"), new ItemStack(BotaniaItems.RUNE_OF_SLOTH), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, autumn, air);
		defaultReagent(consumer, idFor("wrath"), new ItemStack(BotaniaItems.RUNE_OF_WRATH), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, winter, earth);
		defaultReagent(consumer, idFor("envy"), new ItemStack(BotaniaItems.RUNE_OF_ENVY), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, winter, water);
		defaultReagent(consumer, idFor("pride"), new ItemStack(BotaniaItems.RUNE_OF_PRIDE), costTier3,
				new Ingredient[] { manaDiamond, manaDiamond, manaQuartz }, summer, fire);

		consumer.accept(idFor("head"), new HeadRecipe(new ItemStack(Items.PLAYER_HEAD), DEFAULT_REAGENT, 22500,
				Ingredient.of(Items.SKELETON_SKULL), Ingredient.of(ConventionalBotaniaTags.Items.PIXIE_DUSTS),
				Ingredient.of(ConventionalItemTags.PRISMARINE_GEMS), Ingredient.of(Items.NAME_TAG, Items.WRITTEN_BOOK),
				Ingredient.of(Items.GOLDEN_APPLE)), null);
	}

	private static ResourceLocation idFor(String s) {
		return botaniaRL("runic_altar/" + s);
	}

	protected static Ingredient DEFAULT_REAGENT = Ingredient.of(BotaniaBlocks.LIVINGROCK);

	protected static void defaultReagent(RecipeOutput consumer, ResourceLocation id, ItemStack output, int mana,
			Ingredient... ingredients) {
		consumer.accept(id, new RunicAltarRecipe(output, DEFAULT_REAGENT, mana, ingredients, new Ingredient[] {}), null);
	}

	protected static void defaultReagent(RecipeOutput consumer, ResourceLocation id, ItemStack output, int mana,
			Ingredient[] ingredients, Ingredient... catalysts) {
		consumer.accept(id, new RunicAltarRecipe(output, DEFAULT_REAGENT, mana, ingredients, catalysts), null);
	}
}
