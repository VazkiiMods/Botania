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
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.ElvenTradeRecipe;
import vazkii.botania.common.crafting.LexiconElvenTradeRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.data.util.BotaniaRecipeHelper;

import java.util.concurrent.CompletableFuture;

public class ElvenTradeProvider extends FabricRecipeProvider {
	public ElvenTradeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		oneToOneTrade(consumer, BotaniaBlocks.LIVINGWOOD_LOG, BotaniaBlocks.DREAMWOOD_LOG);
		oneToOneTrade(consumer, BotaniaBlocks.LIVINGWOOD, BotaniaBlocks.DREAMWOOD);

		twoToOneTrade(consumer, ConventionalBotaniaTags.Items.MANASTEEL_INGOTS, BotaniaItems.ELEMENTIUM_INGOT);
		twoToOneTrade(consumer, ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS, BotaniaBlocks.ELEMENTIUM_BLOCK);
		// TODO: a bit of a debugging/convenience recipe that ensures recipes with different input item types work:
		consumer.accept(id(BotaniaItems.ELEMENTIUM_INGOT, "_from_mixed_input"),
				new ElvenTradeRecipe(
						// ingot + block = 10 ingots, so resolve to a stack of 5 elementium ingots
						new ItemStack[] { new ItemStack(BotaniaItems.ELEMENTIUM_INGOT, 5) },
						Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS),
						Ingredient.of(ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS)
				), null);

		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_PEARL_GEMS, BotaniaItems.PIXIE_DUST);
		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS, BotaniaItems.DRAGONSTONE);
		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_DIAMOND_STORAGE_BLOCKS, BotaniaBlocks.DRAGONSTONE_BLOCK);

		oneToOneTrade(consumer, Items.QUARTZ, BotaniaItems.ELVEN_QUARTZ);
		oneToOneTrade(consumer, BotaniaBlocks.MANAGLASS, BotaniaBlocks.ALFGLASS);

		returnRecipe(consumer, Items.IRON_INGOT);
		returnRecipe(consumer, Blocks.IRON_BLOCK);
		returnRecipe(consumer, Items.ENDER_PEARL);
		returnRecipe(consumer, Items.DIAMOND);
		returnRecipe(consumer, Blocks.DIAMOND_BLOCK);

		consumer.accept(id(BotaniaItems.LEXICA_BOTANIA, "_upgrade"), LexiconElvenTradeRecipe.INSTANCE, null);
	}

	private void oneToOneTrade(RecipeOutput recipeOutput, ItemLike input, ItemLike output) {
		recipeOutput.accept(id(output),
				new ElvenTradeRecipe(singleOutput(output), Ingredient.of(input)), null);
	}

	private void oneToOneTrade(RecipeOutput recipeOutput, TagKey<Item> input, ItemLike output) {
		recipeOutput.accept(id(output),
				new ElvenTradeRecipe(singleOutput(output), Ingredient.of(input)), null);
	}

	private void twoToOneTrade(RecipeOutput recipeOutput, TagKey<Item> input, ItemLike output) {
		recipeOutput.accept(id(output),
				new ElvenTradeRecipe(singleOutput(output), Ingredient.of(input), Ingredient.of(input)), null);
	}

	private void returnRecipe(RecipeOutput recipeOutput, ItemLike item) {
		recipeOutput.accept(id(item, "_return"),
				new ElvenTradeRecipe(singleOutput(item), Ingredient.of(item)), null);
	}

	private static ItemStack[] singleOutput(ItemLike output) {
		return new ItemStack[] { new ItemStack(output) };
	}

	private static ResourceLocation id(ItemLike item) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, item);
	}

	private static ResourceLocation id(ItemLike item, String suffix) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, item, suffix);
	}

	@Override
	public String getName() {
		return "Botania elven trade recipes";
	}
}
