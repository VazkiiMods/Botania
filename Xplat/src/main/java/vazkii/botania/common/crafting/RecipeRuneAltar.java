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

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class RecipeRuneAltar implements IRuneAltarRecipe {
	private final ResourceLocation id;
	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final int mana;

	public RecipeRuneAltar(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16, "Cannot have more than 16 ingredients");
		this.id = id;
		this.output = output;
		this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
		this.mana = mana;
	}

	@Override
	public boolean matches(Container inv, @Nonnull Level world) {
		return RecipeUtils.matches(inputs, inv, null);
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
		return new ItemStack(ModBlocks.runeAltar);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.RUNE_SERIALIZER;
	}

	@Override
	public int getManaUsage() {
		return mana;
	}

	public static class Serializer extends RecipeSerializerBase<RecipeRuneAltar> {
		@Nonnull
		@Override
		public RecipeRuneAltar fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new RecipeRuneAltar(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeRuneAltar fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromNetwork(buf);
			}
			ItemStack output = buf.readItem();
			int mana = buf.readVarInt();
			return new RecipeRuneAltar(id, output, mana, inputs);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull RecipeRuneAltar recipe) {
			buf.writeVarInt(recipe.getIngredients().size());
			for (Ingredient input : recipe.getIngredients()) {
				input.toNetwork(buf);
			}
			buf.writeItem(recipe.getResultItem());
			buf.writeVarInt(recipe.getManaUsage());
		}
	}

}
