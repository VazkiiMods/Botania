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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

public class RecipeManaInfusion implements IManaInfusionRecipe {
	private final Identifier id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable
	private final StateIngredient catalyst;
	private final String group;

	public RecipeManaInfusion(Identifier id, ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable StateIngredient catalyst) {
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		Preconditions.checkArgument(mana < 100000);
		this.group = group == null ? "" : group;
		this.catalyst = catalyst;
	}

	@Deprecated
	public RecipeManaInfusion(Identifier id, ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable BlockState catalystState) {
		this(id, output, input, mana, group, StateIngredientHelper.of(catalystState));
	}

	@Nonnull
	@Override
	public final Identifier getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<RecipeManaInfusion> getSerializer() {
		return ModRecipeTypes.MANA_INFUSION_SERIALIZER;
	}

	@Override
	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	@Nullable
	@Override
	public BlockState getCatalyst() {
		if (catalyst == null) {
			return null;
		}
		return catalyst.getDisplayed().get(0);
	}

	@Override
	public StateIngredient getRecipeCatalyst() {
		return catalyst;
	}

	@Override
	public int getManaToConsume() {
		return mana;
	}

	@Nonnull
	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.copyOf(Ingredient.EMPTY, input);
	}

	@Nonnull
	@Override
	public String getGroup() {
		return group;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.manaPool);
	}

	public static class Serializer implements RecipeSerializer<RecipeManaInfusion> {

		@Nonnull
		@Override
		public RecipeManaInfusion read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			JsonElement input = Objects.requireNonNull(json.get("input"));
			Ingredient ing = Ingredient.fromJson(input);
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "output"));
			int mana = JsonHelper.getInt(json, "mana");
			String group = JsonHelper.getString(json, "group", "");
			StateIngredient catalyst = null;
			if (json.has("catalyst")) { // TODO migrate entirely to state ingredients
				JsonElement element = json.get("catalyst");
				if (element.isJsonPrimitive()) {
					String s = JsonHelper.getString(json, "catalyst");
					Identifier catalystId = Identifier.tryParse(s);
					if (catalystId == null) {
						throw new IllegalArgumentException("Invalid catalyst ID: " + s);
					}
					catalyst = StateIngredientHelper.of(Registry.BLOCK.getOrEmpty(catalystId)
							.orElseThrow(() -> new IllegalArgumentException("Unknown catalyst: " + s)));
				} else if (!element.getAsJsonObject().has("type")) {
					catalyst = StateIngredientHelper.of(StateIngredientHelper.readBlockState(JsonHelper.getObject(json, "catalyst")));
				} else {
					catalyst = StateIngredientHelper.deserialize(element.getAsJsonObject());
				}
			}

			return new RecipeManaInfusion(id, output, ing, mana, group, catalyst);
		}

		@Nullable
		@Override
		public RecipeManaInfusion read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			Ingredient input = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			StateIngredient catalyst = null;
			if (buf.readBoolean()) {
				catalyst = StateIngredientHelper.read(buf);
			}
			String group = buf.readString();
			return new RecipeManaInfusion(id, output, input, mana, group, catalyst);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getPreviewInputs().get(0).write(buf);
			buf.writeItemStack(recipe.getOutput());
			buf.writeVarInt(recipe.getManaToConsume());
			boolean hasCatalyst = recipe.getRecipeCatalyst() != null;
			buf.writeBoolean(hasCatalyst);
			if (hasCatalyst) {
				recipe.getRecipeCatalyst().write(buf);
			}
			buf.writeString(recipe.getGroup());
		}
	}
}
