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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.flower.functional.MarimorphosisBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.OrechidBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.OrechidIgnemBlockEntity;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.fabric.data.FabricDatagenInitializer;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class OrechidProvider extends FabricRecipeProvider {

	public OrechidProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	// TODO: We had an enormous amount of ores defined for mod compat.
	//       The old data needs to be completely revised.
	@Override
	public void buildRecipes(RecipeOutput consumer) {
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.COAL_ORE, 5129);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.IRON_ORE, 1568);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.REDSTONE_ORE, 71);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.COPPER_ORE, 2724,
				BotaniaTags.Biomes.ORECHID_STONE_COPPER_BONUS, 2500);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.GOLD_ORE, 106,
				BotaniaTags.Biomes.ORECHID_STONE_GOLD_BONUS, 1790);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.EMERALD_ORE, 1,
				BotaniaTags.Biomes.ORECHID_STONE_EMERALD_BONUS, 111);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.LAPIS_ORE, 281);
		stone(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), Blocks.DIAMOND_ORE, 10);

		deepslate(consumer, Blocks.DEEPSLATE_COAL_ORE, 57);
		deepslate(consumer, Blocks.DEEPSLATE_IRON_ORE, 2513);
		deepslate(consumer, Blocks.DEEPSLATE_REDSTONE_ORE, 2510);
		deepslate(consumer, Blocks.DEEPSLATE_COPPER_ORE, 471, BotaniaTags.Biomes.ORECHID_DEEPSLATE_COPPER_BONUS, 1350);
		deepslate(consumer, Blocks.DEEPSLATE_GOLD_ORE, 1605);
		deepslate(consumer, Blocks.DEEPSLATE_EMERALD_ORE, 1, BotaniaTags.Biomes.ORECHID_DEEPSLATE_EMERALD_BONUS, 10);
		deepslate(consumer, Blocks.DEEPSLATE_LAPIS_ORE, 1033);
		deepslate(consumer, Blocks.DEEPSLATE_DIAMOND_ORE, 1801);

		netherrack(consumer, Blocks.NETHER_QUARTZ_ORE, 19600);
		netherrack(consumer, Blocks.NETHER_GOLD_ORE, 3635);
		netherrack(consumer, Blocks.ANCIENT_DEBRIS, 148);

		biomeStone(consumer, BotaniaBlocks.biomeStoneForest, BotaniaTags.Biomes.MARIMORPHOSIS_FOREST_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStonePlains, BotaniaTags.Biomes.MARIMORPHOSIS_PLAINS_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneMountain, BotaniaTags.Biomes.MARIMORPHOSIS_MOUNTAIN_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneFungal, BotaniaTags.Biomes.MARIMORPHOSIS_FUNGAL_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneSwamp, BotaniaTags.Biomes.MARIMORPHOSIS_SWAMP_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneDesert, BotaniaTags.Biomes.MARIMORPHOSIS_DESERT_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneTaiga, BotaniaTags.Biomes.MARIMORPHOSIS_TAIGA_BONUS);
		biomeStone(consumer, BotaniaBlocks.biomeStoneMesa, BotaniaTags.Biomes.MARIMORPHOSIS_MESA_BONUS);
	}

	protected ResourceLocation orechidId(Block b) {
		return botaniaRL("orechid/" + BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	protected ResourceLocation ignemId(Block b) {
		return botaniaRL("orechid_ignem/" + BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	protected ResourceLocation marimorphosisId(Block b) {
		return botaniaRL("marimorphosis/" + BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	protected void stone(RecipeOutput consumer, Block output, int weight) {
		stone(consumer, output, weight, null, 0);
	}

	private void stone(RecipeOutput consumer, Block output, int weight, @Nullable TagKey<Biome> bonusBiomes, int bonusWeight) {
		consumer.accept(orechidId(output), new OrechidRecipe(forBlock(Blocks.STONE), forBlock(output),
				OrechidBlockEntity.DEFAULT_DELAY, OrechidBlockEntity.DEFAULT_COST, weight, bonusWeight, bonusBiomes), null);
	}

	protected void deepslate(RecipeOutput consumer, Block output, int weight) {
		deepslate(consumer, output, weight, null, 0);
	}

	protected void deepslate(RecipeOutput consumer, Block output, int weight, @Nullable TagKey<Biome> bonusBiome, int bonusWeight) {
		consumer.accept(orechidId(output), new OrechidRecipe(forBlock(Blocks.DEEPSLATE), forBlock(output),
				OrechidBlockEntity.DEFAULT_DELAY, OrechidBlockEntity.DEFAULT_COST, weight, bonusWeight, bonusBiome), null);
	}

	protected void netherrack(RecipeOutput consumer, Block output, int weight) {
		consumer.accept(ignemId(output), new OrechidIgnemRecipe(forBlock(Blocks.NETHERRACK), forBlock(output),
				OrechidBlockEntity.DEFAULT_DELAY, OrechidIgnemBlockEntity.DEFAULT_COST, weight), null);
	}

	protected void biomeStone(RecipeOutput consumer, Block output, TagKey<Biome> biome) {
		consumer.accept(marimorphosisId(output), new MarimorphosisRecipe(
				forTag(BotaniaTags.Blocks.MARIMORPHOSIS_CONVERTABLE), forBlock(output),
				MarimorphosisBlockEntity.COST, 1, 11, biome), null);
	}

	protected static StateIngredient forBlock(Block block) {
		return StateIngredients.of(block);
	}

	protected static StateIngredient forTag(TagKey<Block> tag) {
		return StateIngredients.of(tag);
	}

	@Override
	public String getName() {
		return "Botania Orechid and Marimorphosis recipes";
	}

}
