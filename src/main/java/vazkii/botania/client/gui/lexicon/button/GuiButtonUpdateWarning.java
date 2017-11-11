/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 25, 2015, 6:13:11 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonUpdateWarning extends GuiButtonLexicon {

	public GuiButtonUpdateWarning(int id, int x, int y) {
		super(id, x, y, 11, 11, "");
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		if(!visible || !enabled)
			return;

		hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
		int k = getHoverState(hovered);

		boolean red = k == 2 || ClientTickHandler.ticksInGame % 10 < 5;

		par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
		GlStateManager.color(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x, y, red ? 153 : 142, 180, 11, 11);

		List<String> tooltip = new ArrayList<>();
		String version = PersistentVariableHelper.lastBotaniaVersion;
		for(int i = 0; i < 6; i++) {
			tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.changes" + i, version).replaceAll("&", "\u00a7"));
			if(i == 3)
				tooltip.add("");
		}

		int tooltipY = (tooltip.size() - 1) * 10 - 25;
		if(k == 2)
			RenderHelper.renderTooltip(par2 - 125, par3 + tooltipY, tooltip);
	}

}
