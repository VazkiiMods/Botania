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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.ManaInfusionRecipe;
import vazkii.botania.common.crafting.StateIngredients;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaInfusionProvider extends FabricRecipeProvider {
	public ManaInfusionProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public String getName() {
		return "Botania mana pool recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		normal(consumer, BotaniaItems.manaSteel, ConventionalItemTags.IRON_INGOTS, 3000);
		normal(consumer, BotaniaBlocks.manasteelBlock, ConventionalItemTags.STORAGE_BLOCKS_IRON, 27000);

		normal(consumer, BotaniaItems.manaPearl, Items.ENDER_PEARL, 6000);

		normal(consumer, BotaniaItems.manaDiamond, ConventionalItemTags.DIAMOND_GEMS, 10000);
		normal(consumer, BotaniaBlocks.manaDiamondBlock, ConventionalItemTags.STORAGE_BLOCKS_DIAMOND, 90000);

		normal(consumer, id("mana_powder_from_dust"), "botania:mana_powder", BotaniaItems.manaPowder,
				Ingredient.of(BotaniaTags.Items.MANA_POWDER_SOURCE_DUSTS), 500);
		normal(consumer, id("mana_powder_from_dye"), "botania:mana_powder", BotaniaItems.manaPowder,
				Ingredient.of(ConventionalItemTags.DYES), 400);

		normal(consumer, BotaniaBlocks.pistonRelay, Blocks.PISTON, 15000);
		normal(consumer, BotaniaItems.manaCookie, Items.COOKIE, 20000);
		normal(consumer, BotaniaItems.grassSeeds, Blocks.SHORT_GRASS, 2500);
		normal(consumer, BotaniaItems.podzolSeeds, Blocks.DEAD_BUSH, 2500);

		normal(consumer, BotaniaItems.mycelSeeds, ConventionalItemTags.MUSHROOMS, 6500);

		normal(consumer, BotaniaItems.manaQuartz, Items.QUARTZ, 250);
		normal(consumer, BotaniaBlocks.tinyPotato, Items.POTATO, 1337);

		normal(consumer, BotaniaBlocks.manaGlass, ConventionalItemTags.GLASS_BLOCKS_COLORLESS, 150);
		normal(consumer, BotaniaItems.manaString, Items.STRING, 1250);

		normal(consumer, BotaniaItems.manaBottle, Items.GLASS_BOTTLE, 5000);

		alchemy(consumer, Items.LEATHER, Items.ROTTEN_FLESH, 600);

		cycle(consumer, 40, "log",
				Blocks.OAK_LOG, Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG, Blocks.MANGROVE_LOG,
				Blocks.BIRCH_LOG, Blocks.CHERRY_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG);
		cycle(consumer, 100, "froglight", Blocks.OCHRE_FROGLIGHT, Blocks.VERDANT_FROGLIGHT, Blocks.PEARLESCENT_FROGLIGHT);
		cycle(consumer, 12000, "sapling",
				Blocks.OAK_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.MANGROVE_PROPAGULE,
				Blocks.BIRCH_SAPLING, Blocks.CHERRY_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING);

		deconstruct(consumer, Items.GLOWSTONE_DUST, Blocks.GLOWSTONE);
		deconstruct(consumer, Items.QUARTZ, Blocks.QUARTZ_BLOCK);
		deconstruct(consumer, BotaniaItems.darkQuartz, BotaniaBlocks.darkQuartz);
		deconstruct(consumer, BotaniaItems.manaQuartz, BotaniaBlocks.manaQuartz);
		deconstruct(consumer, BotaniaItems.blazeQuartz, BotaniaBlocks.blazeQuartz);
		deconstruct(consumer, BotaniaItems.lavenderQuartz, BotaniaBlocks.lavenderQuartz);
		deconstruct(consumer, BotaniaItems.redQuartz, BotaniaBlocks.redQuartz);
		deconstruct(consumer, BotaniaItems.elfQuartz, BotaniaBlocks.elfQuartz);
		deconstruct(consumer, BotaniaItems.sunnyQuartz, BotaniaBlocks.sunnyQuartz);

		alchemy(consumer, Blocks.CHISELED_STONE_BRICKS, Blocks.STONE_BRICKS, 150);
		alchemy(consumer, Blocks.ICE, Blocks.SNOW_BLOCK, 2250);

		cycle(consumer, 320, "vine_and_lily_pad", Items.LILY_PAD, Items.VINE);

		cycle(consumer, 200, "fish", Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
		cycle(consumer, 6000, "crop", Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);

		alchemy(consumer, Items.POTATO, Items.POISONOUS_POTATO, 1200);
		alchemy(consumer, Items.NETHER_WART, Items.BLAZE_ROD, 4000);

		cycle(consumer, 200, "gunpowder_and_flint", Items.GUNPOWDER, Items.FLINT);

		alchemy(consumer, Items.NAME_TAG, Items.WRITABLE_BOOK, 6000);

		deconstruct(consumer, Items.STRING, 3, ItemTags.WOOL, 100);

		cycle(consumer, 1200, "cactus_and_slime", Items.SLIME_BALL, Items.CACTUS);

		alchemy(consumer, Items.ENDER_PEARL, Items.GHAST_TEAR, 28000);

		cycle(consumer, 300, "glowstone_and_redstone", Items.GLOWSTONE_DUST, Items.REDSTONE);

		alchemy(consumer, Blocks.SAND, ConventionalItemTags.COBBLESTONES, 50);
		alchemy(consumer, Blocks.RED_SAND, Blocks.TERRACOTTA, 50);

		deconstruct(consumer, Items.CLAY_BALL, Blocks.CLAY);
		deconstruct(consumer, Items.BRICK, Blocks.BRICKS);

		alchemy(consumer, Blocks.COARSE_DIRT, Blocks.DIRT, 120);
		alchemy(consumer, Blocks.SOUL_SOIL, Blocks.SOUL_SAND, 120);

		alchemy(consumer, Blocks.ANDESITE, Blocks.STONE, 200);
		cycle(consumer, 200, "stone", Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.TUFF);
		cycle(consumer, 200, "nether_stone", Blocks.BASALT, Blocks.NETHERRACK, Blocks.BLACKSTONE);

		cycle(consumer, 500, "shrub", Blocks.FERN, Blocks.DEAD_BUSH, Blocks.SHORT_GRASS);

		// NB: No wither rose or sniffer flowers is intentional
		cycle(consumer, 400, "flower", Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID,
				Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP,
				Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
				Blocks.PINK_PETALS, Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY);

		alchemy(consumer, Blocks.SMALL_DRIPLEAF, Items.BIG_DRIPLEAF, 500);
		alchemy(consumer, Blocks.CHORUS_FLOWER, Items.POPPED_CHORUS_FRUIT, 10000);

		cycle(consumer, 240, "berry", Items.APPLE, Items.SWEET_BERRIES, Items.GLOW_BERRIES);

		mini(consumer, BotaniaBlocks.agricarnationChibi, BotaniaBlocks.agricarnation);
		mini(consumer, BotaniaBlocks.clayconiaChibi, BotaniaBlocks.clayconia);
		mini(consumer, BotaniaBlocks.bellethornChibi, BotaniaBlocks.bellethorn);
		mini(consumer, BotaniaBlocks.bubbellChibi, BotaniaBlocks.bubbell);
		mini(consumer, BotaniaBlocks.hopperhockChibi, BotaniaBlocks.hopperhock);
		mini(consumer, BotaniaBlocks.jiyuuliaChibi, BotaniaBlocks.jiyuulia);
		mini(consumer, BotaniaBlocks.tangleberrieChibi, BotaniaBlocks.tangleberrie);
		mini(consumer, BotaniaBlocks.marimorphosisChibi, BotaniaBlocks.marimorphosis);
		mini(consumer, BotaniaBlocks.rannuncarpusChibi, BotaniaBlocks.rannuncarpus);
		mini(consumer, BotaniaBlocks.solegnoliaChibi, BotaniaBlocks.solegnolia);

		alchemy(consumer, BotaniaBlocks.motifHydroangeas, BotaniaBlocks.hydroangeas, 2500);

		conjuration(consumer, Items.REDSTONE, 5000);
		conjuration(consumer, Items.GLOWSTONE_DUST, 5000);
		conjuration(consumer, Items.QUARTZ, 2500);
		conjuration(consumer, Items.COAL, 2100);
		conjuration(consumer, Items.SNOWBALL, 200);
		conjuration(consumer, Blocks.NETHERRACK, 200);
		conjuration(consumer, Blocks.SOUL_SAND, 1500);
		conjuration(consumer, Blocks.GRAVEL, 720);

		conjuration(consumer, Blocks.OAK_LEAVES, 2000);
		conjuration(consumer, Blocks.BIRCH_LEAVES, 2000);
		conjuration(consumer, Blocks.SPRUCE_LEAVES, 2000);
		conjuration(consumer, Blocks.JUNGLE_LEAVES, 2000);
		conjuration(consumer, Blocks.ACACIA_LEAVES, 2000);
		conjuration(consumer, Blocks.DARK_OAK_LEAVES, 2000);
		conjuration(consumer, Blocks.AZALEA_LEAVES, 2000);
		conjuration(consumer, Blocks.FLOWERING_AZALEA_LEAVES, 2000);
		conjuration(consumer, Blocks.MANGROVE_LEAVES, 2000);
		conjuration(consumer, Blocks.CHERRY_LEAVES, 2000);

		conjuration(consumer, Blocks.SHORT_GRASS, 800);
	}

	private static final StateIngredient ALCHEMY = StateIngredients.of(BotaniaBlocks.alchemyCatalyst);
	private static final StateIngredient CONJURATION = StateIngredients.of(BotaniaBlocks.conjurationCatalyst);

	private static void normal(RecipeOutput consumer, ItemLike output, ItemLike input, int mana) {
		normal(consumer, output, ingr(input), mana);
	}

	private static void normal(RecipeOutput consumer, ItemLike output, TagKey<Item> input, int mana) {
		normal(consumer, output, ingr(input), mana);
	}

	private static void normal(RecipeOutput consumer, ItemLike output, Ingredient input, int mana) {
		normal(consumer, id(getItemName(output)), null, output, input, mana);
	}

	private static void normal(RecipeOutput consumer, ResourceLocation id, @Nullable String group, ItemLike output, Ingredient input, int mana) {
		consumer.accept(id, new ManaInfusionRecipe(new ItemStack(output), input, mana, group, null), null);
	}

	private static void alchemy(RecipeOutput consumer, String id, ItemStack output, Ingredient input, int mana, @Nullable String group) {
		consumer.accept(id("alchemy/" + id), new ManaInfusionRecipe(output, input, mana, group, ALCHEMY), null);
	}

	private static void alchemy(RecipeOutput consumer, ItemLike output, ItemLike input, int mana) {
		alchemy(consumer, getItemName(output), new ItemStack(output), ingr(input), mana, null);
	}

	private static void alchemy(RecipeOutput consumer, ItemLike output, TagKey<Item> input, int mana) {
		alchemy(consumer, getItemName(output), new ItemStack(output), ingr(input), mana, null);
	}

	private static void conjuration(RecipeOutput consumer, ItemLike itemLike, int mana) {
		consumer.accept(id("conjuration/" + getItemName(itemLike)),
				new ManaInfusionRecipe(new ItemStack(itemLike, 2), Ingredient.of(itemLike), mana, null, CONJURATION),
				null);
	}

	protected void cycle(RecipeOutput consumer, int cost, String cycleName, ItemLike... items) {
		for (int i = 0; i < items.length; i++) {
			ItemLike inItem = items[i];
			ItemLike outItem = i == items.length - 1 ? items[0] : items[i + 1];
			alchemy(consumer, "%s/%s".formatted(cycleName, getConversionRecipeName(outItem, inItem)),
					new ItemStack(outItem), ingr(inItem), cost, "botania:%s_cycle".formatted(cycleName));
		}
	}

	protected void mini(RecipeOutput consumer, ItemLike mini, ItemLike full) {
		alchemy(consumer, getItemName(mini), new ItemStack(mini), ingr(full), 2500, "botania:flower_shrinking");
	}

	protected void deconstruct(RecipeOutput consumer, ItemLike items, ItemLike block) {
		alchemy(consumer, getItemName(block) + "_deconstruct", new ItemStack(items, 4), ingr(block), 25, "botania:block_deconstruction");
	}

	protected void deconstruct(RecipeOutput consumer, ItemLike items, int count, TagKey<Item> block, int mana) {
		alchemy(consumer, block.location().getPath() + "_deconstruct", new ItemStack(items, count), ingr(block), mana, null);
	}

	protected static ResourceLocation id(String s) {
		return botaniaRL("mana_infusion/" + s);
	}

	protected static Ingredient ingr(ItemLike i) {
		return Ingredient.of(i);
	}

	protected static Ingredient ingr(TagKey<Item> i) {
		return Ingredient.of(i);
	}
}
