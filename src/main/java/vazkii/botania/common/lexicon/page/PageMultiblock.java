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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.lib.LibResources;

import java.util.ArrayList;
import java.util.List;

public class PageMultiblock extends LexiconPage {

	private static final ResourceLocation multiblockOverlay = new ResourceLocation(LibResources.GUI_MULTIBLOCK_OVERLAY);

	GuiButton button;
	final MultiblockSet set;
	final Multiblock mb;
	int ticksElapsed;

	public PageMultiblock(String unlocalizedName, MultiblockSet set) {
		super(unlocalizedName);
		mb = set.getForFacing(EnumFacing.SOUTH);
		this.set = set;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(multiblockOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlpha();
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();

		final float maxX = 90, maxY = 60;
		GlStateManager.pushMatrix();
		GlStateManager.translate(gui.getLeft() + gui.getWidth() / 2, gui.getTop() + 90, gui.getZLevel() + 100F);

		float diag = (float) Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
		float height = mb.getYSize();
		float scaleX = maxX / diag;
		float scaleY = maxY / height;
		float scale = -Math.min(scaleY, scaleX);
		GlStateManager.scale(scale, scale, scale);

		GlStateManager.rotate(-20F, 1, 0, 0);
		GlStateManager.rotate(gui.getElapsedTicks(), 0, 1, 0);

		MultiblockRenderHandler.renderMultiblockOnPage(mb);

		GlStateManager.popMatrix();

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		String s = TextFormatting.BOLD + I18n.format(getUnlocalizedName());
		font.setUnicodeFlag(true);
		font.drawString(s, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.getTop() + 16, 0x000000);
		font.setUnicodeFlag(unicode);

		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		int x = gui.getLeft() + 15;
		int y = gui.getTop() + 25;
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.STONEBRICK), x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 0F, 200F);
		if(mx >= x && mx < x + 16 && my >= y && my < y + 16) {
			List<String> mats = new ArrayList<>();
			mats.add(I18n.format("botaniamisc.materialsRequired"));
			for(ItemStack stack : mb.materials) {
				String size = "" + stack.getCount();
				if(size.length() < 2)
					size = "0" + size;
				mats.add(" " + TextFormatting.AQUA + size + " " + TextFormatting.GRAY + stack.getDisplayName());
			}

			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, mats);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void onOpened(IGuiLexiconEntry gui) {
		button = new GuiButton(101, gui.getLeft() + 30, gui.getTop() + gui.getHeight() - 50, gui.getWidth() - 60, 20, getButtonStr());
		gui.getButtonList().add(button);
	}

	@SideOnly(Side.CLIENT)
	private String getButtonStr() {
		return I18n.format(MultiblockRenderHandler.currentMultiblock == set ? "botaniamisc.unvisualize" : "botaniamisc.visualize");
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
