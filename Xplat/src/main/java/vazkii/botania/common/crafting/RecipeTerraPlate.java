/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

import javax.annotation.Nonnull;

public class RecipeTerraPlate implements ITerraPlateRecipe {
	private final ResourceLocation id;
	private final int mana;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;

	public RecipeTerraPlate(ResourceLocation id, int mana, NonNullList<Ingredient> inputs, ItemStack output) {
		this.id = id;
		this.mana = mana;
		this.inputs = inputs;
		this.output = output;
	}

	@Override
	public int getMana() {
		return mana;
	}

	@Override
	public boolean matches(Container inv, @Nonnull Level world) {
		int nonEmptySlots = 0;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (!inv.getItem(i).isEmpty()) {
				if (inv.getItem(i).getCount() > 1) {
					return false;
				}
				nonEmptySlots++;
			}
		}

		IntOpenHashSet usedSlots = new IntOpenHashSet(inv.getContainerSize());
		return RecipeUtils.matches(inputs, inv, usedSlots) && usedSlots.size() == nonEmptySlots;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull Container inv) {
		return output.copy();
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<RecipeTerraPlate> getSerializer() {
		return ModRecipeTypes.TERRA_PLATE_SERIALIZER;
	}

	public static class Serializer extends RecipeSerializerBase<RecipeTerraPlate> {
		@Nonnull
		@Override
		public RecipeTerraPlate fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			Ingredient[] ingredients = new Ingredient[ingrs.size()];
			for (int i = 0; i < ingrs.size(); i++) {
				ingredients[i] = Ingredient.fromJson(ingrs.get(i));
			}
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			return new RecipeTerraPlate(recipeId, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}

		@Override
		public RecipeTerraPlate fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int mana = buffer.readVarInt();
			Ingredient[] ingredients = new Ingredient[buffer.readVarInt()];
			for (int i = 0; i < ingredients.length; i++) {
				ingredients[i] = Ingredient.fromNetwork(buffer);
			}
			ItemStack output = buffer.readItem();
			return new RecipeTerraPlate(recipeId, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, RecipeTerraPlate recipe) {
			buffer.writeVarInt(recipe.mana);
			buffer.writeVarInt(recipe.getIngredients().size());
			for (Ingredient ingr : recipe.getIngredients()) {
				ingr.toNetwork(buffer);
			}
			buffer.writeItem(recipe.output);
		}
	}
}
