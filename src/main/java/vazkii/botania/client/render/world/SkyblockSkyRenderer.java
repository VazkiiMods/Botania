/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.world;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.SkyRenderHandler;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.mixin.AccessorWorldRenderer;

import java.util.Random;

public class SkyblockSkyRenderer implements SkyRenderHandler {

	private static final ResourceLocation textureSkybox = new ResourceLocation(LibResources.MISC_SKYBOX);
	private static final ResourceLocation textureRainbow = new ResourceLocation(LibResources.MISC_RAINBOW);
	private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
	private static final ResourceLocation[] planetTextures = new ResourceLocation[] {
			new ResourceLocation(LibResources.MISC_PLANET + "0.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "1.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "2.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "3.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "4.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "5.png")
	};

	// [VanillaCopy] WorldRenderer.renderSky, overworld section, edits noted
	@Override
	public void render(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, Minecraft mc) {
		// Environment setup
		VertexBuffer skyVBO = ((AccessorWorldRenderer) mc.worldRenderer).getSkyVBO();
		VertexFormat skyVertexFormat = ((AccessorWorldRenderer) mc.worldRenderer).getSkyVertexFormat();

		RenderSystem.disableTexture();
		Vector3d Vector3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
		float f = (float) Vector3d.x;
		float f1 = (float) Vector3d.y;
		float f2 = (float) Vector3d.z;

		// Botania - darken in void
		float insideVoid = 0;
		if (mc.player.getPosY() <= -2) {
			insideVoid = (float) Math.min(1F, -(mc.player.getPosY() + 2) / 30F);
		}

		f = Math.max(0F, f - insideVoid);
		f1 = Math.max(0F, f1 - insideVoid);
		f2 = Math.max(0F, f2 - insideVoid);

		FogRenderer.applyFog();
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(f, f1, f2);
		skyVBO.bindBuffer();
		skyVertexFormat.setupBufferState(0L);
		skyVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
		VertexBuffer.unbindBuffer();
		skyVertexFormat.clearBufferState();
		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		float[] afloat = world.func_239132_a_().func_230492_a_(world.getCelestialAngle(partialTicks), partialTicks);
		if (afloat != null) {
			RenderSystem.disableTexture();
			RenderSystem.shadeModel(7425);
			matrixStackIn.push();
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
			float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
			float f4 = afloat[0];
			float f5 = afloat[1];
			float f6 = afloat[2];
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
			int i = 16;

			for (int j = 0; j <= 16; ++j) {
				float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
				float f8 = MathHelper.sin(f7);
				float f9 = MathHelper.cos(f7);
				bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
			}

			bufferbuilder.finishDrawing();
			WorldVertexBufferUploader.draw(bufferbuilder);
			matrixStackIn.pop();
			RenderSystem.shadeModel(7424);
		}

		RenderSystem.enableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		matrixStackIn.push();
		float f11 = 1.0F - world.getRainStrength(partialTicks);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, f11);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
		// Botania: extras
		renderExtra(matrixStackIn, world, partialTicks, insideVoid);
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(world.getCelestialAngle(partialTicks) * 360.0F));
		Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();
		float f12 = 60.0F; // Botania: 30 -> 60
		mc.textureManager.bindTexture(SUN_TEXTURES);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
		bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
		bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
		bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
		f12 = 60.0F; // Botania: 20 -> 60
		mc.textureManager.bindTexture(MOON_PHASES_TEXTURES);
		int k = world.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f13 = (float) (l + 0) / 4.0F;
		float f14 = (float) (i1 + 0) / 2.0F;
		float f15 = (float) (l + 1) / 4.0F;
		float f16 = (float) (i1 + 1) / 2.0F;
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(matrix4f1, -f12, -100.0F, f12).tex(f15, f16).endVertex();
		bufferbuilder.pos(matrix4f1, f12, -100.0F, f12).tex(f13, f16).endVertex();
		bufferbuilder.pos(matrix4f1, f12, -100.0F, -f12).tex(f13, f14).endVertex();
		bufferbuilder.pos(matrix4f1, -f12, -100.0F, -f12).tex(f15, f14).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
		RenderSystem.disableTexture();
		// Botania: custom stars
		{
			float celAng = world.getCelestialAngle(partialTicks);
			float effCelAng = celAng;
			if (celAng > 0.5) {
				effCelAng = 0.5F - (celAng - 0.5F);
			}
			float starAlpha = f11 * Math.max(0.1F, effCelAng * 2);
			renderStars(skyVertexFormat, matrixStackIn, mc, starAlpha, partialTicks);
		}
		/*
		float f10 = world.getStarBrightness(partialTicks) * f11;
		if (f10 > 0.0F) {
			RenderSystem.color4f(f10, f10, f10, f10);
			this.starVBO.bindBuffer();
			this.skyVertexFormat.setupBufferState(0L);
			this.starVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
			VertexBuffer.unbindBuffer();
			this.skyVertexFormat.clearBufferState();
		}
		*/

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		matrixStackIn.pop();
		// Botania: No horizon
		/*
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		double d0 = mc.player.getEyePosition(partialTicks).y - world.getHorizonHeight();
		if (d0 < 0.0D) {
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, 12.0D, 0.0D);
			sky2VBO.bindBuffer();
			skyVertexFormat.setupBufferState(0L);
			sky2VBO.draw(matrixStackIn.getLast().getMatrix(), 7);
			VertexBuffer.unbindBuffer();
			skyVertexFormat.clearBufferState();
			matrixStackIn.pop();
		}
		
		if (world.dimension.isSkyColored()) {
			RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		} else {
			RenderSystem.color3f(f, f1, f2);
		}
		*/

		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
		RenderSystem.disableFog();
	}

	private void renderExtra(MatrixStack ms, ClientWorld world, float partialTicks, float insideVoid) {
		// Botania - Begin extra stuff
		Tessellator tessellator = Tessellator.getInstance();
		float rain = 1.0F - world.getRainStrength(partialTicks);
		float celAng = world.getCelestialAngle(partialTicks);
		float effCelAng = celAng;
		if (celAng > 0.5) {
			effCelAng = 0.5F - (celAng - 0.5F);
		}

		// === Planets
		float scale = 20F;
		float lowA = Math.max(0F, effCelAng - 0.3F) * rain;
		float a = Math.max(0.1F, lowA);

		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		ms.push();
		RenderSystem.color4f(1F, 1F, 1F, a * 4 * (1F - insideVoid));
		ms.rotate(new Vector3f(0.5F, 0.5F, 0F).rotationDegrees(90));
		for (int p = 0; p < planetTextures.length; p++) {
			Minecraft.getInstance().textureManager.bindTexture(planetTextures[p]);
			Matrix4f mat = ms.getLast().getMatrix();
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			tessellator.getBuffer().pos(mat, -scale, 100, -scale).tex(0.0F, 0.0F).endVertex();
			tessellator.getBuffer().pos(mat, scale, 100, -scale).tex(1.0F, 0.0F).endVertex();
			tessellator.getBuffer().pos(mat, scale, 100, scale).tex(1.0F, 1.0F).endVertex();
			tessellator.getBuffer().pos(mat, -scale, 100, scale).tex(0.0F, 1.0F).endVertex();
			tessellator.draw();

			switch (p) {
			case 0:
				ms.rotate(Vector3f.XP.rotationDegrees(70));
				scale = 12F;
				break;
			case 1:
				ms.rotate(Vector3f.ZP.rotationDegrees(120));
				scale = 15F;
				break;
			case 2:
				ms.rotate(new Vector3f(1, 0, 1).rotationDegrees(80));
				scale = 25F;
				break;
			case 3:
				ms.rotate(Vector3f.ZP.rotationDegrees(100));
				scale = 10F;
				break;
			case 4:
				ms.rotate(new Vector3f(1, 0, 0.5F).rotationDegrees(-60));
				scale = 40F;
			}
		}
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		ms.pop();

		// === Rays
		Minecraft.getInstance().textureManager.bindTexture(textureSkybox);

		scale = 20F;
		a = lowA;
		ms.push();
		RenderSystem.blendFuncSeparate(770, 1, 1, 0);
		ms.translate(0, -1, 0);
		ms.rotate(Vector3f.XP.rotationDegrees(220));
		RenderSystem.color4f(1F, 1F, 1F, a);
		int angles = 90;
		float y = 2F;
		float y0 = 0F;
		float uPer = 1F / 360F;
		float anglePer = 360F / angles;
		double fuzzPer = Math.PI * 10 / angles;
		float rotSpeed = 1F;
		float rotSpeedMod = 0.4F;

		for (int p = 0; p < 3; p++) {
			float baseAngle = rotSpeed * rotSpeedMod * (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks);
			ms.rotate(Vector3f.YP.rotationDegrees((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.25F * rotSpeed * rotSpeedMod));

			Matrix4f mat = ms.getLast().getMatrix();
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			for (int i = 0; i < angles; i++) {
				int j = i;
				if (i % 2 == 0) {
					j--;
				}

				float ang = j * anglePer + baseAngle;
				float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
				float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
				float yo = (float) Math.sin(fuzzPer * j) * 1;

				float ut = ang * uPer;
				if (i % 2 == 0) {
					tessellator.getBuffer().pos(mat, xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
					tessellator.getBuffer().pos(mat, xp, yo + y0, zp).tex(ut, 0).endVertex();
				} else {
					tessellator.getBuffer().pos(mat, xp, yo + y0, zp).tex(ut, 0).endVertex();
					tessellator.getBuffer().pos(mat, xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				}

			}
			tessellator.draw();

			switch (p) {
			case 0:
				ms.rotate(Vector3f.XP.rotationDegrees(20));
				RenderSystem.color4f(1F, 0.4F, 0.4F, a);
				fuzzPer = Math.PI * 14 / angles;
				rotSpeed = 0.2F;
				break;
			case 1:
				ms.rotate(Vector3f.XP.rotationDegrees(50));
				RenderSystem.color4f(0.4F, 1F, 0.7F, a);
				fuzzPer = Math.PI * 6 / angles;
				rotSpeed = 2F;
				break;
			}
		}
		ms.pop();

		// === Rainbow
		ms.push();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		Minecraft.getInstance().textureManager.bindTexture(textureRainbow);
		scale = 10F;
		float effCelAng1 = celAng;
		if (effCelAng1 > 0.25F) {
			effCelAng1 = 1F - effCelAng1;
		}
		effCelAng1 = 0.25F - Math.min(0.25F, effCelAng1);

		long time = world.getDayTime() + 1000;
		int day = (int) (time / 24000L);
		Random rand = new Random(day * 0xFF);
		float angle1 = rand.nextFloat() * 360F;
		float angle2 = rand.nextFloat() * 360F;
		RenderSystem.color4f(1F, 1F, 1F, effCelAng1 * (1F - insideVoid));
		ms.rotate(Vector3f.YP.rotationDegrees(angle1));
		ms.rotate(Vector3f.ZP.rotationDegrees(angle2));

		Matrix4f mat = ms.getLast().getMatrix();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		for (int i = 0; i < angles; i++) {
			int j = i;
			if (i % 2 == 0) {
				j--;
			}

			float ang = j * anglePer;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
			float yo = 0;

			float ut = ang * uPer;
			if (i % 2 == 0) {
				tessellator.getBuffer().pos(mat, xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				tessellator.getBuffer().pos(mat, xp, yo + y0, zp).tex(ut, 0).endVertex();
			} else {
				tessellator.getBuffer().pos(mat, xp, yo + y0, zp).tex(ut, 0).endVertex();
				tessellator.getBuffer().pos(mat, xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
			}

		}
		tessellator.draw();
		ms.pop();
		RenderSystem.color4f(1F, 1F, 1F, 1F - insideVoid);
		GlStateManager.blendFuncSeparate(770, 1, 1, 0);
	}

	private void renderStars(VertexFormat format, MatrixStack ms, Minecraft mc, float alpha, float partialTicks) {
		net.minecraft.client.renderer.vertex.VertexBuffer starVBO = ((AccessorWorldRenderer) mc.worldRenderer).getStarVBO();

		float t = (ClientTickHandler.ticksInGame + partialTicks + 2000) * 0.005F;
		ms.push();

		ms.push();
		ms.rotate(Vector3f.YP.rotationDegrees(t * 3));
		RenderSystem.color4f(1F, 1F, 1F, alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.push();
		ms.rotate(Vector3f.YP.rotationDegrees(t));
		RenderSystem.color4f(0.5F, 1F, 1F, alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.push();
		ms.rotate(Vector3f.YP.rotationDegrees(t * 2));
		RenderSystem.color4f(1F, 0.75F, 0.75F, alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.push();
		ms.rotate(Vector3f.ZP.rotationDegrees(t * 3));
		RenderSystem.color4f(1F, 1F, 1F, 0.25F * alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.push();
		ms.rotate(Vector3f.ZP.rotationDegrees(t));
		RenderSystem.color4f(0.5F, 1F, 1F, 0.25F * alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.push();
		ms.rotate(Vector3f.ZP.rotationDegrees(t * 2));
		RenderSystem.color4f(1F, 0.75F, 0.75F, 0.25F * alpha);
		drawVbo(format, ms, starVBO);
		ms.pop();

		ms.pop();
	}

	private void drawVbo(VertexFormat skyVertexFormat, MatrixStack ms, VertexBuffer vbo) {
		vbo.bindBuffer();
		skyVertexFormat.setupBufferState(0);
		vbo.draw(ms.getLast().getMatrix(), GL11.GL_QUADS);
		VertexBuffer.unbindBuffer();
		skyVertexFormat.clearBufferState();
	}

}
