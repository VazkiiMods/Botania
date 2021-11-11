/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;

import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

public final class TooltipAdditionDisplayHandler {

	public static void onToolTipRender(PoseStack ms, ManaBarTooltipComponent component, int width, int tooltipX, int tooltipY, Font font) {
		if (component.getPickLevel() >= 0) {
			drawTerraPick(ms, component, tooltipX, tooltipY - 4, width, 3, font);
		} else {
			drawManaBar(ms, component, tooltipX, tooltipY - 4, width, 3);
		}
	}

	private static void drawTerraPick(PoseStack ms, ManaBarTooltipComponent component, int mouseX, int mouseY, int width, int height, Font font) {
		int level = component.getPickLevel();
		float percent = component.getPercentageFull();
		boolean ss = level >= ItemTerraPick.LEVELS.length - 1;

		int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percent));
		float huePer = width == 0 ? 0F : 1F / width;
		float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

		RenderSystem.disableDepthTest();
		Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
		GuiComponent.fill(ms, mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		for (int i = 0; i < rainbowWidth; i++) {
			GuiComponent.fill(ms, mouseX + i, mouseY - height, mouseX + i + 1, mouseY, 0xFF000000 | Mth.hsvToRgb((hueOff + huePer * i) % 1F, 1F, 1F));
		}
		GuiComponent.fill(ms, mouseX + rainbowWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);

		String rank = I18n.get("botania.rank" + level).replaceAll("&", "\u00a7");

		ms.pushPose();
		ms.translate(0, 0, 300);
		font.drawShadow(ms, rank, mouseX, mouseY - 12, 0xFFFFFF);
		if (!ss) {
			rank = I18n.get("botania.rank" + (level + 1)).replaceAll("&", "\u00a7");
			font.drawShadow(ms, rank, mouseX + width - font.width(rank), mouseY - 12, 0xFFFFFF);
		}
		ms.popPose();
		RenderSystem.enableDepthTest();
	}

	private static void drawManaBar(PoseStack ms, ManaBarTooltipComponent component, int mouseX, int mouseY, int width, int height) {
		float fraction = component.getPercentageFull();
		int manaBarWidth = (int) Math.ceil(width * fraction);

		RenderSystem.disableDepthTest();
		Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
		GuiComponent.fill(ms, mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		GuiComponent.fill(ms, mouseX, mouseY - height, mouseX + manaBarWidth, mouseY, 0xFF000000 | Mth.hsvToRgb(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
		GuiComponent.fill(ms, mouseX + manaBarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
	}

}
