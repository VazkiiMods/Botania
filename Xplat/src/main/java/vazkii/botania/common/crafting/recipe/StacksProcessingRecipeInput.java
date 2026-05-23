package vazkii.botania.common.crafting.recipe;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.ProcessingRecipeInput;

import java.util.Arrays;
import java.util.List;

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

	@Override
	public List<ItemStack> getItems() {
		return Arrays.asList(stacks);
	}

	@Override
	public ProcessingRecipeInput getSubset(int startSlot, int endSlot) {
		return new SubsetStacksProcessingRecipeInput(this, startSlot, endSlot);
	}

	private static class SubsetStacksProcessingRecipeInput implements ProcessingRecipeInput {

		private final ProcessingRecipeInput input;
		private final int startSlot;
		private final int endSlot;
		private final List<ItemStack> items;
		private final StackedContents stackedContents = new StackedContents();

		public SubsetStacksProcessingRecipeInput(ProcessingRecipeInput input, int startSlot, int endSlot) {
			this.input = input;
			this.startSlot = startSlot;
			this.endSlot = endSlot;
			this.items = input.getItems().subList(startSlot, endSlot);
			this.items.forEach(stackedContents::accountStack);
		}

		@Override
		public StackedContents getStackedContents() {
			return this.stackedContents;
		}

		@Override
		public List<ItemStack> getItems() {
			return this.items;
		}

		@Override
		public ProcessingRecipeInput getSubset(int startSlot, int endSlot) {
			return input.getSubset(this.startSlot + startSlot, this.startSlot + endSlot);
		}

		@Override
		public ItemStack getItem(int index) {
			return items.get(index);
		}

		@Override
		public int size() {
			return endSlot - startSlot;
		}
	}
}
