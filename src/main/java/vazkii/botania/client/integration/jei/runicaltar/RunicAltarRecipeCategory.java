/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.runicaltar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RunicAltarRecipeCategory implements IRecipeCategory<IRuneAltarRecipe> {

	public static final ResourceLocation UID = prefix("runic_altar");
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public RunicAltarRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(114, 104);
		localizedName = I18n.format("botania.nei.runicAltar");
		overlay = guiHelper.createDrawable(prefix("textures/gui/petal_overlay.png"),
				17, 11, 114, 82);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.runeAltar));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IRuneAltarRecipe> getRecipeClass() {
		return IRuneAltarRecipe.class;
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
	public void setIngredients(IRuneAltarRecipe recipe, IIngredients iIngredients) {
		List<List<ItemStack>> list = new ArrayList<>();
		for (Ingredient ingr : recipe.getIngredients()) {
			list.add(Arrays.asList(ingr.getMatchingStacks()));
		}
		iIngredients.setInputLists(VanillaTypes.ITEM, list);
		iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}

	@Override
	public void draw(IRuneAltarRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 0, 4);
		HUDHandler.renderManaBar(ms, 6, 98, 0x0000FF, 0.75F, recipe.getManaUsage(), TilePool.MAX_MANA / 10);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRuneAltarRecipe recipe, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 47, 44);
		recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.runeAltar));

		int index = 1;
		double angleBetweenEach = 360.0 / ingredients.getInputs(VanillaTypes.ITEM).size();
		Vector2f point = new Vector2f(47, 12), center = new Vector2f(47, 44);

		for (List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			recipeLayout.getItemStacks().init(index, true, (int) point.x, (int) point.y);
			recipeLayout.getItemStacks().set(index, o);
			index += 1;
			point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angleBetweenEach);
		}

		recipeLayout.getItemStacks().init(index, false, 86, 11);
		recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		JEIBotaniaPlugin.addDefaultRecipeIdTooltip(recipeLayout.getItemStacks(), index, recipe.getId());
	}

}
