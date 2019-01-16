/**
 * This class was created by <Vazkii/ChickenBones>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 3, 2014, 9:05:38 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.fx.ParticleRenderDispatcher;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayDeque;
import java.util.Deque;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public class LightningHandler {

	private LightningHandler() {}

	private static final int BATCH_THRESHOLD = 200;
	private static final ResourceLocation outsideResource = new ResourceLocation(LibResources.MISC_WISP_LARGE);
	private static final ResourceLocation insideResource = new ResourceLocation(LibResources.MISC_WISP_SMALL);
	public static final Deque<FXLightning> queuedLightningBolts = new ArrayDeque<>();

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		Profiler profiler = Minecraft.getMinecraft().profiler;

		profiler.startSection("botania-particles");
		ParticleRenderDispatcher.dispatch();
		profiler.startSection("redString");
		RedStringRenderer.renderAll();
		profiler.endStartSection("lightning");

		float frame = event.getPartialTicks();
		Entity entity = Minecraft.getMinecraft().player;
		TextureManager render = Minecraft.getMinecraft().renderEngine;

		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);

		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		ParticleRenderDispatcher.lightningCount = 0;

		render.bindTexture(outsideResource);
		int counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(0, false);
			if(counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		render.bindTexture(insideResource);
		counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(1, true);
			if(counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		queuedLightningBolts.clear();

		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);

		GlStateManager.translate(interpPosX, interpPosY, interpPosZ);
		GlStateManager.popMatrix();

		profiler.endSection();
		profiler.endSection();

	}
}