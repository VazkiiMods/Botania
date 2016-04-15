/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [18/12/2015, 02:06:56 (GMT)]
 */
package vazkii.botania.client.render.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.Random;

public class SkyblockSkyRenderer extends IRenderHandler {

	private static final ResourceLocation textureSkybox = new ResourceLocation(LibResources.MISC_SKYBOX);
	private static final ResourceLocation textureRainbow = new ResourceLocation(LibResources.MISC_RAINBOW);
	private static final ResourceLocation textureMoonPhases = new ResourceLocation("textures/environment/moon_phases.png");
	private static final ResourceLocation textureSun = new ResourceLocation("textures/environment/sun.png");
	private static final ResourceLocation[] planetTextures = new ResourceLocation[] {
		new ResourceLocation(LibResources.MISC_PLANET + "0.png"),
		new ResourceLocation(LibResources.MISC_PLANET + "1.png"),
		new ResourceLocation(LibResources.MISC_PLANET + "2.png"),
		new ResourceLocation(LibResources.MISC_PLANET + "3.png"),
		new ResourceLocation(LibResources.MISC_PLANET + "4.png"),
		new ResourceLocation(LibResources.MISC_PLANET + "5.png")
	};

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		boolean test = false;
		if(test)
			return;

		int glSkyList = ReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, LibObfuscation.GL_SKY_LIST);
		int glSkyList2 = ReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, LibObfuscation.GL_SKY_LIST2); // Horizon line. We don't have it here
		int starGLCallList = ReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, LibObfuscation.STAR_GL_CALL_LIST);

		GlStateManager.disableTexture2D();
		Vec3d vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f1 = (float)vec3.xCoord;
		float f2 = (float)vec3.yCoord;
		float f3 = (float)vec3.zCoord;
		float f6;

		float insideVoid = 0;
		if(mc.thePlayer.posY <= -2)
			insideVoid = (float) Math.min(1F, -(mc.thePlayer.posY + 2) / 30F);
		
		f1 = Math.max(0F, f1 - insideVoid);
		f2 = Math.max(0F, f2 - insideVoid);
		f3 = Math.max(0F, f3 - insideVoid);

		Tessellator tessellator1 = Tessellator.getInstance();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f1, f2, f3);
		GlStateManager.callList(glSkyList);
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		float f7;
		float f8;
		float f9;
		float f10;

		// === Sunset
		if(afloat != null) {
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			f6 = afloat[0];
			f7 = afloat[1];
			f8 = afloat[2];
			float f11;

			tessellator1.getBuffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			tessellator1.getBuffer().pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3] * (1F - insideVoid)).endVertex();
			byte b0 = 16;

			for(int j = 0; j <= b0; ++j) {
				f11 = (float)j * (float)Math.PI * 2.0F / (float)b0;
				float f12 = MathHelper.sin(f11);
				float f13 = MathHelper.cos(f11);
				tessellator1.getBuffer().pos((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
			}

			tessellator1.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.pushMatrix();
		f6 = Math.max(0.2F, 1.0F - world.getRainStrength(partialTicks)) * (1F - insideVoid);
		f7 = 0.0F;
		f8 = 0.0F;
		f9 = 0.0F;

		GlStateManager.translate(f7, f8, f9);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);

		float celAng = world.getCelestialAngle(partialTicks);
		float effCelAng = celAng;
		if(celAng > 0.5)
			effCelAng = 0.5F - (celAng - 0.5F);

		// === Planets
		f10 = 20F;
		float lowA = Math.max(0F, effCelAng - 0.3F) * f6;
		float a = Math.max(0.1F, lowA);

		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, (a * 4) * (1F - insideVoid));
		GlStateManager.rotate(90F, 0.5F, 0.5F, 0.0F);
		for(int p = 0; p < planetTextures.length; p++) {
			mc.renderEngine.bindTexture(planetTextures[p]);
			drawObject(tessellator1, f10);

			switch(p) {
			case 0:
				GlStateManager.rotate(70F, 1F, 0F, 0F);
				f10 = 12F;
				break;
			case 1: 
				GlStateManager.rotate(120F, 0F, 0F, 1F);
				f10 = 15F;
				break;
			case 2:
				GlStateManager.rotate(80F, 1F, 0F, 1F);
				f10 = 25F;
				break;
			case 3:
				GlStateManager.rotate(100F, 0F, 0F, 1F);
				f10 = 10F;
				break;
			case 4:
				GlStateManager.rotate(-60F, 1F, 0F, 0.5F);
				f10 = 40F;
			}
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();

		// === Rays
		mc.renderEngine.bindTexture(textureSkybox); 

		f10 = 20F;
		a = lowA;
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		GlStateManager.translate(0F, -1F, 0F);
		GlStateManager.rotate(220F, 1F, 0F, 0F);
		GlStateManager.color(1F, 1F, 1F, a);
		int angles = 90;
		float s = 3F;
		float m = 1F;
		float y = 2F;
		float y0 = 0F;
		float uPer = 1F / 360F;
		float anglePer = 360F / angles;
		double fuzzPer = (Math.PI * 10) / angles;
		float rotSpeed = 1F;
		float rotSpeedMod = 0.4F;

		for(int p = 0; p < 3; p++) {
			float baseAngle = rotSpeed * rotSpeedMod * (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks);
			GlStateManager.rotate((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.25F * rotSpeed * rotSpeedMod, 0F, 1F, 0F);

			tessellator1.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			for(int i = 0; i < angles; i++) {
				int j = i;
				if(i % 2 == 0)
					j--;

				float ang = j * anglePer + baseAngle;
				double xp = Math.cos(ang * Math.PI / 180F) * f10;
				double zp = Math.sin(ang * Math.PI / 180F) * f10;
				double yo = Math.sin(fuzzPer * j) * 1;

				float ut = ang * uPer;
				if(i % 2 == 0) {
					tessellator1.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
					tessellator1.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
				} else {
					tessellator1.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
					tessellator1.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				}

			}
			tessellator1.draw();

			switch(p) {
			case 0:
				GlStateManager.rotate(20F, 1F, 0F, 0F);
				GlStateManager.color(1F, 0.4F, 0.4F, a);
				fuzzPer = (Math.PI * 14) / angles;
				rotSpeed = 0.2F;
				break;
			case 1:
				GlStateManager.rotate(50F, 1F, 0F, 0F);
				GlStateManager.color(0.4F, 1F, 0.7F, a);
				fuzzPer = (Math.PI * 6) / angles;
				rotSpeed = 2F;
				break;
			}
		}
		GlStateManager.popMatrix();

		// === Rainbow
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		mc.renderEngine.bindTexture(textureRainbow); 
		f10 = 10F;
		float effCelAng1 = celAng;
		if(effCelAng1 > 0.25F)
			effCelAng1 = 1F - effCelAng1;
		effCelAng1 = 0.25F - Math.min(0.25F, effCelAng1);

		long time = world.getWorldTime() + 1000;
		int day = (int) (time / 24000L);
		Random rand = new Random(day * 0xFF);
		float angle1 = rand.nextFloat() * 360F;
		float angle2 = rand.nextFloat() * 360F;
		GlStateManager.color(1F, 1F, 1F, effCelAng1 * (1F - insideVoid));
		GlStateManager.rotate(angle1, 0F, 1F, 0F);
		GlStateManager.rotate(angle2, 0F, 0F, 1F);

		tessellator1.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		for(int i = 0; i < angles; i++) {
			int j = i;
			if(i % 2 == 0)
				j--;

			float ang = j * anglePer;
			double xp = Math.cos(ang * Math.PI / 180F) * f10;
			double zp = Math.sin(ang * Math.PI / 180F) * f10;
			double yo = 0;

			float ut = ang * uPer;
			if(i % 2 == 0) {
				tessellator1.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				tessellator1.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
			} else {
				tessellator1.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
				tessellator1.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
			}

		}
		tessellator1.draw();
		GlStateManager.popMatrix();

		GlStateManager.color(1F, 1F, 1F, 1F - insideVoid);

		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);

		// === Sun	
		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		f10 = 60.0F;
		mc.renderEngine.bindTexture(textureSun); 
		drawObject(tessellator1, f10);

		// === Moon
		f10 = 60.0F;
		mc.renderEngine.bindTexture(textureMoonPhases);
		int k = world.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f14 = (float)(l + 0) / 4.0F;
		float f15 = (float)(i1 + 0) / 2.0F;
		float f16 = (float)(l + 1) / 4.0F;
		float f17 = (float)(i1 + 1) / 2.0F;
		tessellator1.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator1.getBuffer().pos((double)(-f10), -100.0D, (double)f10).tex((double)f16, (double)f17).endVertex();
		tessellator1.getBuffer().pos((double)f10, -100.0D, (double)f10).tex((double)f14, (double)f17).endVertex();
		tessellator1.getBuffer().pos((double)f10, -100.0D, (double)(-f10)).tex((double)f14, (double)f15).endVertex();
		tessellator1.getBuffer().pos((double)(-f10), -100.0D, (double)(-f10)).tex((double)f16, (double)f15).endVertex();
		tessellator1.draw();

		// === Stars
		f6 *= Math.max(0.1F, effCelAng * 2);
		float t = (ClientTickHandler.ticksInGame + partialTicks + 2000) * 0.005F;
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 3, 0F, 1F, 0F);
		GlStateManager.color(1F, 1F, 1F, f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t, 0F, 1F, 0F);
		GlStateManager.color(0.5F, 1F, 1F, f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 2, 0F, 1F, 0F);
		GlStateManager.color(1F, 0.75F, 0.75F, f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 3, 0F, 0F, 1F);
		GlStateManager.color(1F, 1F, 1F, 0.25F * f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t, 0F, 0F, 1F);
		GlStateManager.color(0.5F, 1F, 1F, 0.25F * f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 2, 0F, 0F, 1F);
		GlStateManager.color(1F, 0.75F, 0.75F, 0.25F * f6);
		GlStateManager.callList(starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
	}

	private void drawObject(Tessellator tess, float f10) {
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tess.getBuffer().pos((double)(-f10), 100.0D, (double)(-f10)).tex(0.0D, 0.0D).endVertex();
		tess.getBuffer().pos((double)(f10), 100.0D, (double)(-f10)).tex(1.0D, 0.0D).endVertex();
		tess.getBuffer().pos((double)(f10), 100.0D, (double)(f10)).tex(1.0D, 1.0D).endVertex();
		tess.getBuffer().pos((double)(-f10), 100.0D, (double)(f10)).tex(0.0D, 1.0D).endVertex();
		tess.draw();
	}

}
