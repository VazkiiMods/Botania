/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 8, 2014, 2:38:15 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

// THIS CODE WAS STOLEN FROM THE END PORTAL
// OH GOD
// I HAVE NO IDEA WHAT I'M DOING
// SO MUCH STUFF I DON'T UNDERSTAND D:
// HELP
public class RenderTileStarfield extends TileEntitySpecialRenderer {

	private static final ResourceLocation field_147529_c = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation field_147526_d = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random field_147527_e = new Random(31100L);
	FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
		float f1 = (float)field_147501_a.field_147560_j;
		float f2 = (float)field_147501_a.field_147561_k;
		float f3 = (float)field_147501_a.field_147558_l;
		GL11.glDisable(GL11.GL_LIGHTING);
		field_147527_e.setSeed(31100L);
		float f4 = 0.24F;

		for(int i = 0; i < 16; ++i) {
			GL11.glPushMatrix();
			float f5 = 16 - i;
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);

			if(i == 0) {
				bindTexture(field_147529_c);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			if(i == 1) {
				bindTexture(field_147526_d);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				f6 = 0.5F;
			}

			float f8 = (float)-(p_147500_4_ + f4);
			float f9 = f8 + ActiveRenderInfo.objectY;
			float f10 = f8 + f5 + ActiveRenderInfo.objectY;
			float f11 = f9 / f10;
			f11 += (float)(p_147500_4_ + f4);
			GL11.glTranslatef(f1, f11, f3);
			GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
			GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
			GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
			GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
			GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
			GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
			GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, Minecraft.getSystemTime() % 20000L / 20000.0F, 0.0F);
			GL11.glScalef(f6, f6, f6);
			GL11.glTranslatef(0.5F, 0.5F, 0.0F);
			GL11.glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
			GL11.glTranslatef(-f1, -f3, -f2);
			f9 = f8 + ActiveRenderInfo.objectY;
			GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();

			Color color = Color.getHSBColor(Minecraft.getSystemTime() / 20F % 360 / 360F, 1F, 1F);
			f11 = color.getRed() / 255F;
			float f12 = color.getGreen() / 255F;
			float f13 = color.getBlue() / 255F;

			tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
			tessellator.addVertex(p_147500_2_, p_147500_4_ + f4, p_147500_6_);
			tessellator.addVertex(p_147500_2_, p_147500_4_ + f4, p_147500_6_ + 1.0D);
			tessellator.addVertex(p_147500_2_ + 1.0D, p_147500_4_ + f4, p_147500_6_ + 1.0D);
			tessellator.addVertex(p_147500_2_ + 1.0D, p_147500_4_ + f4, p_147500_6_);
			tessellator.draw();
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
		field_147528_b.clear();
		field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		field_147528_b.flip();
		return field_147528_b;
	}
}
