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

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeProvider extends BotaniaRecipeProvider {
	public ElvenTradeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(new FinishedElvenRecipe(id("dreamwood_log"), new ItemStack(BotaniaBlocks.dreamwoodLog), Ingredient.of(BotaniaBlocks.livingwoodLog)));
		consumer.accept(new FinishedElvenRecipe(id("dreamwood"), new ItemStack(BotaniaBlocks.dreamwood), Ingredient.of(BotaniaBlocks.livingwood)));

		Ingredient manaDiamond = Ingredient.of(BotaniaTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaSteel = Ingredient.of(BotaniaTags.Items.INGOTS_MANASTEEL);
		consumer.accept(new FinishedElvenRecipe(id("elementium"), new ItemStack(BotaniaItems.elementium), manaSteel, manaSteel));
		consumer.accept(new FinishedElvenRecipe(id("elementium_block"), new ItemStack(BotaniaBlocks.elementiumBlock), Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.manasteelBlock)));

		consumer.accept(new FinishedElvenRecipe(id("pixie_dust"), new ItemStack(BotaniaItems.pixieDust), Ingredient.of(BotaniaItems.manaPearl)));
		consumer.accept(new FinishedElvenRecipe(id("dragonstone"), new ItemStack(BotaniaItems.dragonstone), manaDiamond));
		consumer.accept(new FinishedElvenRecipe(id("dragonstone_block"), new ItemStack(BotaniaBlocks.dragonstoneBlock), Ingredient.of(BotaniaBlocks.manaDiamondBlock)));

		consumer.accept(new FinishedElvenRecipe(id("elf_quartz"), new ItemStack(BotaniaItems.elfQuartz), Ingredient.of(Items.QUARTZ)));
		consumer.accept(new FinishedElvenRecipe(id("elf_glass"), new ItemStack(BotaniaBlocks.elfGlass), Ingredient.of(BotaniaBlocks.manaGlass)));

		consumer.accept(new FinishedElvenRecipe(id("iron_return"), new ItemStack(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT)));
		consumer.accept(new FinishedElvenRecipe(id("iron_block_return"), new ItemStack(Blocks.IRON_BLOCK), Ingredient.of(Blocks.IRON_BLOCK)));
		consumer.accept(new FinishedElvenRecipe(id("ender_pearl_return"), new ItemStack(Items.ENDER_PEARL), Ingredient.of(Items.ENDER_PEARL)));
		consumer.accept(new FinishedElvenRecipe(id("diamond_return"), new ItemStack(Items.DIAMOND), Ingredient.of(Items.DIAMOND)));
		consumer.accept(new FinishedElvenRecipe(id("diamond_block_return"), new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.of(Blocks.DIAMOND_BLOCK)));

		// TODO 1.19.3 check that this works
		consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(JsonObject obj) {}

			@Override
			public ResourceLocation getId() {
				return id("lexicon_elven");
			}

			@Override
			public RecipeSerializer<?> getType() {
				return BotaniaRecipeTypes.LEXICON_ELVEN_TRADE_SERIALIZER;
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
		});
	}

	private static ResourceLocation id(String path) {
		return prefix("elven_trade/" + path);
	}

	@Override
	public String getName() {
		return "Botania elven trade recipes";
	}

	protected static class FinishedElvenRecipe implements FinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> inputs;
		private final List<ItemStack> outputs;

		public FinishedElvenRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this(id, Arrays.asList(inputs), Collections.singletonList(output));
		}

		protected FinishedElvenRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> outputs) {
			this.id = id;
			this.inputs = inputs;
			this.outputs = outputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			JsonArray in = new JsonArray();
			for (Ingredient ingr : inputs) {
				in.add(ingr.toJson());
			}

			JsonArray out = new JsonArray();
			for (ItemStack s : outputs) {
				out.add(ItemNBTHelper.serializeStack(s));
			}

			json.add("ingredients", in);
			json.add("output", out);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return BotaniaRecipeTypes.ELVEN_TRADE_SERIALIZER;
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
}
