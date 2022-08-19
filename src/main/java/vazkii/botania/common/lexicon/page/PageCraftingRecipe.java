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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageCraftingRecipe extends PageRecipe {

	private static final ResourceLocation craftingOverlay = new ResourceLocation(LibResources.GUI_CRAFTING_OVERLAY);

	List<IRecipe> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

	boolean oreDictRecipe, shapelessRecipe;

	public PageCraftingRecipe(String unlocalizedName, List<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes = filterRecipes(recipes);
	}

	public PageCraftingRecipe(String unlocalizedName, IRecipe recipe) {
		this(unlocalizedName, Arrays.asList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(IRecipe recipe : recipes)
			if (recipe != null)
				LexiconRecipeMappings.map(recipe.getRecipeOutput(), entry, index);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		oreDictRecipe = shapelessRecipe = false;

		if (recipes.size() == 0) return;
		IRecipe recipe = recipes.get(recipeAt);

		renderCraftingRecipe(gui, recipe);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(craftingOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		int iconX = gui.getLeft() + 115;
		int iconY = gui.getTop() + 12;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(shapelessRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 0, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Arrays.asList(StatCollector.translateToLocal("botaniamisc.shapeless")));

			iconY += 20;
		}

		render.bindTexture(craftingOverlay);
		GL11.glEnable(GL11.GL_BLEND);

		if(oreDictRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 16, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Arrays.asList(StatCollector.translateToLocal("botaniamisc.oredict")));
		}
		GL11.glDisable(GL11.GL_BLEND);
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
					renderItemAtGridPos(gui, 1 + x, 1 + y, shaped.recipeItems[y * shaped.recipeWidth + x], true);
		} else if(recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
			int width = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 4);
			int height = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 5);

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++) {
					Object input = shaped.getInput()[y * width + x];
					if(input != null)
						renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((ArrayList<ItemStack>) input).get(0), true);
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

						renderItemAtGridPos(gui, 1 + x, 1 + y, (ItemStack) shapeless.recipeItems.get(index), true);
					}
			}

			shapelessRecipe = true;
		} else if(recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= shapeless.getRecipeSize())
							break drawGrid;

						Object input = shapeless.getInput().get(index);
						if(input != null)
							renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((ArrayList<ItemStack>) input).get(0), true);
					}
			}

			shapelessRecipe = true;
			oreDictRecipe = true;
		}

		renderItemAtGridPos(gui, 2, 0, recipe.getRecipeOutput(), false);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList();
		for(IRecipe r : recipes)
			list.add(r.getRecipeOutput());

		return list;
	}
}
