/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.lib.ModTags;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidProvider extends BotaniaRecipeProvider {

	public OrechidProvider(DataGenerator generator) {
		super(generator);
	}

	// TODO: We had an enormous amount of ores defined for mod compat.
	//       The old data needs to be completely revised.
	@Override
	protected void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(stone(Blocks.COAL_ORE, 67415));
		consumer.accept(stone(Blocks.IRON_ORE, 29371));
		consumer.accept(stone(Blocks.REDSTONE_ORE, 7654));
		consumer.accept(stone(Blocks.COPPER_ORE, 7000));
		consumer.accept(stone(Blocks.GOLD_ORE, 2647));
		consumer.accept(stone(Blocks.EMERALD_ORE, 1239));
		consumer.accept(stone(Blocks.LAPIS_ORE, 1079));
		consumer.accept(stone(Blocks.DIAMOND_ORE, 883));

		consumer.accept(deepslate(Blocks.DEEPSLATE_COAL_ORE, 75));
		consumer.accept(deepslate(Blocks.DEEPSLATE_IRON_ORE, 250));
		consumer.accept(deepslate(Blocks.DEEPSLATE_REDSTONE_ORE, 150));
		consumer.accept(deepslate(Blocks.DEEPSLATE_COPPER_ORE, 75));
		consumer.accept(deepslate(Blocks.DEEPSLATE_GOLD_ORE, 125));
		consumer.accept(deepslate(Blocks.DEEPSLATE_EMERALD_ORE, 50));
		consumer.accept(deepslate(Blocks.DEEPSLATE_LAPIS_ORE, 175));
		consumer.accept(deepslate(Blocks.DEEPSLATE_DIAMOND_ORE, 100));

		consumer.accept(netherrack(Blocks.NETHER_QUARTZ_ORE, 19600));
		consumer.accept(netherrack(Blocks.NETHER_GOLD_ORE, 3635));
		consumer.accept(netherrack(Blocks.ANCIENT_DEBRIS, 148));

		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneForest, ModTags.Biomes.MARIMORPHOSIS_FOREST_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStonePlains, ModTags.Biomes.MARIMORPHOSIS_PLAINS_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneMountain, ModTags.Biomes.MARIMORPHOSIS_MOUNTAIN_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneFungal, ModTags.Biomes.MARIMORPHOSIS_FUNGAL_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneSwamp, ModTags.Biomes.MARIMORPHOSIS_SWAMP_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneDesert, ModTags.Biomes.MARIMORPHOSIS_DESERT_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneTaiga, ModTags.Biomes.MARIMORPHOSIS_TAIGA_BONUS));
		consumer.accept(biomeStone(ModFluffBlocks.biomeStoneMesa, ModTags.Biomes.MARIMORPHOSIS_MESA_BONUS));
	}

	protected ResourceLocation orechidId(Block b) {
		return prefix("orechid/" + Registry.BLOCK.getKey(b).getPath());
	}

	protected ResourceLocation ignemId(Block b) {
		return prefix("orechid_ignem/" + Registry.BLOCK.getKey(b).getPath());
	}

	protected ResourceLocation marimorphosisId(Block b) {
		return prefix("marimorphosis/" + Registry.BLOCK.getKey(b).getPath());
	}

	protected Result stone(Block output, int weight) {
		return new Result(ModRecipeTypes.ORECHID_SERIALIZER, orechidId(output), Blocks.STONE, forBlock(output), weight);
	}

	protected Result deepslate(Block output, int weight) {
		return new Result(ModRecipeTypes.ORECHID_SERIALIZER, orechidId(output), Blocks.DEEPSLATE, forBlock(output), weight);
	}

	protected Result netherrack(Block output, int weight) {
		return new Result(ModRecipeTypes.ORECHID_IGNEM_SERIALIZER, ignemId(output), Blocks.NETHERRACK, forBlock(output), weight);
	}

	protected Result biomeStone(Block output, TagKey<Biome> biome) {
		return new BiomeResult(ModRecipeTypes.MARIMORPHOSIS_SERIALIZER, marimorphosisId(output), Blocks.STONE, forBlock(output), 1, 11, biome);
	}

	protected static StateIngredient forBlock(Block block) {
		return StateIngredientHelper.of(block);
	}

	@Override
	public String getName() {
		return "Botania Orechid and Marimorphosis weight data";
	}

	protected static class Result implements net.minecraft.data.recipes.FinishedRecipe {
		private final RecipeSerializer<? extends IOrechidRecipe> type;
		private final ResourceLocation id;
		private final Block input;
		private final StateIngredient output;
		private final int weight;

		public Result(RecipeSerializer<? extends IOrechidRecipe> type, ResourceLocation id, Block input, StateIngredient output, int weight) {
			this.type = type;
			this.id = id;
			this.input = input;
			this.output = output;
			this.weight = weight;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("input", Registry.BLOCK.getKey(input).toString());
			json.add("output", output.serialize());
			json.addProperty("weight", weight);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return type;
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}

	protected static class BiomeResult extends Result {
		private final int bonusWeight;
		private final TagKey<Biome> biome;

		public BiomeResult(RecipeSerializer<? extends IOrechidRecipe> type, ResourceLocation id, Block input,
				StateIngredient output, int weight, int bonusWeight, TagKey<Biome> biome) {
			super(type, id, input, output, weight);
			this.bonusWeight = bonusWeight;
			this.biome = biome;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			super.serializeRecipeData(json);
			json.addProperty("biome_bonus_tag", biome.location().toString());
			json.addProperty("biome_bonus", bonusWeight);
		}
	}

}
