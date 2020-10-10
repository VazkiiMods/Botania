/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

import javax.annotation.Nonnull;

import java.util.List;

public class RecipeTerraPlate implements ITerraPlateRecipe {
	private final ResourceLocation id;
	private final int mana;
	private final List<Ingredient> inputs;
	private final ItemStack output;

	public RecipeTerraPlate(ResourceLocation id, int mana, List<Ingredient> inputs, ItemStack output) {
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
	public boolean matches(IInventory inv, @Nonnull World world) {
		int nonEmptySlots = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (inv.getStackInSlot(i).getCount() > 1) {
					return false;
				}
				nonEmptySlots++;
			}
		}

		IntOpenHashSet usedSlots = new IntOpenHashSet(inv.getSizeInventory());
		return RecipeUtils.matches(inputs, inv, usedSlots) && usedSlots.size() == nonEmptySlots;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return output.copy();
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<RecipeTerraPlate> getSerializer() {
		return ModRecipeTypes.TERRA_PLATE_SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeTerraPlate> {
		@Nonnull
		@Override
		public RecipeTerraPlate read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int mana = JSONUtils.getInt(json, "mana");
			ImmutableList.Builder<Ingredient> builder = ImmutableList.builder();
			for (JsonElement input : JSONUtils.getJsonArray(json, "ingredients")) {
				builder.add(Ingredient.deserialize(input));
			}
			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			return new RecipeTerraPlate(recipeId, mana, builder.build(), output);
		}

		@Override
		public RecipeTerraPlate read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
			int mana = buffer.readVarInt();
			int inputs = buffer.readVarInt();
			ImmutableList.Builder<Ingredient> builder = ImmutableList.builder();
			for (int i = 0; i < inputs; i++) {
				builder.add(Ingredient.read(buffer));
			}
			ItemStack output = buffer.readItemStack();
			return new RecipeTerraPlate(recipeId, mana, builder.build(), output);
		}

		@Override
		public void write(PacketBuffer buffer, RecipeTerraPlate recipe) {
			buffer.writeVarInt(recipe.mana);
			buffer.writeVarInt(recipe.inputs.size());
			for (Ingredient ingr : recipe.inputs) {
				ingr.write(buffer);
			}
			buffer.writeItemStack(recipe.output);
		}
	}
}
