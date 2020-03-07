/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeElvenTrade {
	private final ResourceLocation id;
	private final ImmutableList<ItemStack> outputs;
	private final ImmutableList<Ingredient> inputs;

	public RecipeElvenTrade(ResourceLocation id, ItemStack output, Ingredient... inputs) {
		this(id, new ItemStack[] { output }, inputs);
	}

	public RecipeElvenTrade(ResourceLocation id, ItemStack[] outputs, Ingredient... inputs) {
		this.id = id;
		this.outputs = ImmutableList.copyOf(outputs);
		this.inputs = ImmutableList.copyOf(inputs);
	}

	/**
	 * Attempts to match the recipe
	 * 
	 * @param  stacks Entire contents of the portal's buffer
	 * @return        {@link Optional#empty()} if recipe doesn't match, Optional with a set of items used by recipe
	 *                otherwise
	 */
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

	/**
	 * If the recipe does not contain the item, it will be destroyed upon entering the portal.
	 */
	public boolean containsItem(ItemStack stack) {
		for (Ingredient input : inputs) {
			if (input.test(stack)) {
				return true;
			}
		}
		return false;
	}

	public ResourceLocation getId() {
		return id;
	}

	public List<Ingredient> getInputs() {
		return inputs;
	}

	public List<ItemStack> getOutputs() {
		return outputs;
	}

	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		return getOutputs();
	}

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(id);
		buf.writeVarInt(inputs.size());
		for (Ingredient input : inputs) {
			input.write(buf);
		}
		buf.writeVarInt(outputs.size());
		for (ItemStack output : outputs) {
			buf.writeItemStack(output, false);
		}
	}

	public static RecipeElvenTrade read(PacketBuffer buf) {
		ResourceLocation id = buf.readResourceLocation();
		Ingredient[] inputs = new Ingredient[buf.readVarInt()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = Ingredient.read(buf);
		}
		ItemStack[] outputs = new ItemStack[buf.readVarInt()];
		for (int i = 0; i < outputs.length; i++) {
			outputs[i] = buf.readItemStack();
		}
		return new RecipeElvenTrade(id, outputs, inputs);
	}
}
