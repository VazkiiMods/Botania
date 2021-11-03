/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PureDaisyProvider extends BotaniaRecipeProvider {
	public PureDaisyProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {

		consumer.accept(new FinishedRecipe(id("livingrock"), StateIngredientHelper.of(Blocks.STONE), ModBlocks.livingrock.defaultBlockState()));
		consumer.accept(new StateCopyingRecipe(id("livingwood"),
				StateIngredientHelper.tagExcluding(BlockTags.LOGS,
						StateIngredientHelper.of(ModTags.Blocks.LIVINGWOOD_LOGS),
						StateIngredientHelper.of(ModTags.Blocks.DREAMWOOD_LOGS)),
				ModBlocks.livingwoodLog));

		consumer.accept(new FinishedRecipe(id("cobblestone"), StateIngredientHelper.of(Blocks.NETHERRACK), Blocks.COBBLESTONE.defaultBlockState()));
		consumer.accept(new FinishedRecipe(id("sand"), StateIngredientHelper.of(Blocks.SOUL_SAND), Blocks.SAND.defaultBlockState()));
		consumer.accept(new FinishedRecipe(id("packed_ice"), StateIngredientHelper.of(Blocks.ICE), Blocks.PACKED_ICE.defaultBlockState()));
		consumer.accept(new FinishedRecipe(id("blue_ice"), StateIngredientHelper.of(Blocks.PACKED_ICE), Blocks.BLUE_ICE.defaultBlockState()));
		consumer.accept(new FinishedRecipe(id("obsidian"), StateIngredientHelper.of(ModBlocks.blazeBlock), Blocks.OBSIDIAN.defaultBlockState()));
		consumer.accept(new FinishedRecipe(id("snow_block"), StateIngredientHelper.of(Blocks.WATER), Blocks.SNOW_BLOCK.defaultBlockState()));
	}

	@Override
	public String getName() {
		return "Botania Pure Daisy recipes";
	}

	private static ResourceLocation id(String path) {
		return prefix("pure_daisy/" + path);
	}

	protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		public static final int DEFAULT_TIME = 150;

		protected final ResourceLocation id;
		protected final StateIngredient input;
		protected final BlockState outputState;
		protected final int time;

		public FinishedRecipe(ResourceLocation id, StateIngredient input, BlockState state) {
			this(id, input, state, DEFAULT_TIME);
		}

		public FinishedRecipe(ResourceLocation id, StateIngredient input, BlockState state, int time) {
			Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
			this.id = id;
			this.input = input;
			this.outputState = state;
			this.time = time;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", input.serialize());
			json.add("output", StateIngredientHelper.serializeBlockState(outputState));
			if (time != DEFAULT_TIME) {
				json.addProperty("time", time);
			}
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipeTypes.PURE_DAISY_SERIALIZER;
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

	protected static class StateCopyingRecipe extends FinishedRecipe {
		public StateCopyingRecipe(ResourceLocation id, StateIngredient input, Block block) {
			super(id, input, block.defaultBlockState());
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", input.serialize());
			json.addProperty("output", Registry.BLOCK.getKey(outputState.getBlock()).toString());
			if (time != DEFAULT_TIME) {
				json.addProperty("time", time);
			}
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipeTypes.COPYING_PURE_DAISY_SERIALIZER;
		}
	}
}
