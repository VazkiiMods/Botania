/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.world;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;

import java.util.Random;

public class SkyblockSkyRenderer {

	private static final ResourceLocation textureSkybox = new ResourceLocation(LibResources.MISC_SKYBOX);
	private static final ResourceLocation textureRainbow = new ResourceLocation(LibResources.MISC_RAINBOW);
	private static final ResourceLocation[] planetTextures = new ResourceLocation[] {
			new ResourceLocation(LibResources.MISC_PLANET + "0.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "1.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "2.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "3.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "4.png"),
			new ResourceLocation(LibResources.MISC_PLANET + "5.png")
	};

	public static void renderExtra(PoseStack ms, ClientLevel world, float partialTicks, float insideVoid) {
		// Botania - Begin extra stuff
		Tesselator tessellator = Tesselator.getInstance();
		float rain = 1.0F - world.getRainLevel(partialTicks);
		float celAng = world.getTimeOfDay(partialTicks);
		float effCelAng = celAng;
		if (celAng > 0.5) {
			effCelAng = 0.5F - (celAng - 0.5F);
		}

		// === Planets
		float scale = 20F;
		float lowA = Math.max(0F, effCelAng - 0.3F) * rain;
		float a = Math.max(0.1F, lowA);

		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		ms.pushPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, a * 4 * (1F - insideVoid));
		ms.mulPose(new Vector3f(0.5F, 0.5F, 0F).rotationDegrees(90));
		for (int p = 0; p < planetTextures.length; p++) {
			Minecraft.getInstance().getTextureManager().bindForSetup(planetTextures[p]);
			Matrix4f mat = ms.last().pose();
			tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			tessellator.getBuilder().vertex(mat, -scale, 100, -scale).uv(0.0F, 0.0F).endVertex();
			tessellator.getBuilder().vertex(mat, scale, 100, -scale).uv(1.0F, 0.0F).endVertex();
			tessellator.getBuilder().vertex(mat, scale, 100, scale).uv(1.0F, 1.0F).endVertex();
			tessellator.getBuilder().vertex(mat, -scale, 100, scale).uv(0.0F, 1.0F).endVertex();
			tessellator.end();

			switch (p) {
			case 0:
				ms.mulPose(Vector3f.XP.rotationDegrees(70));
				scale = 12F;
				break;
			case 1:
				ms.mulPose(Vector3f.ZP.rotationDegrees(120));
				scale = 15F;
				break;
			case 2:
				ms.mulPose(new Vector3f(1, 0, 1).rotationDegrees(80));
				scale = 25F;
				break;
			case 3:
				ms.mulPose(Vector3f.ZP.rotationDegrees(100));
				scale = 10F;
				break;
			case 4:
				ms.mulPose(new Vector3f(1, 0, 0.5F).rotationDegrees(-60));
				scale = 40F;
			}
		}
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		ms.popPose();

		// === Rays
		Minecraft.getInstance().getTextureManager().bindForSetup(textureSkybox);

