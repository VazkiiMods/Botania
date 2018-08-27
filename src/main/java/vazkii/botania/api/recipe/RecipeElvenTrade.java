package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RecipeElvenTrade {

	private final ImmutableList<ItemStack> outputs;
	private final ImmutableList<Object> inputs;

	public RecipeElvenTrade(ItemStack[] outputs, Object... inputs) {
		this.outputs = ImmutableList.copyOf(outputs);

		ImmutableList.Builder<Object> inputsToSet = ImmutableList.builder();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet.build();
	}

	public boolean matches(List<ItemStack> stacks, boolean remove) {
		List<Object> inputsMissing = new ArrayList<>(inputs);
		List<ItemStack> stacksToRemove = new ArrayList<>();

		for(ItemStack stack : stacks) {
			if(stack.isEmpty()) {
				continue;
			}
			if(inputsMissing.isEmpty())
				break;

			int stackIndex = -1, oredictIndex = -1;

			for(int j = 0; j < inputsMissing.size(); j++) {
				Object input = inputsMissing.get(j);
				if(input instanceof String) {
					List<ItemStack> validStacks = OreDictionary.getOres((String) input);
					boolean found = false;
					for(ItemStack ostack : validStacks) {
						if(OreDictionary.itemMatches(ostack, stack, false)) {
							if(!stacksToRemove.contains(stack))
								stacksToRemove.add(stack);
							oredictIndex = j;
							found = true;
							break;
						}
					}

					if(found)
						break;
				} else if(input instanceof ItemStack && simpleAreStacksEqual((ItemStack) input, stack)) {
					if(!stacksToRemove.contains(stack))
						stacksToRemove.add(stack);
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				inputsMissing.remove(stackIndex);
			else if(oredictIndex != -1)
				inputsMissing.remove(oredictIndex);
		}

		if(remove)
			for(ItemStack r : stacksToRemove)
				stacks.remove(r);

		return inputsMissing.isEmpty();
	}

	private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
		return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
	}

	public List<Object> getInputs() {
		return inputs;
	}

	public List<ItemStack> getOutputs() {
		return outputs;
	}

}
