/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.petalapothecary;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PetalApothecaryRecipeCategory implements IRecipeCategory<IPetalRecipe> {

	public static final ResourceLocation UID = prefix("petals");
	private final IDrawableStatic background;
	private final String localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;

	public PetalApothecaryRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(114, 97);
		localizedName = I18n.format("botania.nei.petalApothecary");
		overlay = guiHelper.createDrawable(prefix("textures/gui/petal_overlay.png"),
				17, 11, 114, 82);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.defaultAltar));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IPetalRecipe> getRecipeClass() {
		return IPetalRecipe.class;
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
	public void setIngredients(IPetalRecipe recipe, IIngredients iIngredients) {
		List<List<ItemStack>> list = new ArrayList<>();
		for (Ingredient ingr : recipe.getIngredients()) {
			list.add(Arrays.asList(ingr.getMatchingStacks()));
		}
		iIngredients.setInputLists(VanillaTypes.ITEM, list);
		iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}

	@Override
	public void draw(IPetalRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(ms, 0, 4);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IPetalRecipe recipe, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 47, 44);
		recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.defaultAltar));

		int index = 1;
		double angleBetweenEach = 360.0 / ingredients.getInputs(VanillaTypes.ITEM).size();
		Vector2f point = new Vector2f(47, 12), center = new Vector2f(47, 44);

		for (List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			recipeLayout.getItemStacks().init(index, true, (int) point.x, (int) point.y);
			recipeLayout.getItemStacks().set(index, o);
			index += 1;
			point = rotatePointAbout(point, center, angleBetweenEach);
		}

		recipeLayout.getItemStacks().init(index, false, 86, 11);
		recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	public static Vector2f rotatePointAbout(Vector2f in, Vector2f about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Vector2f((float) newX, (float) newY);
	}
}
