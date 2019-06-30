/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 29, 2015, 4:16:09 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconChallengesList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonChallenges extends GuiButtonLexicon {

	public GuiButtonChallenges(int x, int y) {
		super(x, y, 11, 11, "", b -> {
			Minecraft.getInstance().displayGuiScreen(new GuiLexiconChallengesList());
		});
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getYImage(isHovered());

		Minecraft.getInstance().textureManager.bindTexture(GuiLexicon.texture);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		blit(x, y, k == 2 ? 131 : 120, 180, 11, 11);

		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.GREEN + I18n.format("botaniamisc.challenges"));

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}