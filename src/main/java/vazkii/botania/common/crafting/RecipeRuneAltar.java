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

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class RecipeRuneAltar implements IRuneAltarRecipe {
	private final Identifier id;
	private final ItemStack output;
	private final DefaultedList<Ingredient> inputs;
	private final int mana;

	public RecipeRuneAltar(Identifier id, ItemStack output, int mana, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16);
		Preconditions.checkArgument(mana <= 100000);
		this.id = id;
		this.output = output;
		this.inputs = DefaultedList.copyOf(Ingredient.EMPTY, inputs);
		this.mana = mana;
	}

	@Override
	public boolean matches(Inventory inv, @Nonnull World world) {
		return RecipeUtils.matches(inputs, inv, null);
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
		return new ItemStack(ModBlocks.runeAltar);
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.RUNE_SERIALIZER;
	}

	@Override
	public int getManaUsage() {
		return mana;
	}

	public static class Serializer implements RecipeSerializer<RecipeRuneAltar> {
		@Nonnull
		@Override
		public RecipeRuneAltar read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "output"));
			int mana = JsonHelper.getInt(json, "mana");
			JsonArray ingrs = JsonHelper.getArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new RecipeRuneAltar(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeRuneAltar read(@Nonnull Identifier id, @Nonnull PacketByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromPacket(buf);
			}
			ItemStack output = buf.readItemStack();
			int mana = buf.readVarInt();
			return new RecipeRuneAltar(id, output, mana, inputs);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buf, @Nonnull RecipeRuneAltar recipe) {
			buf.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient input : recipe.getPreviewInputs()) {
				input.write(buf);
			}
			buf.writeItemStack(recipe.getOutput());
			buf.writeVarInt(recipe.getManaUsage());
		}
	}

}
