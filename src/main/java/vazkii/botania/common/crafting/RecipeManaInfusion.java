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
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
	private final ResourceLocation id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable private final BlockState catalystState;
	private final String group;

	public RecipeManaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana,
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
		return catalystState;
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
		return NonNullList.from(null, input);
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
			ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
			int mana = JSONUtils.getInt(json, "mana");
			String group = JSONUtils.getString(json, "group", "");
			BlockState catalystState = null;
			if (json.has("catalyst")) {
				if (json.get("catalyst").isJsonPrimitive()) {
					String s = JSONUtils.getString(json, "catalyst");
					ResourceLocation catalystId = ResourceLocation.tryCreate(s);
					if (catalystId == null) {
						throw new IllegalArgumentException("Invalid catalyst ID: " + s);
					}
					Optional<Block> cat = Registry.BLOCK.getValue(catalystId);
					if (!cat.isPresent()) {
						throw new IllegalArgumentException("Unknown catalyst: " + s);
					}
					catalystState = cat.get().getDefaultState();
				} else {
					catalystState = StateIngredientHelper.readBlockState(JSONUtils.getJsonObject(json, "catalyst"));
				}
			}

			RecipeManaInfusion ret = new RecipeManaInfusion(id, output, ing, mana, group, catalystState);
			return ret;
		}

		@Nullable
		@Override
		public RecipeManaInfusion read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Ingredient input = Ingredient.read(buf);
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			int catalystId = buf.readInt();
			String group = buf.readString();
			BlockState catalystState = catalystId == -1 ? null : Block.getStateById(catalystId);
			return new RecipeManaInfusion(id, output, input, mana, group, catalystState);
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getIngredients().get(0).write(buf);
			buf.writeItemStack(recipe.getRecipeOutput(), false);
			buf.writeVarInt(recipe.getManaToConsume());
			buf.writeInt(recipe.getCatalyst() == null ? -1 : Block.getStateId(recipe.getCatalyst()));
			buf.writeString(recipe.getGroup());
		}
	}
}
