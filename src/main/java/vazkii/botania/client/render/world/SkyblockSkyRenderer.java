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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;

import java.util.Random;

public class SkyblockSkyRenderer extends IRenderHandler {

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

	// Copy of overworld section of RenderGlobal.renderSky(), heavily modified
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		// Environment setup
		int glSkyList = mc.renderGlobal.glSkyList;
		net.minecraft.client.renderer.vertex.VertexBuffer skyVBO = mc.renderGlobal.skyVBO;

		// Begin
		GlStateManager.disableTexture2D();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f = (float)vec3d.x;
		float f1 = (float)vec3d.y;
		float f2 = (float)vec3d.z;

		// Botania - darken when in void
		float insideVoid = 0;
		if(mc.player.posY <= -2)
			insideVoid = (float) Math.min(1F, -(mc.player.posY + 2) / 30F);

		f = Math.max(0F, f - insideVoid);
		f1 = Math.max(0F, f1 - insideVoid);
		f2 = Math.max(0F, f2 - insideVoid);

		/*if (pass != 2) Botania - no anaglyph stuff
		{
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}*/

		GlStateManager.color(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder BufferBuilder = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f, f1, f2);

		if (OpenGlHelper.useVbo())
		{
			skyVBO.bindBuffer();
			GlStateManager.glEnableClientState(32884);
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			skyVBO.drawArrays(7);
			skyVBO.unbindBuffer();
			GlStateManager.glDisableClientState(32884);
		}
		else
		{
			GlStateManager.callList(glSkyList);
		}

		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

