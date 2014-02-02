/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 4:58:19 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.page.LexiconPage;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PageCraftingRecipes extends LexiconPage {

	private static final ResourceLocation craftingOverlay = new ResourceLocation(LibResources.GUI_CRAFTING_OVERLAY);

	List<IRecipe> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

	int relativeMouseX, relativeMouseY;

	boolean oreDictRecipe, shapelessRecipe;

	ItemStack tooltipStack, tooltipContainerStack;

	public PageCraftingRecipes(String unlocalizedName, List<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
	}

	public PageCraftingRecipes(String unlocalizedName, IRecipe recipe) {
		this(unlocalizedName, Arrays.asList(recipe));
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		relativeMouseX = mx;
		relativeMouseY = my;
		oreDictRecipe = shapelessRecipe = false;

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(craftingOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		IRecipe recipe = recipes.get(recipeAt);
		renderCraftingRecipe(gui, recipe);

		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 40;
		PageText.renderText(x, y, width, height, getUnlocalizedName());

		GL11.glColor4f(1F, 1F, 1F, 1F);
		render.bindTexture(craftingOverlay);
		int iconX = gui.getLeft() + 115;
		int iconY = gui.getTop() + 12;

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

		if(tooltipStack != null) {
			List<String> tooltipData = tooltipStack.getTooltip(Minecraft.getMinecraft().thePlayer, false);

			RenderHelper.renderTooltip(mx, my, tooltipData);
			if(tooltipContainerStack != null)
				RenderHelper.renderTooltipGreen(mx, my + 8 + tooltipData.size() * 11, Arrays.asList(EnumChatFormatting.AQUA + StatCollector.translateToLocal("botaniamisc.craftingContainer"), tooltipContainerStack.getDisplayName()));
		}
		tooltipStack = tooltipContainerStack = null;
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void updateScreen() {
		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

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

	public void renderItemAtGridPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack, boolean accountForContainer) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 29 + 7;
		int yPos = gui.getTop() + y * 29 + 24 - (y == 0 ? 7 : 0);
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, accountForContainer);
	}

	public void renderItem(IGuiLexiconEntry gui, int xPos, int yPos, ItemStack stack, boolean accountForContainer) {
		RenderItem render = new RenderItem();
		TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		if(!ForgeHooksClient.renderInventoryItem(new RenderBlocks(), renderEngine, stack, render.renderWithColor, gui.getZLevel(), xPos, yPos))
			render.renderItemIntoGUI(fontRenderer, renderEngine, stack, xPos, yPos);
		render.renderItemOverlayIntoGUI(fontRenderer, renderEngine, stack, xPos, yPos);

		if(relativeMouseX >= xPos && relativeMouseY >= yPos && relativeMouseX <= xPos + 16 && relativeMouseY <= yPos + 16) {
			tooltipStack = stack;
			if(accountForContainer) {
				ItemStack containerStack = stack.getItem().getContainerItemStack(stack);
				if(containerStack != null && containerStack.getItem() != null)
					tooltipContainerStack = containerStack;
			}
		}

		GL11.glDisable(GL11.GL_LIGHTING);
	}

}
