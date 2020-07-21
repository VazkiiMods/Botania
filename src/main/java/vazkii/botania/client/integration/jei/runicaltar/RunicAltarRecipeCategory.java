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
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.client.integration.jei.mana.ManaIngredient;
import vazkii.botania.client.integration.jei.mana.ManaIngredientRenderer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunicAltarRecipeCategory implements IRecipeCategory<IRuneAltarRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "runic_altar");
	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public RunicAltarRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(150, 110);
		localizedName = I18n.format("botania.nei.runicAltar");
		overlay = guiHelper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/petal_overlay.png"),
				0, 0, 150, 110);
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

		iIngredients.setInput(ManaIngredient.Type.INSTANCE, new ManaIngredient(recipe.getManaUsage(), false));
	}

	@Override
	public void draw(IRuneAltarRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRuneAltarRecipe recipe, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 64, 52);
		recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.runeAltar));

		int index = 1;
		double angleBetweenEach = 360.0 / ingredients.getInputs(VanillaTypes.ITEM).size();
		Point point = new Point(64, 20), center = new Point(64, 52);

		for (List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			recipeLayout.getItemStacks().init(index, true, point.x, point.y);
			recipeLayout.getItemStacks().set(index, o);
			index += 1;
			point = rotatePointAbout(point, center, angleBetweenEach);
		}

		recipeLayout.getItemStacks().init(index, false, 103, 17);
		recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		IGuiIngredientGroup<IManaIngredient> manaIngredients = recipeLayout.getIngredientsGroup(ManaIngredient.Type.INSTANCE);
		manaIngredients.init(0, true,
				ManaIngredientRenderer.Bar.INSTANCE,
				28, 105,
				102, 5,
				0, 0);
		manaIngredients.set(ingredients);

		JEIBotaniaPlugin.addDefaultRecipeIdTooltip(recipeLayout.getItemStacks(), index, recipe.getId());
	}

	private Point rotatePointAbout(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}

}
