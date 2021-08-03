/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

import java.util.*;

// Originally taken with permission from WRCBE - heavily modified
public class FXLightning extends Particle {

	private static final ResourceLocation outsideResource = new ResourceLocation(LibResources.MISC_WISP_LARGE);
	private static final ResourceLocation insideResource = new ResourceLocation(LibResources.MISC_WISP_SMALL);
	private static final int fadetime = 20;
	private final int expandTime;
	private final int colorOuter;
	private final int colorInner;

	private final List<FXLightningSegment> segments;
	private final int segmentCount;

	public FXLightning(ClientLevel world, Vector3 sourcevec, Vector3 targetvec, float speed, long seed, int colorOuter, int colorInner) {
		super(world, sourcevec.x, sourcevec.y, sourcevec.z);
		this.colorOuter = colorOuter;
		this.colorInner = colorInner;
		double length = targetvec.subtract(sourcevec).mag();
		lifetime = fadetime + world.random.nextInt(fadetime) - fadetime / 2;
		expandTime = (int) (length * speed);
		age = -(int) (length * speed);

		LightningSegmentGenerator gen = new LightningSegmentGenerator(seed);
		Pair<Integer, List<FXLightningSegment>> res = gen.compute(sourcevec, targetvec, length);
		segmentCount = res.getFirst();
		segments = res.getSecond();
	}

	@Override
	public void render(VertexConsumer buffer, Camera info, float partialTicks) {
		// todo fix this >.>

		// old way (bad position and too thick)
		// LightningHandler.queuedLightningBolts.offer(this);

		// new way (right position but heavy artifacting)

		Vec3 cameraPos = info.getPosition();
		PoseStack ms = new PoseStack();
		// 0.25f offset for a more pleasing viewing experience
		ms.translate(-cameraPos.x(), -cameraPos.y() + 0.25F, -cameraPos.z());
		renderBolt(ms, buffer, 0, false);
		renderBolt(ms, buffer, 1, true);
	}

	@Nonnull
	@Override
	public ParticleRenderType getRenderType() {
		return RENDER;
	}

	public void renderBolt(PoseStack ms, VertexConsumer wr, int pass, boolean inner) {
		Matrix4f mat = ms.last().pose();

		float boltAge = age < 0 ? 0 : (float) age / (float) lifetime;
		float mainAlpha;
		if (pass == 0) {
			Minecraft.getInstance().getTextureManager().bind(outsideResource);
			mainAlpha = (1 - boltAge) * 0.4F;
		} else {
			Minecraft.getInstance().getTextureManager().bind(insideResource);
			mainAlpha = 1 - boltAge * 0.5F;
		}

		int renderstart = (int) ((expandTime / 2 - lifetime + age) / (float) (expandTime / 2) * segmentCount);
		int renderend = (int) ((age + expandTime) / (float) expandTime * segmentCount);

		for (FXLightningSegment rendersegment : segments) {
			if (rendersegment.segmentNo < renderstart || rendersegment.segmentNo > renderend) {
				continue;
			}

			Vector3 playerVec = getRelativeViewVector(rendersegment.startPoint.point).multiply(-1);

			double width = 0.025F * (playerVec.mag() / 5 + 1) * (1 + rendersegment.light) * 0.5F;

			Vector3 diff1 = playerVec.crossProduct(rendersegment.prevDiff).normalize().multiply(width / rendersegment.sinPrev);
			Vector3 diff2 = playerVec.crossProduct(rendersegment.nextDiff).normalize().multiply(width / rendersegment.sinNext);

			Vector3 startvec = rendersegment.startPoint.point;
			Vector3 endvec = rendersegment.endPoint.point;

			int color = inner ? colorInner : colorOuter;
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0xFF00) >> 8;
			int b = color & 0xFF;
			int a = (int) (mainAlpha * rendersegment.light * 0xFF);
			int fullbright = 0xF000F0;

			endvec.subtract(diff2).vertex(mat, wr);
			wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
			startvec.subtract(diff1).vertex(mat, wr);
			wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
			startvec.add(diff1).vertex(mat, wr);
			wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
			endvec.add(diff2).vertex(mat, wr);
			wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();

			if (rendersegment.next == null) {
				Vector3 roundend = rendersegment.endPoint.point.add(rendersegment.diff.normalize().multiply(width));

				roundend.subtract(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0, 0).uv2(fullbright).endVertex();
				endvec.subtract(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
				endvec.add(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
				roundend.add(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0, 1).uv2(fullbright).endVertex();
			}

			if (rendersegment.prev == null) {
				Vector3 roundend = rendersegment.startPoint.point.subtract(rendersegment.diff.normalize().multiply(width));

				startvec.subtract(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
				roundend.subtract(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0, 0).uv2(fullbright).endVertex();
				roundend.add(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0, 1).uv2(fullbright).endVertex();
				startvec.add(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
			}
		}
	}

	private static Vector3 getRelativeViewVector(Vector3 pos) {
		Entity renderEntity = Minecraft.getInstance().getCameraEntity();
		return new Vector3((float) renderEntity.getX() - pos.x, (float) renderEntity.getY() - pos.y, (float) renderEntity.getZ() - pos.z);
	}

	public static final ParticleRenderType RENDER = new ParticleRenderType() {
		@Override
		public void begin(BufferBuilder buffer, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
		}

		@Override
		public void end(Tesselator tess) {
			tess.end();
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
		}

		@Override
		public String toString() {
			return "botania:lightning";
		}
	};

}
