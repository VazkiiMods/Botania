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

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TerraPlateProvider extends BotaniaRecipeProvider {
	public TerraPlateProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania Terra Plate recipes";
	}

	@Override
	public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(idFor("terrasteel_ingot"), TilePool.MAX_MANA / 2,
				new ItemStack(ModItems.terrasteel), Ingredient.of(ModItems.manaSteel),
				Ingredient.of(ModItems.manaPearl), Ingredient.of(ModItems.manaDiamond)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("terra_plate/" + s);
	}

	private static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private final ResourceLocation id;
		private final int mana;
		private final ItemStack output;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs) {
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
			return ModRecipeTypes.TERRA_PLATE_SERIALIZER;
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
