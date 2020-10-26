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

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

import javax.annotation.Nonnull;

public class RecipeTerraPlate implements ITerraPlateRecipe {
	private final Identifier id;
	private final int mana;
	private final DefaultedList<Ingredient> inputs;
	private final ItemStack output;

	public RecipeTerraPlate(Identifier id, int mana, DefaultedList<Ingredient> inputs, ItemStack output) {
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
	public boolean matches(Inventory inv, @Nonnull World world) {
		int nonEmptySlots = 0;
		for (int i = 0; i < inv.size(); i++) {
			if (!inv.getStack(i).isEmpty()) {
				if (inv.getStack(i).getCount() > 1) {
					return false;
				}
				nonEmptySlots++;
			}
		}

		IntOpenHashSet usedSlots = new IntOpenHashSet(inv.size());
		return RecipeUtils.matches(inputs, inv, usedSlots) && usedSlots.size() == nonEmptySlots;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull Inventory inv) {
		return output.copy();
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return inputs;
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<RecipeTerraPlate> getSerializer() {
		return ModRecipeTypes.TERRA_PLATE_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<RecipeTerraPlate> {
		@Nonnull
		@Override
		public RecipeTerraPlate read(@Nonnull Identifier recipeId, @Nonnull JsonObject json) {
			int mana = JsonHelper.getInt(json, "mana");
			JsonArray ingrs = JsonHelper.getArray(json, "ingredients");
			Ingredient[] ingredients = new Ingredient[ingrs.size()];
			for (int i = 0; i < ingrs.size(); i++) {
				ingredients[i] = Ingredient.fromJson(ingrs.get(i));
			}
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			return new RecipeTerraPlate(recipeId, mana, DefaultedList.copyOf(Ingredient.EMPTY, ingredients), output);
		}

		@Override
		public RecipeTerraPlate read(@Nonnull Identifier recipeId, PacketByteBuf buffer) {
			int mana = buffer.readVarInt();
			Ingredient[] ingredients = new Ingredient[buffer.readVarInt()];
			for (int i = 0; i < ingredients.length; i++) {
				ingredients[i] = Ingredient.fromPacket(buffer);
			}
			ItemStack output = buffer.readItemStack();
			return new RecipeTerraPlate(recipeId, mana, DefaultedList.copyOf(Ingredient.EMPTY, ingredients), output);
		}

		@Override
		public void write(PacketByteBuf buffer, RecipeTerraPlate recipe) {
			buffer.writeVarInt(recipe.mana);
			buffer.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient ingr : recipe.getPreviewInputs()) {
				ingr.write(buffer);
			}
			buffer.writeItemStack(recipe.output);
		}
	}
}
