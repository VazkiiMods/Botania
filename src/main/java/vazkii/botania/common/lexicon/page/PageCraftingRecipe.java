/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 4:58:19 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PageCraftingRecipe extends PageRecipe {

	private static final ResourceLocation craftingOverlay = new ResourceLocation(LibResources.GUI_CRAFTING_OVERLAY);

	final List<ResourceLocation> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

	boolean oreDictRecipe, shapelessRecipe;

	public PageCraftingRecipe(String unlocalizedName, List<ResourceLocation> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
	}

	public PageCraftingRecipe(String unlocalizedName, ResourceLocation recipe) {
		this(unlocalizedName, Collections.singletonList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(ResourceLocation name : recipes) {
			IRecipe recipe = ForgeRegistries.RECIPES.getValue(name);
			if(recipe != null && !recipe.getRecipeOutput().isEmpty()) {
				LexiconRecipeMappings.map(recipe.getRecipeOutput(), entry, index);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		oreDictRecipe = shapelessRecipe = false;

		IRecipe recipe = ForgeRegistries.RECIPES.getValue(recipes.get(recipeAt));
		renderCraftingRecipe(gui, recipe);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(craftingOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		int iconX = gui.getLeft() + 115;
		int iconY = gui.getTop() + 12;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(shapelessRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 0, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("botaniamisc.shapeless")));

			iconY += 20;
		}

		render.bindTexture(craftingOverlay);
		GlStateManager.enableBlend();

		if(oreDictRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 16, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("botaniamisc.oredict")));
		}
		GlStateManager.disableBlend();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(GuiScreen.isShiftKeyDown())
			return;

		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

	@SideOnly(Side.CLIENT)
	public void renderCraftingRecipe(IGuiLexiconEntry gui, IRecipe recipe) {
		if(recipe instanceof ShapedRecipes) {
			ShapedRecipes shaped = (ShapedRecipes)recipe;

			for(int y = 0; y < shaped.recipeHeight; y++)
				for(int x = 0; x < shaped.recipeWidth; x++)
					renderItemAtGridPos(gui, 1 + x, 1 + y, shaped.recipeItems.get(y * shaped.recipeWidth + x).getMatchingStacks()[0], true);
		} else if(recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
			int width = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 4);
			int height = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 5);

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++) {
					Ingredient input = shaped.getIngredients().get(y * width + x);
					if(input != Ingredient.EMPTY)
						renderItemAtGridPos(gui, 1 + x, 1 + y, input.getMatchingStacks()[0], true);
				}

			oreDictRecipe = true;
		} else if(recipe instanceof ShapelessRecipes) {
			ShapelessRecipes shapeless = (ShapelessRecipes) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= shapeless.recipeItems.size())
							break drawGrid;

						renderItemAtGridPos(gui, 1 + x, 1 + y, shapeless.recipeItems.get(index).getMatchingStacks()[0], true);
					}
			}

			shapelessRecipe = true;
		} else if(recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						/*if(index >= shapeless.getRecipeSize()) todo 1.12
							break drawGrid;*/

						Ingredient input = shapeless.getIngredients().get(index);
						if(input != Ingredient.EMPTY)
							renderItemAtGridPos(gui, 1 + x, 1 + y, input.getMatchingStacks()[0], true);
					}
			}

			shapelessRecipe = true;
			oreDictRecipe = true;
		}

		renderItemAtGridPos(gui, 2, 0, recipe.getRecipeOutput(), false);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		return recipes.stream()
				.map(ForgeRegistries.RECIPES::getValue)
				.map(IRecipe::getRecipeOutput)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
	}
}
