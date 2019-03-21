package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeLensRecipeWrapper implements ICustomCraftingRecipeWrapper {
	private final List<List<ItemStack>> inputs;
	private static final List<ItemStack> lenses;

	static {
		NonNullList<ItemStack> allLenses = NonNullList.create();
		ItemLens lens = (ItemLens) ModItems.lens;
		lens.getSubItems(CreativeTabs.SEARCH, allLenses);
		lenses = ImmutableList.copyOf(allLenses.stream().filter(stack -> !lens.isControlLens(stack)).filter(lens::isCombinable).collect(Collectors.toList()));
	}

	public CompositeLensRecipeWrapper() {
		inputs = ImmutableList.of(lenses, ImmutableList.of(new ItemStack(Items.SLIME_BALL)), lenses);
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().set(ingredients);
		recipeLayout.setShapeless();
		if(recipeLayout.getFocus() != null) {
			ItemStack stack = (ItemStack) recipeLayout.getFocus().getValue();
			if(stack.getItem() == ModItems.lens) {
				setLenses(recipeLayout, stack.getMetadata(), stack.getMetadata());
				return;
			}
		}
		setLenses(recipeLayout, 1, ItemLens.SUBTYPES - 1);
	}

	private void setLenses(IRecipeLayout recipeLayout, int start, int end) {
		List<ItemStack> firstInput = new ArrayList<>();
		List<ItemStack> secondInput = new ArrayList<>();
		List<ItemStack> outputs = new ArrayList<>();

		if(end >= ItemLens.SUBTYPES)
			end = ItemLens.SUBTYPES - 1;

		for(int i = start; i <= end; i++) {
			ItemStack firstLens = new ItemStack(ModItems.lens, 1, i);
			for(ItemStack secondLens : lenses) {
				if(secondLens.getMetadata() == i)
					continue;

				if(((ItemLens) ModItems.lens).canCombineLenses(firstLens, secondLens)) {
					firstInput.add(firstLens);
					secondInput.add(secondLens);
					outputs.add(((ItemLens) ModItems.lens).setCompositeLens(firstLens.copy(), secondLens));
				}
			}

		}
		recipeLayout.getItemStacks().set(1, firstInput);
		recipeLayout.getItemStacks().set(3, secondInput);
		recipeLayout.getItemStacks().set(0, outputs);
	}

}
