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
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconChallenge;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonChallengeIcon extends GuiButtonLexicon {

	public final Challenge challenge;
	private final GuiLexicon owner;

	public GuiButtonChallengeIcon(int x, int y, Challenge challenge, GuiLexicon owner) {
		super(x, y, 16, 16, "", b -> Minecraft.getInstance().displayGuiScreen(new GuiLexiconChallenge(owner, challenge)));
		this.challenge = challenge;
		this.owner = owner;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getYImage(isHovered());

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(challenge.icon, x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();

		if(challenge.complete) {
			GlStateManager.disableDepthTest();
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 9, 0x004C00);
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 8, 0x0BD20D);
			GlStateManager.enableDepthTest();
		}


		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.AQUA + I18n.format(challenge.unlocalizedName));

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}
