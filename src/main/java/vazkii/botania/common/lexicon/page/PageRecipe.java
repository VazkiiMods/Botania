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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.core.helper.PlayerHelper;

import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageRecipe extends LexiconPage {

	int relativeMouseX, relativeMouseY;
	ItemStack tooltipStack = ItemStack.EMPTY, tooltipContainerStack = ItemStack.EMPTY;
	boolean tooltipEntry;

	static boolean mouseDownLastTick = false;

	public PageRecipe(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		relativeMouseX = mx;
		relativeMouseY = my;

		renderRecipe(gui, mx, my);

		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 40;
		PageText.renderText(x, y, width, height, getUnlocalizedName());

		if(!tooltipStack.isEmpty()) {
			List<ITextComponent> tooltipData = tooltipStack.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
			List<String> parsedTooltip = new ArrayList<>();
			boolean first = true;

			for(ITextComponent s : tooltipData) {
				String s_ = s.getFormattedText();
				if(!first) {
					s_ = s.applyTextStyle(TextFormatting.GRAY).getFormattedText();
				}
				parsedTooltip.add(s_);
				first = false;
			}

			FontRenderer font = Minecraft.getInstance().fontRenderer;
			int tooltipHeight = tooltipData.size() * 10 + 2;
			int tooltipWidth = parsedTooltip.stream().map(font::getStringWidth).max(Comparator.comparingInt(a -> a)).orElse(0);
			int rmx = mx + 12;
			int rmy = my - 12;
			
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, parsedTooltip);
			GlStateManager.disableDepthTest();
			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(tooltipStack, parsedTooltip, rmx, rmy, font, tooltipWidth, tooltipHeight));
			MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(tooltipStack, parsedTooltip, rmx, rmy, font, tooltipWidth, tooltipHeight));
			GlStateManager.enableDepthTest();
			
			int tooltipY = 8 + tooltipData.size() * 11;

			if(tooltipEntry) {
				vazkii.botania.client.core.helper.RenderHelper.renderTooltipOrange(mx, my + tooltipY, Collections.singletonList(TextFormatting.GRAY + I18n.format("botaniamisc.clickToRecipe")));
				tooltipY += 18;
			}

			if(!tooltipContainerStack.isEmpty())
				vazkii.botania.client.core.helper.RenderHelper.renderTooltipGreen(mx, my + tooltipY, Arrays.asList(TextFormatting.AQUA + I18n.format("botaniamisc.craftingContainer"), tooltipContainerStack.getDisplayName().getFormattedText()));
		}

		tooltipStack = tooltipContainerStack = ItemStack.EMPTY;
		tooltipEntry = false;
		GlStateManager.disableBlend();
		mouseDownLastTick = GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {}

	@OnlyIn(Dist.CLIENT)
	public void renderItemAtAngle(IGuiLexiconEntry gui, float angle, ItemStack stack) {
		if(stack.isEmpty())
			return;

		angle -= 90;
		int radius = 32;
		double xPos = gui.getLeft() + Math.cos(angle * Math.PI / 180D) * radius + gui.getWidth() / 2 - 8;
		double yPos = gui.getTop() + Math.sin(angle * Math.PI / 180D) * radius + 53;

		renderItem(gui, xPos, yPos, stack, false);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItemAtGridPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack, boolean accountForContainer) {
		if(stack.isEmpty())
			return;

		int xPos = gui.getLeft() + x * 29 + 7 + (y == 0  && x == 3 ? 10 : 0);
		int yPos = gui.getTop() + y * 29 + 24 - (y == 0 ? 7 : 0);

		renderItem(gui, xPos, yPos, stack, accountForContainer);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItem(IGuiLexiconEntry gui, double xPos, double yPos, ItemStack stack, boolean accountForContainer) {
		ItemRenderer render = Minecraft.getInstance().getItemRenderer();
		boolean mouseDown = GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepthTest();
		GlStateManager.pushMatrix();
		GlStateManager.translated(xPos, yPos, 0);
		render.renderItemAndEffectIntoGUI(stack, 0, 0);
		render.renderItemOverlays(Minecraft.getInstance().fontRenderer, stack, 0, 0);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();

		int xpi = (int) xPos;
		int ypi = (int) yPos;
		if(relativeMouseX >= xpi && relativeMouseY >= ypi && relativeMouseX <= xpi + 16 && relativeMouseY <= ypi + 16) {
			tooltipStack = stack;

			EntryData data = LexiconRecipeMappings.getDataForStack(tooltipStack);
			ItemStack book = PlayerHelper.getFirstHeldItemClass(Minecraft.getInstance().player, ILexicon.class);

			if(data != null && (data.entry != gui.getEntry() || data.page != gui.getPageOn()) && book != null && ((ILexicon) book.getItem()).isKnowledgeUnlocked(book, data.entry.getKnowledgeType())) {
				tooltipEntry = true;

				if(!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown()) {
					GuiLexiconEntry newGui = new GuiLexiconEntry(data.entry, (GuiScreen) gui);
					newGui.page = data.page;
					Minecraft.getInstance().displayGuiScreen(newGui);
				}
			} else tooltipEntry = false;

			if(accountForContainer) {
				ItemStack containerStack = stack.getItem().getContainerItem(stack);
				if(!containerStack.isEmpty())
					tooltipContainerStack = containerStack;
			}
		}

		GlStateManager.disableLighting();
	}

}
