package vazkii.botania.api.recipe;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Arrays;

public class BotaniaRecipeInput implements RecipeInput {
	private final ItemStack[] stacks;
	private final StackedContents stackedContents = new StackedContents();

	public BotaniaRecipeInput(ItemStack[] stacks) {
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

	public StackedContents getStackedContents() {
		return stackedContents;
	}
}
