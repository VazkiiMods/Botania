/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2015, 1:48:58 AM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageMultiblock extends LexiconPage {

	private static final ResourceLocation multiblockOverlay = new ResourceLocation(LibResources.GUI_MULTIBLOCK_OVERLAY);

	GuiButton button;
	MultiblockSet set;
	Multiblock mb;
	int ticksElapsed;

	public PageMultiblock(String unlocalizedName, MultiblockSet set) {
		super(unlocalizedName);
		mb = set.getForIndex(0);
		this.set = set;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(multiblockOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		final float maxX = 90, maxY = 60;
		GL11.glPushMatrix();
		GL11.glTranslatef(gui.getLeft() + gui.getWidth() / 2, gui.getTop() + 90, gui.getZLevel() + 100F);

		float diag = (float) Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
		float height = mb.getYSize();
		float scaleX = maxX / diag;
		float scaleY = maxY / height;
		float scale = -Math.min(scaleY, scaleX);
		GL11.glScalef(scale, scale, scale);

		GL11.glRotatef(-20F, 1, 0, 0);
		GL11.glRotatef(gui.getElapsedTicks(), 0, 1, 0);

		MultiblockRenderHandler.renderMultiblockOnPage(mb);

		GL11.glPopMatrix();

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		String s = EnumChatFormatting.BOLD + StatCollector.translateToLocal(getUnlocalizedName());
		font.setUnicodeFlag(true);
		font.drawString(s, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.getTop() + 16, 0x000000);
		font.setUnicodeFlag(unicode);

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		int x = gui.getLeft() + 15;
		int y = gui.getTop() + 25;
		RenderItem.getInstance().renderItemIntoGUI(font, render, new ItemStack(Blocks.stonebrick), x, y);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		GL11.glPushMatrix();
		GL11.glTranslatef(0F, 0F, 200F);
		if(mx >= x && mx < x + 16 && my >= y && my < y + 16) {
			List<String> mats = new ArrayList();
			mats.add(StatCollector.translateToLocal("botaniamisc.materialsRequired"));
			for(ItemStack stack : mb.materials) {
				String size = "" + stack.stackSize;
				if(size.length() < 2)
					size = "0" + size;
				mats.add(" " + EnumChatFormatting.AQUA + size + " " + EnumChatFormatting.GRAY + stack.getDisplayName());
			}

			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, mats);
		}
		GL11.glPopMatrix();
	}

	@Override
	public void onOpened(IGuiLexiconEntry gui) {
		button = new GuiButton(101, gui.getLeft() + 30, gui.getTop() + gui.getHeight() - 50, gui.getWidth() - 60, 20, getButtonStr());
		gui.getButtonList().add(button);
	}

	String getButtonStr() {
		return StatCollector.translateToLocal(MultiblockRenderHandler.currentMultiblock == set ? "botaniamisc.unvisualize" : "botaniamisc.visualize");
	}

	@Override
	public void onClosed(IGuiLexiconEntry gui) {
		gui.getButtonList().remove(button);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onActionPerformed(IGuiLexiconEntry gui, GuiButton button) {
		if(button == this.button) {
			if(MultiblockRenderHandler.currentMultiblock == set)
				MultiblockRenderHandler.setMultiblock(null);
			else MultiblockRenderHandler.setMultiblock(set);
			button.displayString = getButtonStr();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		++ticksElapsed;
	}

}
