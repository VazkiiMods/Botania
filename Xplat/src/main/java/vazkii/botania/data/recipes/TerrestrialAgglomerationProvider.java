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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TerrestrialAgglomerationProvider extends BotaniaRecipeProvider {
	public TerrestrialAgglomerationProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	public String getName() {
		return "Botania Terra Plate recipes";
	}

	@Override
	public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(idFor("terrasteel_ingot"), ManaPoolBlockEntity.MAX_MANA / 2,
				new ItemStack(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.manaSteel),
				Ingredient.of(BotaniaItems.manaPearl), Ingredient.of(BotaniaItems.manaDiamond)));
		consumer.accept(new FinishedRecipe(idFor("fruit_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.infiniteFruit), Ingredient.of(BotaniaItems.mock_infiniteFruit),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
		consumer.accept(new FinishedRecipe(idFor("key_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.kingKey), Ingredient.of(BotaniaItems.mock_kingKey),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
		consumer.accept(new FinishedRecipe(idFor("eye_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.flugelEye), Ingredient.of(BotaniaItems.mock_flugelEye),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
		consumer.accept(new FinishedRecipe(idFor("loki_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.lokiRing), Ingredient.of(BotaniaItems.mock_lokiRing),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
		consumer.accept(new FinishedRecipe(idFor("odin_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.odinRing), Ingredient.of(BotaniaItems.mock_odinRing),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
		consumer.accept(new FinishedRecipe(idFor("thor_restore"), ManaPoolBlockEntity.MAX_MANA,
				new ItemStack(BotaniaItems.thorRing), Ingredient.of(BotaniaItems.mock_thorRing),
				Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.dice)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("terra_plate/" + s);
	}

	protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private final ResourceLocation id;
		private final int mana;
		private final ItemStack output;
		private final Ingredient[] inputs;

		public FinishedRecipe(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs) {
			this.id = id;
			this.mana = mana;
			this.output = output;
			this.inputs = inputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("mana", mana);
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.toJson());
			}
			json.add("ingredients", ingredients);
			json.add("result", ItemNBTHelper.serializeStack(output));
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return BotaniaRecipeTypes.TERRA_PLATE_SERIALIZER;
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
