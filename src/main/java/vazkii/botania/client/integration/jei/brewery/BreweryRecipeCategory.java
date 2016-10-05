/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.brewery;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		if(!(recipeWrapper instanceof BreweryRecipeWrapper))
			return;
		BreweryRecipeWrapper wrapper = (BreweryRecipeWrapper) recipeWrapper;

		List inputs = wrapper.getInputs();

		recipeLayout.getItemStacks().init(0, true, 39, 41);
		if(inputs.get(0) instanceof ItemStack) {
			recipeLayout.getItemStacks().set(0, (ItemStack) inputs.get(0));
		} else {
			recipeLayout.getItemStacks().set(0, (Collection<ItemStack>) inputs.get(0));
		}

		int index = 1, posX = 60;
		for(int i = 1; i < wrapper.getInputs().size(); i++) {
			Object o = wrapper.getInputs().get(i);
			recipeLayout.getItemStacks().init(index, true, posX, 6);
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, (ItemStack) o);
			} else if(o instanceof Collection) {
				recipeLayout.getItemStacks().set(index, (Collection<ItemStack>) o);
			}
			index++;
			posX += 18;
		}

		recipeLayout.getItemStacks().init(7, false, 87, 41);
		recipeLayout.getItemStacks().set(7, wrapper.getOutputs());
	}

}
