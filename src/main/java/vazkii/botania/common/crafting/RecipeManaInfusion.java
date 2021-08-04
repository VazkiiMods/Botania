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

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

public class RecipeManaInfusion implements IManaInfusionRecipe {
	private final ResourceLocation id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable
	private final StateIngredient catalyst;
	private final String group;

	public RecipeManaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable StateIngredient catalyst) {
		Preconditions.checkArgument(mana > 0, "Mana cost must be positive");
		Preconditions.checkArgument(mana <= 1_000_001, "Mana cost must be at most a pool"); // Leaving wiggle room for a certain modpack having creative-pool-only recipes
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		this.group = group == null ? "" : group;
		this.catalyst = catalyst;
	}

	@Deprecated
	public RecipeManaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable BlockState catalystState) {
		this(id, output, input, mana, group, StateIngredientHelper.of(catalystState));
	}

	@Nonnull
	@Override
	public final ResourceLocation getId() {
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
	public ItemStack getResultItem() {
		return output;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, input);
	}

	@Nonnull
	@Override
	public String getGroup() {
		return group;
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ModBlocks.manaPool);
	}

	public static class Serializer implements RecipeSerializer<RecipeManaInfusion> {

		@Nonnull
		@Override
		public RecipeManaInfusion fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			JsonElement input = Objects.requireNonNull(json.get("input"));
			Ingredient ing = Ingredient.fromJson(input);
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			int mana = GsonHelper.getAsInt(json, "mana");
			String group = GsonHelper.getAsString(json, "group", "");
			StateIngredient catalyst = null;
			if (json.has("catalyst")) { // TODO migrate entirely to state ingredients
				JsonElement element = json.get("catalyst");
				if (element.isJsonPrimitive()) {
					String s = GsonHelper.getAsString(json, "catalyst");
					ResourceLocation catalystId = ResourceLocation.tryParse(s);
					if (catalystId == null) {
						throw new IllegalArgumentException("Invalid catalyst ID: " + s);
					}
					catalyst = StateIngredientHelper.of(Registry.BLOCK.getOptional(catalystId)
							.orElseThrow(() -> new IllegalArgumentException("Unknown catalyst: " + s)));
				} else if (!element.getAsJsonObject().has("type")) {
					catalyst = StateIngredientHelper.of(StateIngredientHelper.readBlockState(GsonHelper.getAsJsonObject(json, "catalyst")));
				} else {
					catalyst = StateIngredientHelper.deserialize(element.getAsJsonObject());
				}
			}

			return new RecipeManaInfusion(id, output, ing, mana, group, catalyst);
		}

		@Nullable
		@Override
		public RecipeManaInfusion fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			Ingredient input = Ingredient.fromNetwork(buf);
			ItemStack output = buf.readItem();
			int mana = buf.readVarInt();
			StateIngredient catalyst = null;
			if (buf.readBoolean()) {
				catalyst = StateIngredientHelper.read(buf);
			}
			String group = buf.readUtf();
			return new RecipeManaInfusion(id, output, input, mana, group, catalyst);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getIngredients().get(0).toNetwork(buf);
			buf.writeItem(recipe.getResultItem());
			buf.writeVarInt(recipe.getManaToConsume());
			boolean hasCatalyst = recipe.getRecipeCatalyst() != null;
			buf.writeBoolean(hasCatalyst);
			if (hasCatalyst) {
				recipe.getRecipeCatalyst().write(buf);
			}
			buf.writeUtf(recipe.getGroup());
		}
	}
}
