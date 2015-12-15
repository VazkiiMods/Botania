/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 4, 2014, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageShedding extends PageEntity {

	private static final ResourceLocation sheddingOverlay = new ResourceLocation(LibResources.GUI_SHEDDING_OVERLAY);

	ItemStack shedStack;
	ItemStack tooltipStack;
	boolean tooltipEntry;

	static boolean mouseDownLastTick = false;

	public PageShedding(String unlocalizedName, String entity, int size, ItemStack shedStack) {
		super(unlocalizedName, entity, size);
		this.shedStack = shedStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		prepDummy();
		relativeMouseX = mx;
		relativeMouseY = my;
		int stack_x = gui.getLeft() + gui.getWidth() / 2 - 8;
		int stack_y = gui.getTop() + gui.getHeight() - 40 - 18 - 5;
		int entity_scale = getEntityScale(size);
		int entity_x = gui.getLeft() + gui.getWidth() / 2;
		int entity_y = gui.getTop() + gui.getHeight() / 2 + MathHelper.floor_float(dummyEntity.height * entity_scale / 2) - 29;

		renderEntity(gui, dummyEntity, entity_x, entity_y, entity_scale, dummyEntity.ticksExisted * 2);

		renderItem(gui, stack_x, stack_y, shedStack);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(sheddingOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

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
		}
		else if(tooltipEntity) {
			List<String> parsedTooltip = new ArrayList();
			parsedTooltip.add(EntityList.getEntityString(dummyEntity));
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, parsedTooltip);
		}

		tooltipStack = null;
		tooltipEntry = tooltipEntity = false;
		GL11.glDisable(GL11.GL_BLEND);
		mouseDownLastTick = Mouse.isButtonDown(0);
	}

	@SideOnly(Side.CLIENT)
	public void renderItem(IGuiLexiconEntry gui, int xPos, int yPos, ItemStack stack) {
		RenderItem render = new RenderItem();
		boolean mouseDown = Mouse.isButtonDown(0);

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		render.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, xPos, yPos);
		render.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, xPos, yPos);
		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();

		if(relativeMouseX >= xPos && relativeMouseY >= yPos && relativeMouseX <= xPos + 16 && relativeMouseY <= yPos + 16) {
			tooltipStack = stack;

			EntryData data = LexiconRecipeMappings.getDataForStack(tooltipStack);
			if(data != null && (data.entry != gui.getEntry() || data.page != gui.getPageOn())) {
				tooltipEntry = true;

				if(!mouseDownLastTick && mouseDown && GuiScreen.isShiftKeyDown()) {
					GuiLexiconEntry newGui = new GuiLexiconEntry(data.entry, (GuiScreen) gui);
					newGui.page = data.page;
					Minecraft.getMinecraft().displayGuiScreen(newGui);
				}
			}
		}

		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList();
		list.add(shedStack);
		return list;
	}

}
