/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class BreweryRecipeCategory implements IRecipeCategory<BreweryRecipeWrapper> {

	public static final String UID = "botania.brewery";
	private final IDrawableStatic background;
	private final String localizedName;

	public BreweryRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("botania", "textures/gui/neiBrewery.png");
		background = guiHelper.createDrawable(location, 28, 6, 131, 55);
		localizedName = I18n.format("botania.nei.brewery");
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull BreweryRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		IFocus<?> focus = recipeLayout.getFocus();

		recipeLayout.getItemStacks().init(0, true, 10, 35);
		recipeLayout.getItemStacks().set(0, getItemMatchingFocus(focus, IFocus.Mode.OUTPUT, outputs.get(0), inputs.get(0)));

		int index = 1, posX = 76 - (inputs.size() * 9);
		for (int i = 1; i < inputs.size(); i++) {
			List<ItemStack> o = inputs.get(i);
			recipeLayout.getItemStacks().init(index, true, posX, 0);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		recipeLayout.getItemStacks().init(7, false, 58, 35);
		recipeLayout.getItemStacks().set(7, getItemMatchingFocus(focus, IFocus.Mode.INPUT, inputs.get(0), outputs.get(0)));
	}

	/**
	 * If an item in this recipe is focused, returns the corresponding item instead of all the containers/results.
	 */
	private List<ItemStack> getItemMatchingFocus(IFocus<?> focus, IFocus.Mode mode, List<ItemStack> focused, List<ItemStack> other) {
		if(focus != null && focus.getMode() == mode) {
			ItemStack focusStack = (ItemStack) focus.getValue();
			for(int i = 0; i < focused.size(); i++)
				if(focusStack.isItemEqual(focused.get(i)))
					return Collections.singletonList(other.get(i));
		}
		return other;
	}
}
