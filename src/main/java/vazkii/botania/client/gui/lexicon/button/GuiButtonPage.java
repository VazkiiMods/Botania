/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 16, 2014, 4:52:06 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.Collections;

public class GuiButtonPage extends GuiButtonLexicon {

	final boolean right;

	public GuiButtonPage(int par1, int par2, int par3, boolean right) {
		super(par1, par2, par3, 18, 10, "");
		this.right = right;
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		if(enabled) {
			hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(x, y, k == 2 ? 18 : 0, right ? 180 : 190, 18, 10);

			if(k == 2)
				RenderHelper.renderTooltip(par2, par3, Collections.singletonList(I18n.format(right ? "botaniamisc.nextPage" : "botaniamisc.prevPage")));
		}
	}

}
