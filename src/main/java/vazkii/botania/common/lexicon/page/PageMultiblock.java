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

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

	private Button button;
	private final MultiblockSet set;
	private final Multiblock mb;
	private int ticksElapsed;

	public PageMultiblock(String unlocalizedName, MultiblockSet set) {
		super(unlocalizedName);
		mb = set.getForFacing(Direction.SOUTH);
		this.set = set;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		TextureManager render = Minecraft.getInstance().textureManager;
		render.bindTexture(multiblockOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlphaTest();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		((Screen) gui).blit(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();

		final float maxX = 90, maxY = 60;
		GlStateManager.pushMatrix();
		GlStateManager.translatef(gui.getLeft() + gui.getWidth() / 2, gui.getTop() + 90, gui.getZLevel() + 100F);

		float diag = (float) Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
		float height = mb.getYSize();
		float scaleX = maxX / diag;
		float scaleY = maxY / height;
		float scale = -Math.min(scaleY, scaleX);
		GlStateManager.scalef(scale, scale, scale);

		GlStateManager.rotatef(-20F, 1, 0, 0);
		GlStateManager.rotatef(gui.getElapsedTicks(), 0, 1, 0);

		MultiblockRenderHandler.renderMultiblockOnPage(mb);

		GlStateManager.popMatrix();

		FontRenderer font = Minecraft.getInstance().fontRenderer;
		String s = TextFormatting.BOLD + I18n.format(getUnlocalizedName());
		font.drawString(s, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.getTop() + 16, 0x000000);

		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		int x = gui.getLeft() + 15;
		int y = gui.getTop() + 25;
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(Blocks.STONE_BRICKS), x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();

		GlStateManager.pushMatrix();
		GlStateManager.translatef(0F, 0F, 200F);
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
		button = new Button(gui.getLeft() + 30, gui.getTop() + gui.getHeight() - 50, gui.getWidth() - 60, 20, getButtonStr(), b -> {
			if(MultiblockRenderHandler.currentMultiblock == set)
				MultiblockRenderHandler.setMultiblock(null);
			else MultiblockRenderHandler.setMultiblock(set);
			button.setMessage(getButtonStr()); // todo 1.14 this capture probably wont work
		});
		gui.getButtonList().add(button);
	}

	@OnlyIn(Dist.CLIENT)
	private String getButtonStr() {
		return I18n.format(MultiblockRenderHandler.currentMultiblock == set ? "botaniamisc.unvisualize" : "botaniamisc.visualize");
	}

	@Override
	public void onClosed(IGuiLexiconEntry gui) {
		gui.getButtonList().remove(button);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void updateScreen() {
		++ticksElapsed;
	}

}
