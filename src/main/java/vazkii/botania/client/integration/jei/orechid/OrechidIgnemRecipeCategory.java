/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class OrechidIgnemRecipeCategory implements IRecipeCategory<OrechidIgnemRecipeWrapper> {

	public static final String UID = "botania.orechid_ignem";
	private final IDrawableStatic background;
	private final String localizedName;
	private final IDrawableStatic overlay;

	public OrechidIgnemRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(96, 44);
		localizedName = I18n.format("botania.nei.orechidIgnem");
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull OrechidIgnemRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 9, 12);
		itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		itemStacks.init(1, true, 39, 12);
		itemStacks.set(1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID_IGNEM));

		itemStacks.init(2, true, 68, 12);
		itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft, 17, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

}
