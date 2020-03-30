/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Blocks;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeProvider extends RecipeProvider {
	public ElvenTradeProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		Ingredient livingwood = Ingredient.fromTag(ModTags.Items.LIVINGWOOD);
		consumer.accept(new FinishedRecipe(id("dreamwood"), new ItemStack(ModBlocks.dreamwood), livingwood));

		Ingredient manaDiamond = Ingredient.fromTag(ModTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaSteel = Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL);
		consumer.accept(new FinishedRecipe(id("elementium"), new ItemStack(ModItems.elementium), manaSteel, manaSteel));
		consumer.accept(new FinishedRecipe(id("elementium_block"), new ItemStack(ModBlocks.elementiumBlock), Ingredient.fromItems(ModBlocks.manasteelBlock), Ingredient.fromItems(ModBlocks.manasteelBlock)));

		consumer.accept(new FinishedRecipe(id("pixie_dust"), new ItemStack(ModItems.pixieDust), Ingredient.fromItems(ModItems.manaPearl)));
		consumer.accept(new FinishedRecipe(id("dragonstone"), new ItemStack(ModItems.dragonstone), manaDiamond));
		consumer.accept(new FinishedRecipe(id("dragonstone_block"), new ItemStack(ModBlocks.dragonstoneBlock), Ingredient.fromItems(ModBlocks.manaDiamondBlock)));

		consumer.accept(new FinishedRecipe(id("elf_quartz"), new ItemStack(ModItems.elfQuartz), Ingredient.fromItems(Items.QUARTZ)));
		consumer.accept(new FinishedRecipe(id("elf_glass"), new ItemStack(ModBlocks.elfGlass), Ingredient.fromItems(ModBlocks.manaGlass)));

		consumer.accept(new FinishedRecipe(id("iron_return"), new ItemStack(Items.IRON_INGOT), Ingredient.fromItems(Items.IRON_INGOT)));
		consumer.accept(new FinishedRecipe(id("iron_block_return"), new ItemStack(Blocks.IRON_BLOCK), Ingredient.fromItems(Blocks.IRON_BLOCK)));
		consumer.accept(new FinishedRecipe(id("ender_pearl_return"), new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.ENDER_PEARL)));
		consumer.accept(new FinishedRecipe(id("diamond_return"), new ItemStack(Items.DIAMOND), Ingredient.fromItems(Items.DIAMOND)));
		consumer.accept(new FinishedRecipe(id("diamond_block_return"), new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.fromItems(Blocks.DIAMOND_BLOCK)));

		CustomRecipeBuilder.func_218656_a(ModRecipeTypes.LEXICON_ELVEN_TRADE_SERIALIZER).build(consumer, id("lexicon_elven").toString());
	}

	private static ResourceLocation id(String path) {
		return prefix("elven_trade/" + path);
	}

	@Override
	public String getName() {
		return "Botania elven trade recipes";
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> inputs;
		private final List<ItemStack> outputs;

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this(id, Arrays.asList(inputs), Collections.singletonList(output));
		}

		private FinishedRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> outputs) {
			this.id = id;
			this.inputs = inputs;
			this.outputs = outputs;
		}

		@Override
		public void serialize(JsonObject json) {
			JsonArray in = new JsonArray();
			for (Ingredient ingr : inputs) {
				in.add(ingr.serialize());
			}

			JsonArray out = new JsonArray();
			for (ItemStack s : outputs) {
				out.add(ItemNBTHelper.serializeStack(s));
			}

			json.add("ingredients", in);
			json.add("output", out);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.ELVEN_TRADE_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
