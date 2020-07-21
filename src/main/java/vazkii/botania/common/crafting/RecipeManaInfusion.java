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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

public class RecipeManaInfusion implements IManaInfusionRecipe {
	private final Identifier id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable
	private final BlockState catalystState;
	private final String group;

	public RecipeManaInfusion(Identifier id, ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable BlockState catalystState) {
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		Preconditions.checkArgument(mana < 100000);
		this.group = group == null ? "" : group;
		this.catalystState = catalystState;
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
		return catalystState;
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
		return DefaultedList.copyOf(null, input);
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

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeManaInfusion> {

		@Nonnull
		@Override
		public RecipeManaInfusion read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			JsonElement input = Objects.requireNonNull(json.get("input"));
			Ingredient ing = Ingredient.fromJson(input);
			ItemStack output = CraftingHelper.getItemStack(JsonHelper.getObject(json, "output"), true);
			int mana = JsonHelper.getInt(json, "mana");
			String group = JsonHelper.getString(json, "group", "");
			BlockState catalystState = null;
			if (json.has("catalyst")) {
				if (json.get("catalyst").isJsonPrimitive()) {
					String s = JsonHelper.getString(json, "catalyst");
					Identifier catalystId = Identifier.tryParse(s);
					if (catalystId == null) {
						throw new IllegalArgumentException("Invalid catalyst ID: " + s);
					}
					Optional<Block> cat = Registry.BLOCK.getOrEmpty(catalystId);
					if (!cat.isPresent()) {
						throw new IllegalArgumentException("Unknown catalyst: " + s);
					}
					catalystState = cat.get().getDefaultState();
				} else {
					catalystState = StateIngredientHelper.readBlockState(JsonHelper.getObject(json, "catalyst"));
				}
			}

			RecipeManaInfusion ret = new RecipeManaInfusion(id, output, ing, mana, group, catalystState);
			return ret;
		}

		@Nullable
		@Override
		public RecipeManaInfusion read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			Ingredient input = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			int catalystId = buf.readInt();
			String group = buf.readString();
			BlockState catalystState = catalystId == -1 ? null : Block.getStateFromRawId(catalystId);
			return new RecipeManaInfusion(id, output, input, mana, group, catalystState);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getPreviewInputs().get(0).write(buf);
			buf.writeItemStack(recipe.getOutput(), false);
			buf.writeVarInt(recipe.getManaToConsume());
			buf.writeInt(recipe.getCatalyst() == null ? -1 : Block.getRawIdFromState(recipe.getCatalyst()));
			buf.writeString(recipe.getGroup());
		}
	}
}
