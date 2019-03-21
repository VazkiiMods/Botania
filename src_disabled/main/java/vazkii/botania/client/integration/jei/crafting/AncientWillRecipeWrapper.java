package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AncientWillRecipeWrapper implements ICustomCraftingRecipeWrapper {
	private final ResourceLocation name;
	private final List<List<ItemStack>> inputs;
	private final List<ItemStack> output;

	public AncientWillRecipeWrapper(AncientWillRecipe recipe) {
		this.name = recipe.getRegistryName();

		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> helmets = ImmutableList.builder();
		ImmutableList.Builder<ItemStack> wills = ImmutableList.builder();
		helmets.add(new ItemStack(ModItems.terrasteelHelm));
		if(Botania.thaumcraftLoaded) {
			helmets.add(new ItemStack(ModItems.terrasteelHelmRevealing));
		}
		for(int i = 0; i < 6; i++) {
			wills.add(new ItemStack(ModItems.ancientWill, 1, i));
		}

		output = helmets.build();
		builder.add(output);
		builder.add(wills.build());
		inputs = builder.build();
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(output));
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IIngredients ingredients) {
		IFocus<?> focus = recipeLayout.getFocus();
		IGuiItemStackGroup group = recipeLayout.getItemStacks();
		group.set(ingredients);

		if(focus != null) {
			ItemStack focused = (ItemStack) focus.getValue();

			if(focus.getMode() == IFocus.Mode.INPUT && focused.getItem() == ModItems.ancientWill) {
				group.set(2, new ItemStack(ModItems.ancientWill, 1, focused.getMetadata()));
				group.set(0, getHelmetsWithWill(focused.getMetadata()));
			} else if(focused.getItem() instanceof IAncientWillContainer) { //helmet
				group.set(1, new ItemStack(focused.getItem()));
				group.set(0, getWillsOnHelmet(focused.getItem()));
			}
		}
	}

	private List<ItemStack> getHelmetsWithWill(int meta) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
		for(ItemStack itemStack : output) {
			ItemStack toAdd = itemStack.copy();
			((IAncientWillContainer) toAdd.getItem()).addAncientWill(toAdd, meta);
			builder.add(toAdd);
		}
		return builder.build();
	}

	private List<ItemStack> getWillsOnHelmet(Item item) {
		if(item instanceof IAncientWillContainer) {
			ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
			for(int i = 0; i < 6; i++) {
				ItemStack stack = new ItemStack(item);
				((IAncientWillContainer) item).addAncientWill(stack, i);
				builder.add(stack);
			}
			return builder.build();
		}
		return ImmutableList.of();
	}
}
