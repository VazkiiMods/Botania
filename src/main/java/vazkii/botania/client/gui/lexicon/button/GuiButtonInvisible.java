/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 8:34:01 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.FontHelper;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class GuiButtonInvisible extends GuiButtonLexicon {

	private static final ResourceLocation dogResource = new ResourceLocation(LibResources.GUI_DOG);

	final GuiLexiconIndex gui;
	public ItemStack displayStack = ItemStack.EMPTY;
	public boolean dog = false;
	float timeHover = 0;

	boolean enableDog = false;
	double dogPos = 0;

	public GuiButtonInvisible(GuiLexiconIndex gui, int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.gui = gui;
	}

	public void click() {
		enableDog = true;
		PersistentVariableHelper.dog = true;
		PersistentVariableHelper.saveSafe();
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		if(enableDog) {
			dogPos += ClientTickHandler.delta * 10;

			par1Minecraft.renderEngine.bindTexture(dogResource);
			float f = 1F / 64F;
			GlStateManager.translate(dogPos, 0, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
			vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect(0, y, zLevel + 10, dogPos % 100 < 50 ? 23 : 0, 0, 23, 19, f, f);
			x = (int) Math.max(x, dogPos + 10);

			GlStateManager.translate(-dogPos, 0, 0);
		}

		hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
		int k = getHoverState(hovered);
		boolean showStack = !displayStack.isEmpty() && !displayString.isEmpty();

		if(!displayString.isEmpty() && k == 2) {
			timeHover = Math.min(5, timeHover + gui.timeDelta);
			gui.setHoveredButton(this);
		} else timeHover = Math.max(0, timeHover - gui.timeDelta);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.disableAlpha();
		int color = 0;
		String format = FontHelper.getFormatFromString(displayString);
		if(format.length() > 1) {
			char key = format.charAt(format.length() - 1);
			if(key == 'o' && format.length() > 3)
				key = format.charAt(1);

			for(TextFormatting ecf : TextFormatting.class.getEnumConstants())
				if(ecf.toString().indexOf(ecf.toString().length() - 1) == key) {
					if(ecf.ordinal() > 15)
						ecf = TextFormatting.BLACK;
					color = LibMisc.CONTROL_CODE_COLORS[ecf.ordinal()];
					break;
				}
		}

		int maxalpha = 0x22;
		int alpha = Math.min(maxalpha, (int) (timeHover / 4 * maxalpha));
		drawRect(x - 5, y, (int) (x - 5 + timeHover * 24), y + height, alpha << 24 | color);
		GlStateManager.enableAlpha();

		boolean unicode = par1Minecraft.fontRenderer.getUnicodeFlag();
		par1Minecraft.fontRenderer.setUnicodeFlag(true);
		par1Minecraft.fontRenderer.drawString(displayString, x + (showStack ? 7 : 0), y + (height - 8) / 2, 0);
		par1Minecraft.fontRenderer.setUnicodeFlag(unicode);

		if(showStack) {
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			par1Minecraft.getRenderItem().renderItemIntoGUI(displayStack, x * 2 - 6, y * 2 + 4);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
		}
		GlStateManager.popMatrix();
	}

}
