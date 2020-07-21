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

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeElvenTrade implements IElvenTradeRecipe {
	private final Identifier id;
	private final ImmutableList<ItemStack> outputs;
	private final DefaultedList<Ingredient> inputs;

	public RecipeElvenTrade(Identifier id, ItemStack[] outputs, Ingredient... inputs) {
		this.id = id;
		this.outputs = ImmutableList.copyOf(outputs);
		this.inputs = DefaultedList.of();
		this.inputs.addAll(Arrays.asList(inputs));
	}

	@Override
	public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);
		List<ItemStack> stacksToRemove = new ArrayList<>();

		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) {
				continue;
			}
			if (inputsMissing.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int i = 0; i < inputsMissing.size(); i++) {
				Ingredient ingr = inputsMissing.get(i);
				if (ingr.test(stack)) {
					if (!stacksToRemove.contains(stack)) {
						stacksToRemove.add(stack);
					}
					stackIndex = i;
					break;
				}
			}

			if (stackIndex != -1) {
				inputsMissing.remove(stackIndex);
			}
		}

		return inputsMissing.isEmpty() ? Optional.of(stacksToRemove) : Optional.empty();
	}

	@Override
	public boolean containsItem(ItemStack stack) {
		for (Ingredient input : inputs) {
			if (input.test(stack)) {
				return true;
			}
		}
		return false;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.ELVEN_TRADE_SERIALIZER;
	}

	@Nonnull
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.alfPortal);
	}

	@Nonnull
	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		return getOutputs();
	}

	public static class Serializer implements RecipeSerializer<RecipeElvenTrade> {

		@Nonnull
		@Override
		public RecipeElvenTrade read(@Nonnull Identifier id, @Nonnull JsonObject json) {
			JsonElement output = json.get("output");
			List<ItemStack> outputStacks = new ArrayList<>();
			if (output.isJsonArray()) {
				for (JsonElement e : output.getAsJsonArray()) {
					JsonObject o = JsonHelper.asObject(e, "output stack");
					outputStacks.add(CraftingHelper.getItemStack(o, true));
				}
			} else {
				JsonObject o = JsonHelper.asObject(output, "output stack");
				outputStacks.add(CraftingHelper.getItemStack(o, true));
			}

			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : JsonHelper.getArray(json, "ingredients")) {
				Ingredient ing = Ingredient.fromJson(e);
				if (!ing.isEmpty()) {
					inputs.add(ing);
				}
			}

			return new RecipeElvenTrade(id, outputStacks.toArray(new ItemStack[0]), inputs.toArray(new Ingredient[0]));
		}

		@Override
		public RecipeElvenTrade read(@Nonnull Identifier id, PacketByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromPacket(buf);
			}
			ItemStack[] outputs = new ItemStack[buf.readVarInt()];
			for (int i = 0; i < outputs.length; i++) {
				outputs[i] = buf.readItemStack();
			}
			return new RecipeElvenTrade(id, outputs, inputs);
		}

		@Override
		public void write(PacketByteBuf buf, RecipeElvenTrade recipe) {
			buf.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient input : recipe.getPreviewInputs()) {
				input.write(buf);
			}
			buf.writeVarInt(recipe.getOutputs().size());
			for (ItemStack output : recipe.getOutputs()) {
				buf.writeItemStack(output, false);
			}
		}
	}
}
