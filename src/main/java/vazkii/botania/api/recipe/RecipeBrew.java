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
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;

import java.util.ArrayList;
import java.util.List;

public class RecipeBrew {

	private final Brew brew;
	private final ImmutableList<Ingredient> inputs;

	public RecipeBrew(Brew brew, Ingredient... inputs) {
		this.brew = brew;
		this.inputs = ImmutableList.copyOf(inputs);
	}

	public boolean matches(IItemHandler inv) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty())
				break;

			if(stack.getItem() instanceof IBrewContainer)
				continue;

			int stackIndex = -1;

			for(int j = 0; j < inputsMissing.size(); j++) {
				Ingredient input = inputsMissing.get(j);
				if(input.test(stack)) {
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				inputsMissing.remove(stackIndex);
			else return false;
		}

		return inputsMissing.isEmpty();
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
