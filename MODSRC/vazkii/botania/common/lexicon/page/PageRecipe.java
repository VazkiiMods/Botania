/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 8, 2014, 2:46:36 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.client.core.helper.RenderHelper;

public class PageRecipe extends LexiconPage {

	int relativeMouseX, relativeMouseY;
	ItemStack tooltipStack, tooltipContainerStack;
	
	public PageRecipe(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		relativeMouseX = mx;
		relativeMouseY = my;

		renderRecipe(gui, mx, my);
		
		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 40;
		PageText.renderText(x, y, width, height, getUnlocalizedName());
		
		if(tooltipStack != null) {
			List<String> tooltipData = tooltipStack.getTooltip(Minecraft.getMinecraft().thePlayer, false);

			RenderHelper.renderTooltip(mx, my, tooltipData);
			if(tooltipContainerStack != null)
				RenderHelper.renderTooltipGreen(mx, my + 8 + tooltipData.size() * 11, Arrays.asList(EnumChatFormatting.AQUA + StatCollector.translateToLocal("botaniamisc.craftingContainer"), tooltipContainerStack.getDisplayName()));
		}
		tooltipStack = tooltipContainerStack = null;
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		// NO-OP
	}
	
	public void renderItemAtAngle(IGuiLexiconEntry gui, int angle, ItemStack stack) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		angle -= 90;
		int xPos = gui.getLeft() + (int) (Math.cos(angle) * 12) + gui.getWidth() / 2;
		int yPos = gui.getTop() + (int) (Math.sin(angle) * 12) + 25;
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, false);
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

		// Translations required so the glint doesn't merge with the book texture
		GL11.glTranslatef(0F, 0F, 200F);
		if(!ForgeHooksClient.renderInventoryItem(new RenderBlocks(), renderEngine, stack, render.renderWithColor, gui.getZLevel(), xPos, yPos))
			render.renderItemIntoGUI(fontRenderer, renderEngine, stack, xPos, yPos);
		render.renderItemOverlayIntoGUI(fontRenderer, renderEngine, stack, xPos, yPos);
		GL11.glTranslatef(0F, 0F, -200F);
		
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
