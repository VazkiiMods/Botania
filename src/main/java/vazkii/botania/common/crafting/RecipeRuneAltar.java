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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
		Preconditions.checkArgument(inputs.length <= 16);
		Preconditions.checkArgument(mana <= 100000);
		this.id = id;
		this.output = output;
		this.inputs = NonNullList.from(null, inputs);
		this.mana = mana;
	}

	@Override
	public boolean matches(IInventory inv, @Nonnull World world) {
		return RecipeUtils.matches(inputs, inv, null);
	}

	@Nonnull
	@Override
	public final ItemStack getRecipeOutput() {
		return output;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		return getRecipeOutput().copy();
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.runeAltar);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.RUNE_SERIALIZER;
	}

	@Override
	public int getManaUsage() {
		return mana;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeRuneAltar> {
		@Nonnull
		@Override
		public RecipeRuneAltar read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
			ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
			int mana = JSONUtils.getInt(json, "mana");
			JsonArray ingrs = JSONUtils.getJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.deserialize(e));
			}
			return new RecipeRuneAltar(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeRuneAltar read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.read(buf);
			}
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			return new RecipeRuneAltar(id, output, mana, inputs);
		}

		@Override
		public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeRuneAltar recipe) {
			buf.writeVarInt(recipe.getIngredients().size());
			for (Ingredient input : recipe.getIngredients()) {
				input.write(buf);
			}
			buf.writeItemStack(recipe.getRecipeOutput(), false);
			buf.writeVarInt(recipe.getManaUsage());
		}
	}

}
