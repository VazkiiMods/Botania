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

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

public final class TooltipAdditionDisplayHandler {

	/* todo 1.16-fabric
	public static void onToolTipRender(RenderTooltipEvent.PostText evt) {
		if (evt.getStack().isEmpty()) {
			return;
		}
		MatrixStack ms = evt.getMatrixStack();
	
		ItemStack stack = evt.getStack();
		int width = evt.getWidth();
		int height = 3;
		int tooltipX = evt.getX();
		int tooltipY = evt.getY() - 4;
		TextRenderer font = evt.getFontRenderer();
	
		if (stack.getItem() instanceof ItemTerraPick) {
			drawTerraPick(ms, stack, tooltipX, tooltipY, width, height, font);
		} else if (stack.getItem() instanceof IManaTooltipDisplay) {
			drawManaBar(ms, stack, (IManaTooltipDisplay) stack.getItem(), tooltipX, tooltipY, width, height);
		}
	}
	*/

	private static void drawTerraPick(PoseStack ms, ItemStack stack, int mouseX, int mouseY, int width, int height, Font font) {
		int level = ItemTerraPick.getLevel(stack);
		int max = ItemTerraPick.LEVELS[Math.min(ItemTerraPick.LEVELS.length - 1, level + 1)];
		boolean ss = level >= ItemTerraPick.LEVELS.length - 1;
		int curr = ItemTerraPick.getMana_(stack);
		float percent = level == 0 ? 0F : (float) curr / (float) max;
		int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percent));
		float huePer = width == 0 ? 0F : 1F / width;
		float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

		RenderSystem.disableDepthTest();
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

	private static void drawManaBar(PoseStack ms, ItemStack stack, IManaTooltipDisplay display, int mouseX, int mouseY, int width, int height) {
		float fraction = display.getManaFractionForDisplay(stack);
		int manaBarWidth = (int) Math.ceil(width * fraction);

		RenderSystem.disableDepthTest();
		GuiComponent.fill(ms, mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		GuiComponent.fill(ms, mouseX, mouseY - height, mouseX + manaBarWidth, mouseY, 0xFF000000 | Mth.hsvToRgb(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
		GuiComponent.fill(ms, mouseX + manaBarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
	}

}
