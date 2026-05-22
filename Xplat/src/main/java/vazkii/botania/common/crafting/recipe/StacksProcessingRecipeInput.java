package vazkii.botania.common.crafting.recipe;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.ProcessingRecipeInput;

import java.util.Arrays;

public class StacksProcessingRecipeInput implements ProcessingRecipeInput {
	private final ItemStack[] stacks;
	private final StackedContents stackedContents = new StackedContents();

	public StacksProcessingRecipeInput(ItemStack[] stacks) {
		this.stacks = stacks;
		Arrays.stream(stacks).forEach(stackedContents::accountStack);
	}

	@Override
	public ItemStack getItem(int index) {
		return stacks[index];
	}

	@Override
	public int size() {
		return stacks.length;
	}

	@Override
	public StackedContents getStackedContents() {
		return stackedContents;
	}
}
