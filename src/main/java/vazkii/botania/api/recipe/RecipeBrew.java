/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 8:52:00 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;

import java.util.ArrayList;
import java.util.List;

public class RecipeBrew {

	private final Brew brew;
	private final ImmutableList<Object> inputs;

	public RecipeBrew(Brew brew, Object... inputs) {
		this.brew = brew;

		ImmutableList.Builder<Object> inputsToSet = ImmutableList.builder();
		for(Object obj : inputs) {
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet.build();
	}

	public boolean matches(IItemHandler inv) {
		List<Object> inputsMissing = new ArrayList<>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty())
				break;

			if(stack.getItem() instanceof IBrewContainer)
				continue;

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
							oredictIndex = j;
							found = true;
							break;
						}
					}


					if(found)
						break;
				} else if(input instanceof ItemStack && simpleAreStacksEqual((ItemStack) input, stack)) {
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				inputsMissing.remove(stackIndex);
			else if(oredictIndex != -1)
				inputsMissing.remove(oredictIndex);
			else return false;
		}

		return inputsMissing.isEmpty();
	}

	private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
		return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
	}

	public List<Object> getInputs() {
		return new ArrayList<>(inputs);
	}

	public Brew getBrew() {
		return brew;
	}

	public int getManaUsage() {
		return brew.getManaCost();
	}

	public ItemStack getOutput(ItemStack stack) {
		if(stack.isEmpty() || !(stack.getItem() instanceof IBrewContainer))
			return new ItemStack(Items.GLASS_BOTTLE); // Fallback...
		IBrewContainer container = (IBrewContainer) stack.getItem();

		return container.getItemForBrew(brew, stack);
	}

	@Override
	public int hashCode() {
		return 31 * brew.hashCode() ^ inputs.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof RecipeBrew
				&& brew == ((RecipeBrew) o).brew
				&& inputs.equals(((RecipeBrew) o).inputs);
	}

}