		if (afloat != null)
		{
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			float f6 = afloat[0];
			float f7 = afloat[1];
			float f8 = afloat[2];

			/*if (pass != 2) Botania - no anaglyph stuff
			{
				float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
				float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
				float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
				f6 = f9;
				f7 = f10;
				f8 = f11;
			}*/

			BufferBuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			BufferBuilder.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3] * (1F - insideVoid)).endVertex(); // Botania - darken in void
			for (int l = 0; l <= 16; ++l)
			{
				float f21 = l * ((float)Math.PI * 2F) / 16.0F;
				float f12 = MathHelper.sin(f21);
				float f13 = MathHelper.cos(f21);
				BufferBuilder.pos(f12 * 120.0F, f13 * 120.0F, -f13 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
			}

			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(7424);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);

		float f17; // Botania - move declaration up from below "extra stuff"

		// Botania - Begin extra stuff
		float celAng = world.getCelestialAngle(partialTicks);
		float effCelAng = celAng;
		if(celAng > 0.5)
			effCelAng = 0.5F - (celAng - 0.5F);

		// === Planets
		f17 = 20F;
		float lowA = Math.max(0F, effCelAng - 0.3F) * f16;
		float a = Math.max(0.1F, lowA);

		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, a * 4 * (1F - insideVoid));
		GlStateManager.rotate(90F, 0.5F, 0.5F, 0.0F);
		for(int p = 0; p < planetTextures.length; p++) {
			mc.renderEngine.bindTexture(planetTextures[p]);
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			tessellator.getBuffer().pos(-f17, 100.0D, -f17).tex(0.0D, 0.0D).endVertex();
			tessellator.getBuffer().pos(f17, 100.0D, -f17).tex(1.0D, 0.0D).endVertex();
			tessellator.getBuffer().pos(f17, 100.0D, f17).tex(1.0D, 1.0D).endVertex();
			tessellator.getBuffer().pos(-f17, 100.0D, f17).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();

			switch(p) {
			case 0:
				GlStateManager.rotate(70F, 1F, 0F, 0F);
				f17 = 12F;
				break;
			case 1:
				GlStateManager.rotate(120F, 0F, 0F, 1F);
				f17 = 15F;
				break;
			case 2:
				GlStateManager.rotate(80F, 1F, 0F, 1F);
				f17 = 25F;
				break;
			case 3:
				GlStateManager.rotate(100F, 0F, 0F, 1F);
				f17 = 10F;
				break;
			case 4:
				GlStateManager.rotate(-60F, 1F, 0F, 0.5F);
				f17 = 40F;
			}
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();

		// === Rays
		mc.renderEngine.bindTexture(textureSkybox);

		f17 = 20F;
		a = lowA;
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		GlStateManager.translate(0F, -1F, 0F);
		GlStateManager.rotate(220F, 1F, 0F, 0F);
		GlStateManager.color(1F, 1F, 1F, a);
		int angles = 90;
		float y = 2F;
		float y0 = 0F;
		float uPer = 1F / 360F;
		float anglePer = 360F / angles;
		double fuzzPer = Math.PI * 10 / angles;
		float rotSpeed = 1F;
		float rotSpeedMod = 0.4F;

		for(int p = 0; p < 3; p++) {
			float baseAngle = rotSpeed * rotSpeedMod * (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks);
			GlStateManager.rotate((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.25F * rotSpeed * rotSpeedMod, 0F, 1F, 0F);

			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			for(int i = 0; i < angles; i++) {
				int j = i;
				if(i % 2 == 0)
					j--;

				float ang = j * anglePer + baseAngle;
				double xp = Math.cos(ang * Math.PI / 180F) * f17;
				double zp = Math.sin(ang * Math.PI / 180F) * f17;
				double yo = Math.sin(fuzzPer * j) * 1;

				float ut = ang * uPer;
				if(i % 2 == 0) {
					tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
					tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
				} else {
					tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
					tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				}

			}
			tessellator.draw();

			switch(p) {
			case 0:
				GlStateManager.rotate(20F, 1F, 0F, 0F);
				GlStateManager.color(1F, 0.4F, 0.4F, a);
				fuzzPer = Math.PI * 14 / angles;
				rotSpeed = 0.2F;
				break;
			case 1:
				GlStateManager.rotate(50F, 1F, 0F, 0F);
				GlStateManager.color(0.4F, 1F, 0.7F, a);
				fuzzPer = Math.PI * 6 / angles;
				rotSpeed = 2F;
				break;
			}
		}
		GlStateManager.popMatrix();

		// === Rainbow
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		mc.renderEngine.bindTexture(textureRainbow);
		f17 = 10F;
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

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		for(int i = 0; i < angles; i++) {
			int j = i;
			if(i % 2 == 0)
				j--;

			float ang = j * anglePer;
			double xp = Math.cos(ang * Math.PI / 180F) * f17;
			double zp = Math.sin(ang * Math.PI / 180F) * f17;
			double yo = 0;

			float ut = ang * uPer;
			if(i % 2 == 0) {
				tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
				tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
			} else {
				tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
				tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
			}

		}
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F - insideVoid);
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		// Botania - End extra stuff


		GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		/*float*/ f17 = 60.0F; // Botania - 30 -> 60 and move declaration above "extra stuff"
		mc.renderEngine.bindTexture(SUN_TEXTURES);
		BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		BufferBuilder.pos(-f17, 100.0D, -f17).tex(0.0D, 0.0D).endVertex();
		BufferBuilder.pos(f17, 100.0D, -f17).tex(1.0D, 0.0D).endVertex();
		BufferBuilder.pos(f17, 100.0D, f17).tex(1.0D, 1.0D).endVertex();
		BufferBuilder.pos(-f17, 100.0D, f17).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		f17 = 60.0F; // Botania - 20 -> 60
		mc.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
		int i = world.getMoonPhase();
		int k = i % 4;
		int i1 = i / 4 % 2;
		float f22 = (k + 0) / 4.0F;
		float f23 = (i1 + 0) / 2.0F;
		float f24 = (k + 1) / 4.0F;
		float f14 = (i1 + 1) / 2.0F;
		BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		BufferBuilder.pos(-f17, -100.0D, f17).tex(f24, f14).endVertex();
		BufferBuilder.pos(f17, -100.0D, f17).tex(f22, f14).endVertex();
		BufferBuilder.pos(f17, -100.0D, -f17).tex(f22, f23).endVertex();
		BufferBuilder.pos(-f17, -100.0D, -f17).tex(f24, f23).endVertex();
		tessellator.draw();
		GlStateManager.disableTexture2D();
		// Botania - Custom star rendering
		{
			f16 *= Math.max(0.1F, effCelAng * 2);
			renderStars(mc, f16, partialTicks);
		}
		/*float f15 = this.world.getStarBrightness(partialTicks) * f16;

		if (f15 > 0.0F)
		{
			GlStateManager.color(f15, f15, f15, f15);

			if (this.vboEnabled)
			{
				this.starVBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				this.starVBO.drawArrays(7);
				this.starVBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			}
			else
			{
				GlStateManager.callList(this.starGLCallList);
			}
		}*/

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		// Botania - no horizon rendering
		/* GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d0 = this.mc.player.getPositionEyes(partialTicks).y - this.world.getHorizon();

		if (d0 < 0.0D)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 12.0F, 0.0F);

			if (this.vboEnabled)
			{
				this.sky2VBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				this.sky2VBO.drawArrays(7);
				this.sky2VBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			}
			else
			{
				GlStateManager.callList(this.glSkyList2);
			}

			GlStateManager.popMatrix();
			float f18 = 1.0F;
			float f19 = -((float)(d0 + 65.0D));
			float f20 = -1.0F;
			BufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
			BufferBuilder.pos(-1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, (double)f19, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, (double)f19, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
			BufferBuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
		}

		if (this.world.provider.isSkyColored())
		{
			GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
		}
		else
		{
			GlStateManager.color(f, f1, f2);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
		GlStateManager.callList(this.glSkyList2);
		GlStateManager.popMatrix();*/
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}

	private void renderStars(Minecraft mc, float alpha, float partialTicks) {
		int starGLCallList = mc.renderGlobal.starGLCallList;
		net.minecraft.client.renderer.vertex.VertexBuffer starVBO = mc.renderGlobal.starVBO;

		float t = (ClientTickHandler.ticksInGame + partialTicks + 2000) * 0.005F;
		GlStateManager.pushMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 3, 0F, 1F, 0F);
		GlStateManager.color(1F, 1F, 1F, alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t, 0F, 1F, 0F);
		GlStateManager.color(0.5F, 1F, 1F, alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 2, 0F, 1F, 0F);
		GlStateManager.color(1F, 0.75F, 0.75F, alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 3, 0F, 0F, 1F);
		GlStateManager.color(1F, 1F, 1F, 0.25F * alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t, 0F, 0F, 1F);
		GlStateManager.color(0.5F, 1F, 1F, 0.25F * alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(t * 2, 0F, 0F, 1F);
		GlStateManager.color(1F, 0.75F, 0.75F, 0.25F * alpha);
		drawVboOrList(starVBO, starGLCallList);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	// Excised from many occurences in RenderGlobal
	private void drawVboOrList(net.minecraft.client.renderer.vertex.VertexBuffer vbo, int displayList) {
		if (OpenGlHelper.useVbo())
		{
			vbo.bindBuffer();
			GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
			vbo.drawArrays(GL11.GL_QUADS);
			vbo.unbindBuffer();
			GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		else
		{
			GlStateManager.callList(displayList);
		}
	}

}
