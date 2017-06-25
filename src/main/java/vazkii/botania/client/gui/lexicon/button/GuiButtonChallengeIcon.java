/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 5:00:30 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.challenge.Challenge;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonChallengeIcon extends GuiButtonLexicon {

	public final Challenge challenge;

	public GuiButtonChallengeIcon(int id, int x, int y, Challenge challenge) {
		super(id, x, y, 16, 16, "");
		this.challenge = challenge;
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
		int k = getHoverState(hovered);

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		par1Minecraft.getRenderItem().renderItemIntoGUI(challenge.icon, x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();

		if(challenge.complete) {
			GlStateManager.disableDepth();
			par1Minecraft.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 9, 0x004C00);
			par1Minecraft.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 8, 0x0BD20D);
			GlStateManager.enableDepth();
		}


		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.AQUA + I18n.format(challenge.unlocalizedName));

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(par2, par3 + tooltipY, tooltip);
	}

}
