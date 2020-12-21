/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 8, 2014, 2:46:36 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageRecipe extends LexiconPage {

	int relativeMouseX, relativeMouseY;
	ItemStack tooltipStack, tooltipContainerStack;
	boolean tooltipEntry;

	static boolean mouseDownLastTick = false;

	public PageRecipe(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	@SideOnly(Side.CLIENT)
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
			List<String> parsedTooltip = new ArrayList();
			boolean first = true;

			for(String s : tooltipData) {
				String s_ = s;
				if(!first)
					s_ = EnumChatFormatting.GRAY + s;
				parsedTooltip.add(s_);
				first = false;
			}

			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, parsedTooltip);

			int tooltipY = 8 + tooltipData.size() * 11;

			if(tooltipEntry) {
				vazkii.botania.client.core.helper.RenderHelper.renderTooltipOrange(mx, my + tooltipY, Arrays.asList(EnumChatFormatting.GRAY + StatCollector.translateToLocal("botaniamisc.clickToRecipe")));
				tooltipY += 18;
			}

			if(tooltipContainerStack != null)
				vazkii.botania.client.core.helper.RenderHelper.renderTooltipGreen(mx, my + tooltipY, Arrays.asList(EnumChatFormatting.AQUA + StatCollector.translateToLocal("botaniamisc.craftingContainer"), tooltipContainerStack.getDisplayName()));
		}

		tooltipStack = tooltipContainerStack = null;
		tooltipEntry = false;
		GL11.glDisable(GL11.GL_BLEND);
		mouseDownLastTick = Mouse.isButtonDown(0);
	}

	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtAngle(IGuiLexiconEntry gui, float angle, ItemStack stack) {
		if(stack == null || stack.getItem() == null)
			return;

		ItemStack workStack = stack.copy();

		if(workStack.getItemDamage() == Short.MAX_VALUE || workStack.getItemDamage() == -1)
			workStack.setItemDamage(0);

		angle -= 90;
		int radius = 32;
		double xPos = gui.getLeft() + Math.cos(angle * Math.PI / 180D) * radius + gui.getWidth() / 2 - 8;
		double yPos = gui.getTop() + Math.sin(angle * Math.PI / 180D) * radius + 53;

		renderItem(gui, xPos, yPos, workStack, false);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtGridPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack, boolean accountForContainer) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 29 + 7 + (y == 0  && x == 3 ? 10 : 0);
		int yPos = gui.getTop() + y * 29 + 24 - (y == 0 ? 7 : 0);
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, accountForContainer);
	}

	@SideOnly(Side.CLIENT)
	public void renderItem(IGuiLexiconEntry gui, double xPos, double yPos, ItemStack stack, boolean accountForContainer) {
		RenderItem render = new RenderItem();
		boolean mouseDown = Mouse.isButtonDown(0);

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPushMatrix();
		GL11.glTranslated(xPos, yPos, 0);
		render.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, 0, 0);
		render.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, 0, 0);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();

		int xpi = (int) xPos;
		int ypi = (int) yPos;
		if(relativeMouseX >= xpi && relativeMouseY >= ypi && relativeMouseX <= xpi + 16 && relativeMouseY <= ypi + 16) {
			tooltipStack = stack;

			EntryData data = LexiconRecipeMappings.getDataForStack(tooltipStack);
			ItemStack book = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();

			if(data != null && (data.entry != gui.getEntry() || data.page != gui.getPageOn()) && book != null && book.getItem() instanceof ILexicon && ((ILexicon) book.getItem()).isKnowledgeUnlocked(book, data.entry.getKnowledgeType())) {
				tooltipEntry = true;

				if(!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown()) {
					GuiLexiconEntry newGui = new GuiLexiconEntry(data.entry, (GuiScreen) gui);
					newGui.page = data.page;
					Minecraft.getMinecraft().displayGuiScreen(newGui);
				}
			} else tooltipEntry = false;

			if(accountForContainer) {
				ItemStack containerStack = stack.getItem().getContainerItem(stack);
				if(containerStack != null && containerStack.getItem() != null)
					tooltipContainerStack = containerStack;
			}
		}

		GL11.glDisable(GL11.GL_LIGHTING);
	}

	public static <T> List<T> filterRecipes(List<T> list) {
		if (list == null) return new ArrayList<T>();
		ArrayList<T> filtered = new ArrayList<T>();
		for (T entry: list) {
			if (entry != null) filtered.add(entry);
		}
		return filtered;
	}

}
