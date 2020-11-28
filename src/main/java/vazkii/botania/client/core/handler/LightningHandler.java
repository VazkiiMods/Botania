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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.lib.LibResources;

import java.util.ArrayDeque;
import java.util.Deque;

public class LightningHandler {

	private LightningHandler() {}

	private static final int BATCH_THRESHOLD = 200;
	private static final Identifier outsideResource = new Identifier(LibResources.MISC_WISP_LARGE);
	private static final Identifier insideResource = new Identifier(LibResources.MISC_WISP_SMALL);
	public static final Deque<FXLightning> queuedLightningBolts = new ArrayDeque<>();

	public static void onRenderWorldLast(float frame, MatrixStack ms) {
		Profiler profiler = MinecraftClient.getInstance().getProfiler();

		profiler.push("botania-particles");
		profiler.push("lightning");

		Entity entity = MinecraftClient.getInstance().player;
		TextureManager render = MinecraftClient.getInstance().getTextureManager();

		double interpPosX = entity.lastRenderX + (entity.getX() - entity.lastRenderX) * frame;
		double interpPosY = entity.lastRenderY + (entity.getY() - entity.lastRenderY) * frame;
		double interpPosZ = entity.lastRenderZ + (entity.getZ() - entity.lastRenderZ) * frame;

		ms.push();
		ms.translate(-interpPosX, -interpPosY, -interpPosZ);

		Tessellator tessellator = Tessellator.getInstance();

		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		render.bindTexture(outsideResource);
		int counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(ms, tessellator.getBuffer(), 0, false);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			}
			counter++;
		}
		tessellator.draw();

		render.bindTexture(insideResource);
		counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(ms, tessellator.getBuffer(), 1, true);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			}
			counter++;
		}
		tessellator.draw();

		queuedLightningBolts.clear();

		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);

		ms.pop();

		profiler.pop();
		profiler.pop();

	}
}
