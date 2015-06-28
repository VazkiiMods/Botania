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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
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
		this.mb = set.getForIndex(0);
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
		GL11.glTranslatef(gui.getLeft() + gui.getWidth() / 2, gui.getTop() + 90, gui.getZLevel() + 500F);

		float diag = (float) Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
		float height = mb.getYSize();
		float scaleX = maxX / diag;
		float scaleY = maxY / height;
		float scale = -Math.min(scaleY, scaleX);
		GL11.glScalef(scale, scale, scale);

		GL11.glRotatef(-20F, 1, 0, 0);
		GL11.glRotatef(gui.getElapsedTicks(), 0, 1, 0);
		
		for(MultiblockComponent comp : mb.getComponents()) {
			ChunkCoordinates pos = comp.getRelativePosition();
			renderBlock(comp.getBlock(), comp.getMeta(), pos.posX + mb.offX, pos.posY + mb.offY, pos.posZ + mb.offZ);
		}
		GL11.glPopMatrix();
		
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		String s = EnumChatFormatting.BOLD + StatCollector.translateToLocal(getUnlocalizedName());
		font.setUnicodeFlag(true);
		font.drawString(s, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.getTop() + 16, 0x000000);
		font.setUnicodeFlag(unicode);
	}

	public void renderBlock(Block block, int meta, int x, int y, int z) {
		if(block != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, z);
			RenderBlocks.getInstance().renderBlockAsItem(block, meta, 0.8F);
			GL11.glPopMatrix();
		}
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
