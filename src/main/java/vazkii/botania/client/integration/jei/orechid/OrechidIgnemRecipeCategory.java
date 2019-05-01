/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class OrechidIgnemRecipeCategory implements IRecipeCategory<OrechidIgnemRecipeWrapper> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "orechid_ignem");
	private final IDrawableStatic background;
	private final String localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;

	public OrechidIgnemRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(168, 64);
		localizedName = I18n.format("botania.nei.orechidIgnem");
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 46);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModSubtiles.orechidIgnem));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends OrechidIgnemRecipeWrapper> getRecipeClass() {
		return OrechidIgnemRecipeWrapper.class;
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

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(OrechidIgnemRecipeWrapper recipe, IIngredients ingredients) {

	}


	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull OrechidIgnemRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 40, 12);
		itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		itemStacks.init(1, true, 70, 12);
		itemStacks.set(1, new ItemStack(ModSubtiles.orechidIgnem));

		itemStacks.init(2, true, 99, 12);
		itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void draw(OrechidIgnemRecipeWrapper recipe, double mouseX, double mouseY) {
		GlStateManager.enableAlphaTest();
		GlStateManager.enableBlend();
		overlay.draw(48, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlphaTest();
	}
}