		scale = 20F;
		a = lowA;
		ms.pushPose();
		RenderSystem.blendFuncSeparate(770, 1, 1, 0);
		ms.translate(0, -1, 0);
		ms.mulPose(Vector3f.XP.rotationDegrees(220));
		RenderSystem.setShaderColor(1F, 1F, 1F, a);
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
			ms.mulPose(Vector3f.YP.rotationDegrees((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.25F * rotSpeed * rotSpeedMod));

			Matrix4f mat = ms.last().pose();
			tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
					tessellator.getBuilder().vertex(mat, xp, yo + y0 + y, zp).uv(ut, 1F).endVertex();
					tessellator.getBuilder().vertex(mat, xp, yo + y0, zp).uv(ut, 0).endVertex();
				} else {
					tessellator.getBuilder().vertex(mat, xp, yo + y0, zp).uv(ut, 0).endVertex();
					tessellator.getBuilder().vertex(mat, xp, yo + y0 + y, zp).uv(ut, 1F).endVertex();
				}

			}
			tessellator.end();

			switch (p) {
			case 0:
				ms.mulPose(Vector3f.XP.rotationDegrees(20));
				RenderSystem.setShaderColor(1F, 0.4F, 0.4F, a);
				fuzzPer = Math.PI * 14 / angles;
				rotSpeed = 0.2F;
				break;
			case 1:
				ms.mulPose(Vector3f.XP.rotationDegrees(50));
				RenderSystem.setShaderColor(0.4F, 1F, 0.7F, a);
				fuzzPer = Math.PI * 6 / angles;
				rotSpeed = 2F;
				break;
			}
		}
		ms.popPose();

		// === Rainbow
		ms.pushPose();
		GlStateManager._blendFuncSeparate(770, 771, 1, 0);
		Minecraft.getInstance().getTextureManager().bindForSetup(textureRainbow);
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
		RenderSystem.setShaderColor(1F, 1F, 1F, effCelAng1 * (1F - insideVoid));
		ms.mulPose(Vector3f.YP.rotationDegrees(angle1));
		ms.mulPose(Vector3f.ZP.rotationDegrees(angle2));

		Matrix4f mat = ms.last().pose();
		tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
				tessellator.getBuilder().vertex(mat, xp, yo + y0 + y, zp).uv(ut, 1F).endVertex();
				tessellator.getBuilder().vertex(mat, xp, yo + y0, zp).uv(ut, 0).endVertex();
			} else {
				tessellator.getBuilder().vertex(mat, xp, yo + y0, zp).uv(ut, 0).endVertex();
				tessellator.getBuilder().vertex(mat, xp, yo + y0 + y, zp).uv(ut, 1F).endVertex();
			}

		}
		tessellator.end();
		ms.popPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F - insideVoid);
		GlStateManager._blendFuncSeparate(770, 1, 1, 0);
	}

	public static void renderStars(VertexBuffer starVBO, PoseStack ms, Matrix4f projMat, float partialTicks, Runnable resetFog) {
		FogRenderer.setupNoFog();
		Minecraft mc = Minecraft.getInstance();
		ShaderInstance shader = GameRenderer.getPositionShader();
		float rain = 1.0F - mc.level.getRainLevel(partialTicks);
		float celAng = mc.level.getTimeOfDay(partialTicks);
		float effCelAng = celAng;
		if (celAng > 0.5) {
			effCelAng = 0.5F - (celAng - 0.5F);
		}
		float alpha = rain * Math.max(0.1F, effCelAng * 2);

		float t = (ClientTickHandler.ticksInGame + partialTicks + 2000) * 0.005F;
		ms.pushPose();

		ms.pushPose();
		ms.mulPose(Vector3f.YP.rotationDegrees(t * 3));
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.pushPose();
		ms.mulPose(Vector3f.YP.rotationDegrees(t));
		RenderSystem.setShaderColor(0.5F, 1F, 1F, alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.pushPose();
		ms.mulPose(Vector3f.YP.rotationDegrees(t * 2));
		RenderSystem.setShaderColor(1F, 0.75F, 0.75F, alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.pushPose();
		ms.mulPose(Vector3f.ZP.rotationDegrees(t * 3));
		RenderSystem.setShaderColor(1F, 1F, 1F, 0.25F * alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.pushPose();
		ms.mulPose(Vector3f.ZP.rotationDegrees(t));
		RenderSystem.setShaderColor(0.5F, 1F, 1F, 0.25F * alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.pushPose();
		ms.mulPose(Vector3f.ZP.rotationDegrees(t * 2));
		RenderSystem.setShaderColor(1F, 0.75F, 0.75F, 0.25F * alpha);
		starVBO.drawWithShader(ms.last().pose(), projMat, shader);
		ms.popPose();

		ms.popPose();
		resetFog.run();
	}

}
