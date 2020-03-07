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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayDeque;
import java.util.Deque;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class LightningHandler {

	private LightningHandler() {}

	private static final int BATCH_THRESHOLD = 200;
	private static final ResourceLocation outsideResource = new ResourceLocation(LibResources.MISC_WISP_LARGE);
	private static final ResourceLocation insideResource = new ResourceLocation(LibResources.MISC_WISP_SMALL);
	public static final Deque<FXLightning> queuedLightningBolts = new ArrayDeque<>();

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		IProfiler profiler = Minecraft.getInstance().getProfiler();

		profiler.startSection("botania-particles");
		profiler.startSection("lightning");

		float frame = event.getPartialTicks();
		Entity entity = Minecraft.getInstance().player;
		TextureManager render = Minecraft.getInstance().textureManager;

		double interpPosX = entity.lastTickPosX + (entity.getX() - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.getY() - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.getZ() - entity.lastTickPosZ) * frame;

		RenderSystem.pushMatrix();
		RenderSystem.translated(-interpPosX, -interpPosY, -interpPosZ);

		Tessellator tessellator = Tessellator.getInstance();

		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		render.bindTexture(outsideResource);
		int counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEXTURE_LIGHT_COLOR);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(0, false);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEXTURE_LIGHT_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		render.bindTexture(insideResource);
		counter = 0;

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEXTURE_LIGHT_COLOR);
		for (FXLightning bolt : queuedLightningBolts) {
			bolt.renderBolt(1, true);
			if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
				tessellator.draw();
				tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEXTURE_LIGHT_COLOR);
			}
			counter++;
		}
		tessellator.draw();

		queuedLightningBolts.clear();

		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);

		RenderSystem.translated(interpPosX, interpPosY, interpPosZ);
		RenderSystem.popMatrix();

		profiler.endSection();
		profiler.endSection();

	}
}
