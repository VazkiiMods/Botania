/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class RecipePetals implements IPetalRecipe {
	private final ResourceLocation id;
	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;

	public RecipePetals(ResourceLocation id, ItemStack output, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16, "Cannot have more than 16 ingredients");
		this.id = id;
		this.output = output;
		this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
	}

	@Override
	public boolean matches(Container inv, @Nonnull Level world) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack input = inv.getItem(i);
			if (input.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if (ingr.test(input)) {
					stackIndex = j;
					break;
				}
			}

			if (stackIndex != -1) {
				ingredientsMissing.remove(stackIndex);
			} else {
				return false;
			}
		}

		return ingredientsMissing.isEmpty();
	}

	@Nonnull
	@Override
	public final ItemStack getResultItem() {
		return output;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull Container inv) {
		return getResultItem().copy();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ModBlocks.defaultAltar);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.PETAL_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<RecipePetals> {
		@Nonnull
		@Override
		public RecipePetals fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new RecipePetals(id, output, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipePetals fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromNetwork(buf);
			}
			ItemStack output = buf.readItem();
			return new RecipePetals(id, output, inputs);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull RecipePetals recipe) {
			buf.writeVarInt(recipe.getIngredients().size());
			for (Ingredient input : recipe.getIngredients()) {
				input.toNetwork(buf);
			}
			buf.writeItem(recipe.getResultItem());
		}

	}

}
