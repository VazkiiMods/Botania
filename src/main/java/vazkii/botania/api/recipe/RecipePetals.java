/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 2:02:44 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class RecipePetals implements IModRecipe {
	private final ResourceLocation id;
	private final ItemStack output;
	private final ImmutableList<Ingredient> inputs;

	public RecipePetals(ResourceLocation id, ItemStack output, Ingredient... inputs) {
		this.id = id;
		this.output = output;
		this.inputs = ImmutableList.copyOf(inputs);
	}

	public boolean matches(IItemHandler inv) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack input = inv.getStackInSlot(i);
			if(input.isEmpty())
				break;

			int stackIndex = -1;

			for(int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if(ingr.test(input)) {
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				ingredientsMissing.remove(stackIndex);
			else return false;
		}

		return ingredientsMissing.isEmpty();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public List<Ingredient> getInputs() {
		return inputs;
	}

	public ItemStack getOutput() {
		return output;
	}

	public void write(PacketBuffer buf) {
		buf.writeResourceLocation(id);
		buf.writeVarInt(inputs.size());
		for (Ingredient input : inputs) {
			input.write(buf);
		}
		buf.writeItemStack(output, false);
	}

	public static RecipePetals read(PacketBuffer buf) {
		ResourceLocation id = buf.readResourceLocation();
		Ingredient[] inputs = new Ingredient[buf.readVarInt()];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = Ingredient.read(buf);
		}
		ItemStack output = buf.readItemStack();
		return new RecipePetals(id, output, inputs);
	}

}
