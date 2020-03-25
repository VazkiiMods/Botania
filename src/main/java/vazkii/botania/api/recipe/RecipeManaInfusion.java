/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import com.google.common.base.Preconditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class RecipeManaInfusion implements IRecipe<IInventory> {
	@ObjectHolder("botania:alchemy_catalyst") public static Block alchemy;

	@ObjectHolder("botania:conjuration_catalyst") public static Block conjuration;

	private final ResourceLocation id;
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable private BlockState catalystState;
	private final String group;

	public RecipeManaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana, @Nullable String group) {
		this.id = id;
		this.output = output;
		this.input = input;
		this.mana = mana;
		Preconditions.checkArgument(mana < 100000);
		this.group = group == null ? "" : group;
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

	@Nonnull
	@Override
	public IRecipeType<?> getType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}

	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	@Nullable
	public BlockState getCatalyst() {
		return catalystState;
	}

	public void setCatalyst(@Nullable BlockState catalyst) {
		catalystState = catalyst;
	}

	public Ingredient getInput() {
		return input;
	}

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
		return NonNullList.from(input);
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

	// Ignored IRecipe stuff

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return output;
	}
	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return false;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
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
			String catalyst = JSONUtils.getString(json, "catalyst", "");
			BlockState catalystState = null;
			if (!catalyst.isEmpty()) {
				try {
					BlockStateParser parser = new BlockStateParser(new StringReader(catalyst), false).parse(false);
					catalystState = parser.getState();
				} catch (CommandSyntaxException e) {
					throw new JsonParseException("Failure reading catalyst state", e);
				}
			}

			RecipeManaInfusion ret = new RecipeManaInfusion(id, output, ing, mana, group);
			ret.setCatalyst(catalystState);
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
			RecipeManaInfusion ret = new RecipeManaInfusion(id, output, input, mana, group);
			ret.setCatalyst(catalystId == -1 ? null : Block.getStateById(catalystId));
			return ret;
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeManaInfusion recipe) {
			recipe.getInput().write(buf);
			buf.writeItemStack(recipe.getRecipeOutput(), false);
			buf.writeVarInt(recipe.getManaToConsume());
			buf.writeInt(recipe.getCatalyst() == null ? -1 : Block.getStateId(recipe.getCatalyst()));
			buf.writeString(recipe.getGroup());
		}
	}
}
