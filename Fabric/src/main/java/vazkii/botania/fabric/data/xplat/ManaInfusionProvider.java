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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeItem;
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
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaInfusionProvider extends BotaniaRecipeProvider {
	public ManaInfusionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public String getName() {
		return "Botania mana pool recipes";
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		normal(consumer, id("manasteel"), new ItemStack(BotaniaItems.manaSteel), ingr(ConventionalItemTags.IRON_INGOTS), 3000);
		normal(consumer, id("manasteel_block"), new ItemStack(BotaniaBlocks.manasteelBlock), ingr(ConventionalItemTags.STORAGE_BLOCKS_IRON), 27000);

		normal(consumer, id("mana_pearl"), new ItemStack(BotaniaItems.manaPearl), ingr(Items.ENDER_PEARL), 6000);

		normal(consumer, id("mana_diamond"), new ItemStack(BotaniaItems.manaDiamond), Ingredient.of(ConventionalItemTags.DIAMOND_GEMS), 10000);
		normal(consumer, id("mana_diamond_block"), new ItemStack(BotaniaBlocks.manaDiamondBlock), ingr(ConventionalItemTags.STORAGE_BLOCKS_DIAMOND), 90000);

		// TODO: make this a tag
		// I think the tag exists, so EMI picks it up, but it might as well be in the recipe
		Ingredient dust = Ingredient.of(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST, Items.SUGAR);
		normal(consumer, id("mana_powder_dust"), new ItemStack(BotaniaItems.manaPowder), dust, 500);
		// TODO: this one too
		Ingredient dyeIngredient = Ingredient.of(ColorHelper.supportedColors().map(DyeItem::byColor).toArray(Item[]::new));
		normal(consumer, id("mana_powder_dye"), new ItemStack(BotaniaItems.manaPowder), dyeIngredient, 400);

		normal(consumer, id("piston_relay"), new ItemStack(BotaniaBlocks.pistonRelay), ingr(Blocks.PISTON), 15000);
		normal(consumer, id("mana_cookie"), new ItemStack(BotaniaItems.manaCookie), ingr(Items.COOKIE), 20000);
		normal(consumer, id("grass_seeds"), new ItemStack(BotaniaItems.grassSeeds), ingr(Blocks.SHORT_GRASS), 2500);
		normal(consumer, id("podzol_seeds"), new ItemStack(BotaniaItems.podzolSeeds), ingr(Blocks.DEAD_BUSH), 2500);

		normal(consumer, id("mycel_seeds"), new ItemStack(BotaniaItems.mycelSeeds), ingr(ConventionalItemTags.MUSHROOMS), 6500);

		normal(consumer, id("mana_quartz"), new ItemStack(BotaniaItems.manaQuartz), ingr(Items.QUARTZ), 250);
		normal(consumer, id("tiny_potato"), new ItemStack(BotaniaBlocks.tinyPotato), ingr(Items.POTATO), 1337);

		normal(consumer, id("mana_glass"), new ItemStack(BotaniaBlocks.manaGlass), ingr(ConventionalItemTags.GLASS_BLOCKS_COLORLESS), 150);
		normal(consumer, id("mana_string"), new ItemStack(BotaniaItems.manaString), ingr(Items.STRING), 1250);

		normal(consumer, id("mana_bottle"), new ItemStack(BotaniaItems.manaBottle), ingr(Items.GLASS_BOTTLE), 5000);

		alchemy(consumer, id("rotten_flesh_to_leather"), new ItemStack(Items.LEATHER), ingr(Items.ROTTEN_FLESH), 600);

		cycle(consumer, 40, "botania:log_cycle",
				Blocks.OAK_LOG, Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG, Blocks.MANGROVE_LOG,
				Blocks.BIRCH_LOG, Blocks.CHERRY_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG);
		cycle(consumer, 6000, "botania:sapling_cycle",
				Blocks.OAK_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.MANGROVE_PROPAGULE,
				Blocks.BIRCH_SAPLING, Blocks.CHERRY_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING);

		alchemy(consumer, id("ice"), new ItemStack(Blocks.ICE), ingr(Blocks.SNOW_BLOCK), 2250);

		cycle(consumer, 320, "botania:vine_and_lily_pad_cycle", Items.LILY_PAD, Items.VINE);

		cycle(consumer, 6000, "botania:crop_cycle", Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.POTATO,
				Items.CARROT, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);

		alchemy(consumer, id("potato_unpoison"), new ItemStack(Items.POTATO), ingr(Items.POISONOUS_POTATO), 1200);
		alchemy(consumer, id("blaze_rod_to_nether_wart"), new ItemStack(Items.NETHER_WART), ingr(Items.BLAZE_ROD), 4000);

		cycle(consumer, 200, "botania:gunpowder_and_flint_cycle", Items.GUNPOWDER, Items.FLINT);

		alchemy(consumer, id("book_to_name_tag"), new ItemStack(Items.NAME_TAG), ingr(Items.WRITABLE_BOOK), 6000);

		cycle(consumer, 1200, "botania:cactus_and_slime_cycle", Items.SLIME_BALL, Blocks.CACTUS);

		alchemy(consumer, id("ender_pearl_from_ghast_tear"), new ItemStack(Items.ENDER_PEARL), ingr(Items.GHAST_TEAR), 28000);
		alchemy(consumer, id("nether_wart_from_wart_block"), new ItemStack(Blocks.NETHER_WART), ingr(Blocks.NETHER_WART_BLOCK), 500);

		cycle(consumer, 300, "botania:glowstone_and_redstone_cycle", Items.GLOWSTONE_DUST, Items.REDSTONE);

		alchemy(consumer, id("cobble_to_sand"), new ItemStack(Blocks.SAND), ingr(ConventionalItemTags.COBBLESTONES), 50);
		alchemy(consumer, id("terracotta_to_red_sand"), new ItemStack(Blocks.RED_SAND), ingr(Blocks.TERRACOTTA), 50);

		alchemy(consumer, id("coarse_dirt"), new ItemStack(Blocks.COARSE_DIRT), ingr(Blocks.DIRT), 120);
		alchemy(consumer, id("soul_soil"), new ItemStack(Blocks.SOUL_SOIL), ingr(Blocks.SOUL_SAND), 120);

		alchemy(consumer, id("stone_to_andesite"), new ItemStack(Blocks.ANDESITE), ingr(Blocks.STONE), 200);
		cycle(consumer, 200, "botania:stone_cycle", Blocks.DIORITE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.CALCITE);
		cycle(consumer, 200, "botania:nether_stone_cycle", Blocks.BASALT, Blocks.NETHERRACK, Blocks.BLACKSTONE);

		alchemy(consumer, id("dripleaf_shrinking"), new ItemStack(Blocks.SMALL_DRIPLEAF), ingr(Blocks.BIG_DRIPLEAF), 500);
		alchemy(consumer, id("chorus_fruit_to_flower"), new ItemStack(Blocks.CHORUS_FLOWER), ingr(Items.POPPED_CHORUS_FRUIT), 10000);

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

		alchemy(consumer, id("hydroangeas_motif"), new ItemStack(BotaniaBlocks.motifHydroangeas), ingr(BotaniaBlocks.hydroangeas), 2500);

		deconstruct2x2(consumer, Items.CLAY_BALL, Blocks.CLAY);
		deconstruct2x2(consumer, Items.SNOWBALL, Blocks.SNOW_BLOCK);

		deconstruct(consumer, Items.SNOWBALL, 1, Blocks.SNOW, 20, null);
		deconstruct(consumer, id("wool_deconstruct"), new ItemStack(Items.STRING, 3), ingr(ItemTags.WOOL), 100, "botania:wool_deconstruction");
		deconstruct(consumer, Items.NETHER_BRICK, 2, Blocks.RED_NETHER_BRICKS, 25, null);

		// fully-grown cluster yields 4 shards with unenchanted pickaxe
		deconstruct(consumer, Items.AMETHYST_SHARD, 1, Blocks.SMALL_AMETHYST_BUD, 100, "botania:amethyst_cluster_deconstruction");
		deconstruct(consumer, Items.AMETHYST_SHARD, 2, Blocks.MEDIUM_AMETHYST_BUD, 100, "botania:amethyst_cluster_deconstruction");
		deconstruct(consumer, Items.AMETHYST_SHARD, 3, Blocks.LARGE_AMETHYST_BUD, 100, "botania:amethyst_cluster_deconstruction");
		deconstruct(consumer, Items.AMETHYST_SHARD, 4, Blocks.AMETHYST_CLUSTER, 100, "botania:amethyst_cluster_deconstruction");

		deconstruct2x2Lossy(consumer, Items.QUARTZ, Blocks.QUARTZ_BLOCK, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.darkQuartz, BotaniaBlocks.darkQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.manaQuartz, BotaniaBlocks.manaQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.blazeQuartz, BotaniaBlocks.blazeQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.lavenderQuartz, BotaniaBlocks.lavenderQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.redQuartz, BotaniaBlocks.redQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.elfQuartz, BotaniaBlocks.elfQuartz, "botania:quartz_deconstruction");
		deconstruct2x2Lossy(consumer, BotaniaItems.sunnyQuartz, BotaniaBlocks.sunnyQuartz, "botania:quartz_deconstruction");

		deconstruct2x2Lossy(consumer, Items.GLOWSTONE_DUST, Blocks.GLOWSTONE);
		deconstruct2x2Lossy(consumer, Items.BRICK, Blocks.BRICKS);
		deconstruct2x2Lossy(consumer, Items.NETHER_BRICK, Blocks.NETHER_BRICKS);
		deconstruct2x2Lossy(consumer, Items.POINTED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);
		deconstruct2x2Lossy(consumer, Items.HONEYCOMB, Blocks.HONEYCOMB_BLOCK);
		deconstruct2x2Lossy(consumer, Items.MAGMA_CREAM, Blocks.MAGMA_BLOCK);
		deconstruct2x2Lossy(consumer, Items.AMETHYST_SHARD, Blocks.AMETHYST_BLOCK);

		deconstruct3x3Lossless(consumer, Items.BAMBOO, Blocks.BAMBOO_BLOCK);
		deconstruct3x3Lossless(consumer, Items.MELON_SLICE, Blocks.MELON);

	}

	private static final StateIngredient ALCHEMY = StateIngredients.of(BotaniaBlocks.alchemyCatalyst);
	private static final StateIngredient CONJURATION = StateIngredients.of(BotaniaBlocks.conjurationCatalyst);

	protected static void normal(RecipeOutput consumer, ResourceLocation id, ItemStack output, Ingredient input, int mana) {
		consumer.accept(id, new ManaInfusionRecipe(output, input, mana, null, null), null);
	}

	protected static void alchemy(RecipeOutput consumer, ResourceLocation id, ItemStack output, Ingredient input, int mana, @Nullable String group) {
		consumer.accept(id, new ManaInfusionRecipe(output, input, mana, group, ALCHEMY), null);
	}

	protected static void alchemy(RecipeOutput consumer, ResourceLocation id, ItemStack output, Ingredient input, int mana) {
		alchemy(consumer, id, output, input, mana, null);
	}

	protected static void deconstruct(RecipeOutput consumer, ResourceLocation id, ItemStack output, Ingredient input, int mana, @Nullable String group) {
		consumer.accept(id, new ManaInfusionRecipe(output, input, mana, group, CONJURATION), null);
	}

	protected void cycle(RecipeOutput consumer, int cost, String group, ItemLike... items) {
		for (int i = 0; i < items.length; i++) {
			Ingredient in = ingr(items[i]);
			ItemStack out = new ItemStack(i == items.length - 1 ? items[0] : items[i + 1]);
			String id = String.format("%s_to_%s", BuiltInRegistries.ITEM.getKey(items[i].asItem()).getPath(), BuiltInRegistries.ITEM.getKey(out.getItem()).getPath());
			alchemy(consumer, id(id), out, in, cost, group);
		}
	}

	protected void mini(RecipeOutput consumer, ItemLike mini, ItemLike full) {
		alchemy(consumer, id(BuiltInRegistries.ITEM.getKey(mini.asItem()).getPath()), new ItemStack(mini), ingr(full), 2500, "botania:flower_shrinking");
	}

	protected void deconstruct(RecipeOutput consumer, ItemLike items, int count, ItemLike block, int mana, @Nullable String group) {
		deconstruct(consumer, id(BuiltInRegistries.ITEM.getKey(block.asItem()).getPath() + "_deconstruct"),
				new ItemStack(items, count), ingr(block), mana, group);
	}

	protected void deconstruct2x2(RecipeOutput consumer, ItemLike items, ItemLike block) {
		deconstruct(consumer, items, 4, block, 30, "botania:lossless_2x2_deconstruction");
	}

	protected void deconstruct2x2Lossy(RecipeOutput consumer, ItemLike items, ItemLike block) {
		deconstruct2x2Lossy(consumer, items, block, "botania:lossy_2x2_deconstruction");
	}

	protected void deconstruct2x2Lossy(RecipeOutput consumer, ItemLike items, ItemLike block, @Nullable String group) {
		deconstruct(consumer, items, 3, block, 25, group);
	}

	protected void deconstruct3x3Lossless(RecipeOutput consumer, ItemLike items, ItemLike block) {
		deconstruct(consumer, items, 9, block, 120, "botania:lossless_3x3_deconstruction");
	}

	protected ResourceLocation id(String s) {
		return botaniaRL("mana_infusion/" + s);
	}

	protected static Ingredient ingr(ItemLike i) {
		return Ingredient.of(i);
	}

	protected static Ingredient ingr(TagKey<Item> i) {
		return Ingredient.of(i);
	}
}
