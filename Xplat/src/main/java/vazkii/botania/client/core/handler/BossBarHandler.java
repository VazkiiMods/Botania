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
import com.mojang.blaze3d.vertex.*;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityDoppleganger;

import java.util.*;

public final class BossBarHandler {

	private BossBarHandler() {}

	// Only access on the client thread!
	public static final Set<EntityDoppleganger> bosses = Collections.newSetFromMap(new WeakHashMap<>());
	private static final ResourceLocation BAR_TEXTURE = new ResourceLocation(LibResources.GUI_BOSS_BAR);

	public static OptionalInt onBarRender(PoseStack ps, int x, int y, BossEvent bossEvent, boolean drawName) {
		for (EntityDoppleganger currentBoss : bosses) {
			if (currentBoss.getBossInfoUuid().equals(bossEvent.getId())) {
				Minecraft mc = Minecraft.getInstance();
				// todo boss_bar.png has textures for different colors, respect bossEvent's getColor()?
				int frameU = 0, frameV = 0;
				int frameWidth = 185, frameHeight = 15;
				int healthU = 0, healthV = frameV + frameHeight;
				int healthWidth = 181, healthHeight = 7;
				int healthX = x + (frameWidth - healthWidth) / 2;
				int healthY = y + (frameHeight - healthHeight) / 2;

				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				int playerCountHeight = drawPlayerCount(currentBoss.getPlayerCount(), ps, x, y);
				RenderSystem.setShaderTexture(0, BAR_TEXTURE);
				RenderHelper.drawTexturedModalRect(ps, x, y, frameU, frameV,
						frameWidth, frameHeight);
				drawHealthBar(ps, currentBoss, healthX, healthY, healthU, healthV,
						(int) (healthWidth * bossEvent.getProgress()), healthHeight, false);

				if (drawName) {
					Component name = bossEvent.getName();
					int centerX = mc.getWindow().getGuiScaledWidth() / 2;
					int nameX = centerX - mc.font.width(name) / 2;
					mc.font.drawShadow(ps, name, nameX, y - 10, 0xA2018C);
				}

				return OptionalInt.of(frameHeight + playerCountHeight + (drawName ? mc.font.lineHeight : 0));
			}
		}

		return OptionalInt.empty();
	}

	private static int drawPlayerCount(int playerCount, PoseStack ps, int x, int y) {
		ps.pushPose();
		int px = x + 160;
		int py = y + 12;

		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		mc.getItemRenderer().renderGuiItem(stack, px, py);

		mc.font.drawShadow(ps, Integer.toString(playerCount), px + 15, py + 4, 0xFFFFFF);
		ps.popPose();

		return 5;
	}

	private static void drawHealthBar(PoseStack ms, EntityDoppleganger currentBoss, int x, int y, int u, int v, int w, int h, boolean bg) {
		var shader = CoreShaders.dopplegangerBar();
		if (shader != null) {
			float time = currentBoss.getInvulTime();
			float grainIntensity = time > 20 ? 1F : Math.max(currentBoss.isHardMode() ? 0.5F : 0F, time / 20F);
			shader.safeGetUniform("BotaniaGrainIntensity").set(grainIntensity);
			shader.safeGetUniform("BotaniaHpFract").set(currentBoss.getHealth() / currentBoss.getMaxHealth());
		}

		float minU = u / 256.0F;
		float maxU = (u + w) / 256.0F;
		float minV = v / 256.0F;
		float maxV = (v + h) / 256.0F;

		var matrix = ms.last().pose();
		RenderSystem.setShader(CoreShaders::dopplegangerBar);
		BufferBuilder builder = Tesselator.getInstance().getBuilder();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		builder.vertex(matrix, x, y + h, 0).uv(minU, maxV).endVertex();
		builder.vertex(matrix, x + w, y + h, 0).uv(maxU, maxV).endVertex();
		builder.vertex(matrix, x + w, y, 0).uv(maxU, minV).endVertex();
		builder.vertex(matrix, x, y, 0).uv(minU, minV).endVertex();
		builder.end();
		BufferUploader.end(builder);
	}

}
