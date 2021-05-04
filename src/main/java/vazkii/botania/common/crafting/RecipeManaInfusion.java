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
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		Preconditions.checkArgument(mana < 100000);
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
	public IRecipeSerializer<RecipeManaInfusion> getSerializer() {
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
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, input);
	}

	@Nonnull
	@Override
	public String getGroup() {
		return group;
	}

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.manaPool);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeManaInfusion> {

		@Nonnull
		@Override
		public RecipeManaInfusion read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			JsonElement input = Objects.requireNonNull(json.get("input"));
			Ingredient ing = Ingredient.deserialize(input);
			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
			int mana = JSONUtils.getInt(json, "mana");
			String group = JSONUtils.getString(json, "group", "");
			StateIngredient catalyst = null;
			if (json.has("catalyst")) { // TODO migrate entirely to state ingredients
				JsonElement element = json.get("catalyst");
				if (element.isJsonPrimitive()) {
					String s = JSONUtils.getString(json, "catalyst");
					ResourceLocation catalystId = ResourceLocation.tryCreate(s);
					if (catalystId == null) {
						throw new IllegalArgumentException("Invalid catalyst ID: " + s);
					}
					catalyst = StateIngredientHelper.of(Registry.BLOCK.getOptional(catalystId)
							.orElseThrow(() -> new IllegalArgumentException("Unknown catalyst: " + s)));
				} else if (!element.getAsJsonObject().has("type")) {
					catalyst = StateIngredientHelper.of(StateIngredientHelper.readBlockState(JSONUtils.getJsonObject(json, "catalyst")));
				} else {
					catalyst = StateIngredientHelper.deserialize(element.getAsJsonObject());
				}
			}

			return new RecipeManaInfusion(id, output, ing, mana, group, catalyst);
		}

		@Nullable
		@Override
		public RecipeManaInfusion read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Ingredient input = Ingredient.read(buf);
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
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getIngredients().get(0).write(buf);
			buf.writeItemStack(recipe.getRecipeOutput(), false);
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
