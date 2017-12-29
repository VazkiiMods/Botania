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
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BreweryRecipeCategory implements IRecipeCategory {

	public static final String UID = "botania.brewery";
	private final IDrawableStatic background;
	private final String localizedName;

	public BreweryRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("botania", "textures/gui/neiBrewery.png");
		background = guiHelper.createDrawable(location, 0, 0, 166, 65, 0, 0, 0, 0);
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		if(!(recipeWrapper instanceof BreweryRecipeWrapper))
			return;

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);

		recipeLayout.getItemStacks().init(0, true, 39, 41);
		recipeLayout.getItemStacks().set(0, inputs.get(0));

		int index = 1, posX = 60;
		for(int i = 1; i < inputs.size(); i++) {
			List<ItemStack> o = inputs.get(i);
			recipeLayout.getItemStacks().init(index, true, posX, 6);
			recipeLayout.getItemStacks().set(index, o);
			index++;
			posX += 18;
		}

		recipeLayout.getItemStacks().init(7, false, 87, 41);
		recipeLayout.getItemStacks().set(7, ingredients.getOutputs(ItemStack.class).get(0));
	}
}
