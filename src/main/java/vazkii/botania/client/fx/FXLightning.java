/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;

import java.util.*;

// Originally taken with permission from WRCBE - heavily modified
public class FXLightning extends Particle {

	private static final int fadetime = 20;
	private final int expandTime;
	private final int colorOuter;
	private final int colorInner;

	private final List<FXLightningSegment> segments;
	private final int segmentCount;

	public FXLightning(ClientWorld world, Vector3 sourcevec, Vector3 targetvec, float speed, long seed, int colorOuter, int colorInner) {
		super(world, sourcevec.x, sourcevec.y, sourcevec.z);
		this.colorOuter = colorOuter;
		this.colorInner = colorInner;
		double length = targetvec.subtract(sourcevec).mag();
		maxAge = fadetime + world.rand.nextInt(fadetime) - fadetime / 2;
		expandTime = (int) (length * speed);
		age = -(int) (length * speed);

		LightningSegmentGenerator gen = new LightningSegmentGenerator(seed);
		Pair<Integer, List<FXLightningSegment>> res = gen.compute(sourcevec, targetvec, length);
		segmentCount = res.getFirst();
		segments = res.getSecond();
	}

	@Override
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo info, float partialTicks) {
		// todo fix this >.>

		// old way (bad position and too thick)
		// LightningHandler.queuedLightningBolts.offer(this);

		// new way (right position but heavy artifacting)
		/*
		Vector3d cameraPos = info.getProjectedView();
		MatrixStack ms = new MatrixStack();
		ms.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		renderBolt(ms, buffer, 0, false);
		renderBolt(ms, buffer, 1, true);
		*/
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return LAYER;
	}

	public void renderBolt(MatrixStack ms, IVertexBuilder wr, int pass, boolean inner) {
		Matrix4f mat = ms.getLast().getMatrix();

		float boltAge = age < 0 ? 0 : (float) age / (float) maxAge;
		float mainAlpha;
		if (pass == 0) {
			mainAlpha = (1 - boltAge) * 0.4F;
		} else {
			mainAlpha = 1 - boltAge * 0.5F;
		}

		int renderstart = (int) ((expandTime / 2 - maxAge + age) / (float) (expandTime / 2) * segmentCount);
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
			wr.color(r, g, b, a).tex(0.5F, 0).lightmap(fullbright).endVertex();
			startvec.subtract(diff1).vertex(mat, wr);
			wr.color(r, g, b, a).tex(0.5F, 0).lightmap(fullbright).endVertex();
			startvec.add(diff1).vertex(mat, wr);
			wr.color(r, g, b, a).tex(0.5F, 1).lightmap(fullbright).endVertex();
			endvec.add(diff2).vertex(mat, wr);
			wr.color(r, g, b, a).tex(0.5F, 1).lightmap(fullbright).endVertex();

			if (rendersegment.next == null) {
				Vector3 roundend = rendersegment.endPoint.point.add(rendersegment.diff.normalize().multiply(width));

				roundend.subtract(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0, 0).lightmap(fullbright).endVertex();
				endvec.subtract(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0.5F, 0).lightmap(fullbright).endVertex();
				endvec.add(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0.5F, 1).lightmap(fullbright).endVertex();
				roundend.add(diff2).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0, 1).lightmap(fullbright).endVertex();
			}

			if (rendersegment.prev == null) {
				Vector3 roundend = rendersegment.startPoint.point.subtract(rendersegment.diff.normalize().multiply(width));

				startvec.subtract(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0.5F, 0).lightmap(fullbright).endVertex();
				roundend.subtract(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0, 0).lightmap(fullbright).endVertex();
				roundend.add(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0, 1).lightmap(fullbright).endVertex();
				startvec.add(diff1).vertex(mat, wr);
				wr.color(r, g, b, a).tex(0.5F, 1).lightmap(fullbright).endVertex();
			}
		}
	}

	private static Vector3 getRelativeViewVector(Vector3 pos) {
		Entity renderEntity = Minecraft.getInstance().getRenderViewEntity();
		return new Vector3((float) renderEntity.getPosX() - pos.x, (float) renderEntity.getPosY() - pos.y, (float) renderEntity.getPosZ() - pos.z);
	}

	private static final IParticleRenderType LAYER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder buffer, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableTexture();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
		}

		@Override
		public void finishRender(Tessellator tess) {
			tess.draw();
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
		}
	};

}
