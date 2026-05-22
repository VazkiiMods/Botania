package vazkii.botania.api.recipe;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

/**
 * A recipe input type for "processing" type multi-item recipes, which are effectively fancy shapeless crafting recipes
 * that are used by something other than a crafting table.
 */
public interface ProcessingRecipeInput extends RecipeInput {
	StackedContents getStackedContents();
	List<ItemStack> getItems();
}
