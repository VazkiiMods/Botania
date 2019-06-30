/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [12/09/2016, 03:17:42 (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonScaleChange extends GuiButtonLexicon {

	public GuiButtonScaleChange(int id, int x, int y) {
		super(id, x, y, 11, 11, "");
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		int maxAllowed = GuiLexicon.getMaxAllowedScale();
		if(PersistentVariableHelper.lexiconGuiScale >= maxAllowed)
			PersistentVariableHelper.lexiconGuiScale = 2;
		else PersistentVariableHelper.lexiconGuiScale++;

		PersistentVariableHelper.saveSafe();
		Minecraft.getInstance().displayGuiScreen(new GuiLexicon());
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getHoverState(hovered);

		Minecraft.getInstance().textureManager.bindTexture(GuiLexicon.texture);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		blit(x, y, k == 2 ? 152 : 141, 191, 11, 11);

		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.GREEN + I18n.format("botaniamisc.scaleChange"));

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}
