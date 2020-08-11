/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.puredaisy;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PureDaisyRecipeCategory implements IRecipeCategory<PureDaisyRecipeWrapper> {

	public static final String UID = "botania.pureDaisy";
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;

	public PureDaisyRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(96, 44);
		localizedName = I18n.format("botania.nei.pureDaisy");
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/pureDaisyOverlay.png"),
				0, 0, 64, 44);
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
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft, 17, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull PureDaisyRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		boolean inputFluid = !ingredients.getInputs(VanillaTypes.FLUID).isEmpty();
		boolean outputFluid = !ingredients.getOutputs(VanillaTypes.FLUID).isEmpty();

		if(inputFluid) {
			recipeLayout.getFluidStacks().init(0, true, 9, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(0, true, 9, 12);
			recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		}

		recipeLayout.getItemStacks().init(1, true, 39, 12);
		recipeLayout.getItemStacks().set(1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY));

		if(outputFluid) {
			recipeLayout.getFluidStacks().init(2, false, 68, 12, 16, 16, 1000, false, null);
			recipeLayout.getFluidStacks().set(2, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
		} else {
			recipeLayout.getItemStacks().init(2, false, 68, 12);
			recipeLayout.getItemStacks().set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		}
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

}
