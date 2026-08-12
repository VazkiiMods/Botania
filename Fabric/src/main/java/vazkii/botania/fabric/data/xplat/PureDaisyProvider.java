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

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.PureDaisyRecipe;
import vazkii.botania.common.crafting.StateIngredients;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;
import vazkii.botania.data.util.BotaniaRecipeHelper;

import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class PureDaisyProvider extends BotaniaRecipeProvider {
	public PureDaisyProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {

		normal(consumer, StateIngredients.of(Blocks.STONE), BotaniaBlocks.LIVINGROCK.defaultBlockState());
		stateCopying(consumer,
				StateIngredients.ofExcept(
						StateIngredients.of(BlockTags.LOGS),
						StateIngredients.anyOf(StateIngredients.of(BotaniaBlocks.LIVINGWOOD_LOG))),
				BotaniaBlocks.LIVINGWOOD_LOG
		);

		normal(consumer, StateIngredients.of(Blocks.NETHERRACK), Blocks.COBBLESTONE.defaultBlockState());
		withFunction(consumer, StateIngredients.of(Blocks.END_STONE),
				Blocks.COBBLED_DEEPSLATE.defaultBlockState(), new CacheableFunction(botaniaRL("ender_essence_release")));
		normal(consumer, StateIngredients.of(Blocks.SOUL_SAND), Blocks.SAND.defaultBlockState());
		normal(consumer, StateIngredients.of(Blocks.ICE), Blocks.PACKED_ICE.defaultBlockState());
		normal(consumer, StateIngredients.of(Blocks.PACKED_ICE), Blocks.BLUE_ICE.defaultBlockState());
		normal(consumer, StateIngredients.of(BotaniaBlocks.BLAZE_MESH), Blocks.OBSIDIAN.defaultBlockState());
		normal(consumer, StateIngredients.of(Blocks.WATER), Blocks.SNOW_BLOCK.defaultBlockState());
		normal(consumer, StateIngredients.of(Blocks.DRIPSTONE_BLOCK), Blocks.CALCITE.defaultBlockState());
	}

	@Override
	public String getName() {
		return "Botania Pure Daisy recipes";
	}

	private void normal(RecipeOutput consumer, StateIngredient input, BlockState output) {
		withFunction(consumer, input, output, null);
	}

	private void withFunction(RecipeOutput consumer, StateIngredient input, BlockState output, @Nullable CacheableFunction successFunction) {
		consumer.accept(id(output.getBlock()), new PureDaisyRecipe(input, StateIngredients.of(output),
				PureDaisyRecipe.DEFAULT_TIME, false, null, successFunction), null);
	}

	private void stateCopying(RecipeOutput consumer, StateIngredient input, Block output) {
		consumer.accept(id(output), new PureDaisyRecipe(input, StateIngredients.of(output),
				PureDaisyRecipe.DEFAULT_TIME, true, null, null), null);
	}

	private static ResourceLocation id(Block block) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.PURE_DAISY_TYPE, block);
	}
}
