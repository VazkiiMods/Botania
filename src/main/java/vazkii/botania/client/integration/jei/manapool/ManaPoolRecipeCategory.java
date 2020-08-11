/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.manapool;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ManaPoolRecipeCategory implements IRecipeCategory<ManaPoolRecipeWrapper> {

	public static final String UID = "botania.manaPool";
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;
	private final ItemStack renderStack = new ItemStack(ModBlocks.pool);

	public ManaPoolRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(142, 55);
		localizedName = I18n.format("botania.nei.manaPool");
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/pureDaisyOverlay.png"),
				0, 0, 64, 46);
		ItemNBTHelper.setBoolean(renderStack, "RenderFull", true);
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

	@Nullable
	@Override
	public IDrawable getIcon() {
		return null;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft, 40, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ManaPoolRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		int index = 0;

		recipeLayout.getItemStacks().init(index, true, 32, 12);
		recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		index++;

		if(ingredients.getInputs(VanillaTypes.ITEM).size() > 1) {
			// Has catalyst
			recipeLayout.getItemStacks().init(index, true, 12, 12);
			recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(1));
			index++;
		}

		recipeLayout.getItemStacks().init(index, true, 62, 12);
		recipeLayout.getItemStacks().set(index, renderStack);
		index++;

		recipeLayout.getItemStacks().init(index, false, 93, 12);
		recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Nonnull
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return new ArrayList<>();
	}

	@Nonnull
	@Override
	public String getModName() {
		return LibMisc.MOD_NAME;
	}

}
