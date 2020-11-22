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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class RecipePetals implements IPetalRecipe {
	private final Identifier id;
	private final ItemStack output;
	private final DefaultedList<Ingredient> inputs;

	public RecipePetals(Identifier id, ItemStack output, Ingredient... inputs) {
		this.id = id;
		this.output = output;
		this.inputs = DefaultedList.copyOf(Ingredient.EMPTY, inputs);
	}

	@Override
	public boolean matches(Inventory inv, @Nonnull World world) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack input = inv.getStack(i);
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
	public final ItemStack getOutput() {
		return output;
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull Inventory inv) {
		return getOutput().copy();
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.defaultAltar);
	}

	@Nonnull
	@Override
	public Identifier getId() {
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
		public RecipePetals read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "output"));
			JsonArray ingrs = JsonHelper.getArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new RecipePetals(id, output, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipePetals read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromPacket(buf);
			}
			ItemStack output = buf.readItemStack();
			return new RecipePetals(id, output, inputs);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull RecipePetals recipe) {
			buf.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient input : recipe.getPreviewInputs()) {
				input.write(buf);
			}
			buf.writeItemStack(recipe.getOutput());
		}

	}

}
