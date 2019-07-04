package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

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

	public boolean matches(List<ItemStack> stacks, boolean remove) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);
		List<ItemStack> stacksToRemove = new ArrayList<>();

		for(ItemStack stack : stacks) {
			if(stack.isEmpty()) {
				continue;
			}
			if(inputsMissing.isEmpty())
				break;

			int stackIndex = -1;

			for (int i = 0; i < inputsMissing.size(); i++) {
				Ingredient ingr = inputsMissing.get(i);
				if (ingr.test(stack)) {
					if(!stacksToRemove.contains(stack))
						stacksToRemove.add(stack);
					stackIndex = i;
					break;
				}
			}

			if(stackIndex != -1)
				inputsMissing.remove(stackIndex);
		}

		if(remove)
			for(ItemStack r : stacksToRemove)
				stacks.remove(r);

		return inputsMissing.isEmpty();
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

}
