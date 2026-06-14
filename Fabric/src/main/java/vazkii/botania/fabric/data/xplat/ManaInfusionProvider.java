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
import net.minecraft.core.registries.BuiltInRegistries;
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
	public static final int BLOCK_COST_MULTIPLIER = 9;
	public static final int COST_MANASTEEL = 3000;
	public static final int COST_MANA_DIAMOND = 10000;

	// works out to exactly 100 mana for 3 output items:
	public static final int BASE_MANA_LOSSY_DECONSTRUCTION = 58;
	public static final int BASE_MANA_LOSSLESS_DECONSTRUCTION = 12;

	public static final int COST_LEAVES_DUPLICATION = 2000;

	public ManaInfusionProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public String getName() {
		return "Botania mana pool recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		normal(consumer, BotaniaItems.manaSteel, ConventionalItemTags.IRON_INGOTS, COST_MANASTEEL);
		normal(consumer, BotaniaBlocks.MANASTEEL_BLOCK, ConventionalItemTags.STORAGE_BLOCKS_IRON,
				BLOCK_COST_MULTIPLIER * COST_MANASTEEL);
		normal(consumer, BotaniaItems.manaPearl, Items.ENDER_PEARL, 6000);
		normal(consumer, BotaniaItems.manaDiamond, ConventionalItemTags.DIAMOND_GEMS, COST_MANA_DIAMOND);
		normal(consumer, BotaniaBlocks.MANA_DIAMOND_BLOCK, ConventionalItemTags.STORAGE_BLOCKS_DIAMOND,
				BLOCK_COST_MULTIPLIER * COST_MANA_DIAMOND);

		normalWithGroup(consumer, BotaniaItems.manaPowder, Ingredient.of(BotaniaTags.Items.MANA_POWDER_SOURCE_DUSTS),
				500, "_from_dust");
		normalWithGroup(consumer, BotaniaItems.manaPowder, Ingredient.of(ConventionalItemTags.DYES),
				400, "_from_dye");

		normal(consumer, BotaniaItems.manaCookie, Items.COOKIE, 20000);
		normal(consumer, BotaniaItems.manaQuartz, Items.QUARTZ, 250);
		normal(consumer, BotaniaBlocks.MANAGLASS, ConventionalItemTags.GLASS_BLOCKS_COLORLESS, 150);
		normal(consumer, BotaniaItems.manaString, Items.STRING, 1250);
		normal(consumer, BotaniaItems.manaBottle, Items.GLASS_BOTTLE, 5000);

		normal(consumer, BotaniaBlocks.FORCE_RELAY, Blocks.PISTON, 15000);
		normal(consumer, BotaniaItems.grassSeeds, Blocks.SHORT_GRASS, 2500);
		normal(consumer, BotaniaItems.mycelSeeds, ConventionalItemTags.MUSHROOMS, 6500);
		normal(consumer, BotaniaItems.podzolSeeds, Blocks.DEAD_BUSH, 2500);
		normal(consumer, BotaniaBlocks.TINY_POTATO, Items.POTATO, 1337);

		alchemy(consumer, Blocks.CHORUS_FLOWER, Items.POPPED_CHORUS_FRUIT, 10000);
		alchemy(consumer, Blocks.COARSE_DIRT, Blocks.DIRT, 120);
		alchemy(consumer, Items.ENDER_PEARL, Items.GHAST_TEAR, 28000);
		alchemy(consumer, Blocks.ICE, Blocks.SNOW_BLOCK, 2250);
		alchemy(consumer, Items.LEATHER, Items.ROTTEN_FLESH, 600);
		alchemy(consumer, Items.NETHER_WART, Items.BLAZE_ROD, 4000);
		alchemy(consumer, Items.NAME_TAG, Items.WRITABLE_BOOK, 6000);
		alchemy(consumer, Items.POTATO, Items.POISONOUS_POTATO, 1200);
		alchemy(consumer, Blocks.RED_SAND, Blocks.TERRACOTTA, 50);
		alchemy(consumer, Blocks.SAND, ConventionalItemTags.NORMAL_COBBLESTONES, 50);
		alchemy(consumer, Blocks.SMALL_DRIPLEAF, Items.BIG_DRIPLEAF, 500);
		alchemy(consumer, Blocks.SOUL_SOIL, Blocks.SOUL_SAND, 120);

		cycle(consumer, 40, "log",
				Blocks.OAK_LOG, Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG, Blocks.MANGROVE_LOG,
				Blocks.BIRCH_LOG, Blocks.CHERRY_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG);
		cycle(consumer, 40, "stem", Blocks.CRIMSON_STEM, Blocks.WARPED_STEM);
		cycle(consumer, 12000, "sapling",
				Blocks.OAK_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.MANGROVE_PROPAGULE,
				Blocks.BIRCH_SAPLING, Blocks.CHERRY_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING);
		cycle(consumer, 120, "fungus", Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

		cycle(consumer, 320, "vine_and_lily_pad", Items.LILY_PAD, Items.VINE);
		cycle(consumer, 100, "froglight",
				Blocks.OCHRE_FROGLIGHT, Blocks.VERDANT_FROGLIGHT, Blocks.PEARLESCENT_FROGLIGHT);
		cycle(consumer, 200, "fish", Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
		cycle(consumer, 6000, "crop",
				Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT,
				Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);

		cycle(consumer, 200, "gunpowder_and_flint", Items.GUNPOWDER, Items.FLINT);
		cycle(consumer, 300, "glowstone_and_redstone", Items.GLOWSTONE_DUST, Items.REDSTONE);
		cycle(consumer, 1200, "cactus_and_slime", Items.SLIME_BALL, Items.CACTUS);

		alchemy(consumer, Blocks.ANDESITE, Blocks.STONE, 200);
		cycle(consumer, 200, "stone", Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.TUFF);
		cycle(consumer, 200, "nether_stone", Blocks.BASALT, Blocks.NETHERRACK, Blocks.BLACKSTONE);

		cycle(consumer, 500, "shrub", Blocks.FERN, Blocks.DEAD_BUSH, Blocks.SHORT_GRASS);

		// NB: No wither rose or sniffer flowers is intentional
		cycle(consumer, 400, "flower",
				Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET,
				Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY,
				Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.PINK_PETALS,
				Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY);

		cycle(consumer, 240, "berry", Items.APPLE, Items.SWEET_BERRIES, Items.GLOW_BERRIES);

		deconstructLossless(consumer, Items.CLAY_BALL, Blocks.CLAY);
		deconstructLossless(consumer, Items.GLOWSTONE_DUST, Blocks.GLOWSTONE);
		deconstructLossless(consumer, Items.MELON_SLICE, 9, Blocks.MELON);

		deconstructLossy(consumer, Items.AMETHYST_SHARD, Blocks.AMETHYST_BLOCK);
		deconstructLossy(consumer, Items.BAMBOO, 8, Blocks.BAMBOO_BLOCK);
		deconstructLossy(consumer, Items.BRICK, Blocks.BRICKS);
		deconstructLossy(consumer, Items.HONEYCOMB, Blocks.HONEYCOMB_BLOCK);
		deconstructLossy(consumer, Items.MAGMA_CREAM, Blocks.MAGMA_BLOCK);
		deconstructLossy(consumer, Items.NETHER_BRICK, Blocks.NETHER_BRICKS);
		deconstructLossy(consumer, Items.NETHER_BRICK, 2, Blocks.RED_NETHER_BRICKS);
		deconstructLossy(consumer, Items.POINTED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);
		deconstructLossy(consumer, Items.QUARTZ, Blocks.QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.darkQuartz, BotaniaBlocks.DARK_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.manaQuartz, BotaniaBlocks.MANA_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.blazeQuartz, BotaniaBlocks.BLAZE_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.lavenderQuartz, BotaniaBlocks.LAVENDER_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.redQuartz, BotaniaBlocks.RED_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.elfQuartz, BotaniaBlocks.ELVEN_QUARTZ_BLOCK);
		deconstructLossy(consumer, BotaniaItems.sunnyQuartz, BotaniaBlocks.SUNNY_QUART_BLOCK);
		deconstructLossy(consumer, Items.STRING, ItemTags.WOOL);

		alchemy(consumer, BotaniaBlocks.HYDROANGEAS_MOTIF, BotaniaBlocks.HYDROANGEAS, 2500);
		mini(consumer, BotaniaBlocks.AGRICARNATION_PETITE, BotaniaBlocks.AGRICARNATION);
		mini(consumer, BotaniaBlocks.BELLETHORNE_PETITE, BotaniaBlocks.BELLETHORNE);
		mini(consumer, BotaniaBlocks.BUBBELL_PETITE, BotaniaBlocks.BUBBELL);
		mini(consumer, BotaniaBlocks.CLAYCONIA_PETITE, BotaniaBlocks.CLAYCONIA);
		mini(consumer, BotaniaBlocks.HOPPERHOCK_PETITE, BotaniaBlocks.HOPPERHOCK);
		mini(consumer, BotaniaBlocks.JIYUULIA_PETITE, BotaniaBlocks.JIYUULIA);
		mini(consumer, BotaniaBlocks.MARIMORPHOSIS_PETITE, BotaniaBlocks.MARIMORPHOSIS);
		mini(consumer, BotaniaBlocks.RANNUNCARPUS_PETITE, BotaniaBlocks.RANNUNCARPUS);
		mini(consumer, BotaniaBlocks.SOLEGNOLIA_PETITE, BotaniaBlocks.SOLEGNOLIA);
		mini(consumer, BotaniaBlocks.TANGLEBERRIE_PETITE, BotaniaBlocks.TANGLEBERRIE);

		conjuration(consumer, Items.REDSTONE, 5000);
		conjuration(consumer, Items.GLOWSTONE_DUST, 5000);
		conjuration(consumer, Items.QUARTZ, 2500);
		conjuration(consumer, Items.COAL, 2100);
		conjuration(consumer, Items.SNOWBALL, 200);
		conjuration(consumer, Blocks.NETHERRACK, 200);
		conjuration(consumer, Blocks.SOUL_SAND, 1500);
		conjuration(consumer, Blocks.GRAVEL, 720);

		conjuration(consumer, Blocks.OAK_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.BIRCH_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.SPRUCE_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.JUNGLE_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.ACACIA_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.DARK_OAK_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.AZALEA_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.FLOWERING_AZALEA_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.MANGROVE_LEAVES, COST_LEAVES_DUPLICATION);
		conjuration(consumer, Blocks.CHERRY_LEAVES, COST_LEAVES_DUPLICATION);

		conjuration(consumer, Blocks.SHORT_GRASS, 800);
	}

	private static final StateIngredient ALCHEMY = StateIngredients.of(BotaniaBlocks.ALCHEMY_CATALYST);
	private static final StateIngredient CONJURATION = StateIngredients.of(BotaniaBlocks.CONJURATION_CATALYST);

	private static void normal(RecipeOutput consumer, ItemLike output, ItemLike input, int mana) {
		normal(consumer, output, Ingredient.of(input), mana);
	}

	private static void normal(RecipeOutput consumer, ItemLike output, TagKey<Item> input, int mana) {
		normal(consumer, output, Ingredient.of(input), mana);
	}

	private static void normal(RecipeOutput consumer, ItemLike output, Ingredient input, int mana) {
		normal(consumer, id(getItemName(output)), null, output, input, mana);
	}

	private static void normalWithGroup(RecipeOutput consumer, ItemLike output, Ingredient input, int mana,
			String recipeNameSuffix) {
		normal(consumer, id(getItemName(output) + recipeNameSuffix),
				BuiltInRegistries.ITEM.getKey(output.asItem()).toString(), output, input, mana);
	}

	private static void normal(RecipeOutput consumer, ResourceLocation id, @Nullable String group, ItemLike output, Ingredient input, int mana) {
		consumer.accept(id, new ManaInfusionRecipe(new ItemStack(output), input, mana, group, null), null);
	}

	private static void alchemy(RecipeOutput consumer, String id, ItemStack output, Ingredient input, int mana, @Nullable String group) {
		consumer.accept(id("alchemy/" + id), new ManaInfusionRecipe(output, input, mana, group, ALCHEMY), null);
	}

	private static void alchemy(RecipeOutput consumer, ItemLike output, ItemLike input, int mana) {
		alchemy(consumer, getItemName(output), new ItemStack(output), Ingredient.of(input), mana, null);
	}

	private static void alchemy(RecipeOutput consumer, ItemLike output, TagKey<Item> input, int mana) {
		alchemy(consumer, getItemName(output), new ItemStack(output), Ingredient.of(input), mana, null);
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
					new ItemStack(outItem), Ingredient.of(inItem), cost, "botania:%s_cycle".formatted(cycleName));
		}
	}

	protected void mini(RecipeOutput consumer, ItemLike mini, ItemLike full) {
		alchemy(consumer, getItemName(mini), new ItemStack(mini), Ingredient.of(full), 2500, "botania:flower_shrinking");
	}

	protected void deconstructLossless(RecipeOutput consumer, ItemLike items, ItemLike block) {
		deconstructLossless(consumer, items, 4, block);
	}

	protected void deconstructLossless(RecipeOutput consumer, ItemLike items, int count, ItemLike block) {
		deconstruct(consumer, getItemName(block), items, count, Ingredient.of(block), BASE_MANA_LOSSLESS_DECONSTRUCTION, "botania:lossless_deconstruction");
	}

	protected void deconstructLossy(RecipeOutput consumer, ItemLike output, ItemLike input) {
		deconstructLossy(consumer, getItemName(input), output, 3, Ingredient.of(input));
	}

	protected void deconstructLossy(RecipeOutput consumer, ItemLike output, TagKey<Item> input) {
		deconstructLossy(consumer, input.location().getPath(), output, 3, Ingredient.of(input));
	}

	protected void deconstructLossy(RecipeOutput consumer, ItemLike output, int count, ItemLike input) {
		deconstructLossy(consumer, getItemName(input), output, count, Ingredient.of(input));
	}

	protected void deconstructLossy(RecipeOutput consumer, String id, ItemLike output, int count, Ingredient input) {
		deconstruct(consumer, id, output, count, input, BASE_MANA_LOSSY_DECONSTRUCTION, "botania:lossy_deconstruction");
	}

	private static void deconstruct(RecipeOutput consumer, String id, ItemLike output, int count, Ingredient input,
			int baseMana, @Nullable String group) {
		alchemy(consumer, id + "_deconstruct", new ItemStack(output, count), input,
				(int) Math.round(Math.sqrt(count) * baseMana), group);
	}

	protected static ResourceLocation id(String s) {
		return botaniaRL("mana_infusion/" + s);
	}

}
