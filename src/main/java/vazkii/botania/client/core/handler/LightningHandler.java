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
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.lib.LibResources;

import java.util.ArrayDeque;
import java.util.Deque;

public class LightningHandler {

	private LightningHandler() {}

	private static final int BATCH_THRESHOLD = 200;
	private static final ResourceLocation outsideResource = new ResourceLocation(LibResources.MISC_WISP_LARGE);
	private static final ResourceLocation insideResource = new ResourceLocation(LibResources.MISC_WISP_SMALL);
	public static final Deque<FXLightning> queuedLightningBolts = new ArrayDeque<>();

	public static void onRenderWorldLast(float frame, PoseStack ms) {
		ProfilerFiller profiler = Minecraft.getInstance().getProfiler();

		profiler.push("botania-particles");
		profiler.push("lightning");

		Entity entity = Minecraft.getInstance().player;
		TextureManager render = Minecraft.getInstance().getTextureManager();

		double interpPosX = entity.xOld + (entity.getX() - entity.xOld) * frame;
		double interpPosY = entity.yOld + (entity.getY() - entity.yOld) * frame;
		double interpPosZ = entity.zOld + (entity.getZ() - entity.zOld) * frame;

		ms.pushPose();
		ms.translate(-interpPosX, -interpPosY, -interpPosZ);

		Tesselator tessellator = Tesselator.getInstance();

		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		render.bind(outsideResource);
		int counter = 0;

		tessellator.getBuilder().begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(ms, tessellator.getBuilder(), 0, false);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.end();
				tessellator.getBuilder().begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
			}
			counter++;
		}
		tessellator.end();

		render.bind(insideResource);
		counter = 0;

		tessellator.getBuilder().begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(ms, tessellator.getBuilder(), 1, true);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.end();
				tessellator.getBuilder().begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
			}
			counter++;
		}
		tessellator.end();

		queuedLightningBolts.clear();

		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);

		ms.popPose();

		profiler.pop();
		profiler.pop();

	}
}
