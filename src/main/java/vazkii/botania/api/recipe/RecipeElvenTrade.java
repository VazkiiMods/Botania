package vazkii.botania.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeElvenTrade {

	ItemStack output;
	List<Object> inputs;

	public RecipeElvenTrade(ItemStack output, Object... inputs) {
		this.output = output;

		List<Object> inputsToSet = new ArrayList();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet;
	}

	public boolean matches(List<ItemStack> stacks, boolean remove) {
		List<Object> inputsMissing = new ArrayList(inputs);
		List<ItemStack> stacksToRemove = new ArrayList();

		for(ItemStack stack : stacks) {
			if(stack == null) {
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
						ItemStack cstack = ostack.copy();
						if(cstack.getItemDamage() == Short.MAX_VALUE)
							cstack.setItemDamage(stack.getItemDamage());

						if(stack.isItemEqual(cstack)) {
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

	boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
		return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
	}

	public List<Object> getInputs() {
		return new ArrayList(inputs);
	}

	public ItemStack getOutput() {
		return output;
	}

}
