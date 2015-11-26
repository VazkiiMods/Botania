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
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
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
	public void renderTileEntityAt(TileEntity p_180544_1_, double p_180544_2_, double p_180544_4_, double p_180544_6_, float p_180544_8_, int p_180544_9_) {
		float f1 = (float)this.rendererDispatcher.entityX;
		float f2 = (float)this.rendererDispatcher.entityY;
		float f3 = (float)this.rendererDispatcher.entityZ;
		GlStateManager.disableLighting();
		field_147527_e.setSeed(31100L);
		float f4 = 0.75F;

		for (int j = 0; j < 16; ++j)
		{
			GlStateManager.pushMatrix();
			float f5 = (float)(16 - j);
			float f6 = 0.0625F;
			float f7 = 1.0F / (f5 + 1.0F);

			if (j == 0)
			{
				this.bindTexture(field_147529_c);
				f7 = 0.1F;
				f5 = 65.0F;
				f6 = 0.125F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
			}

			if (j >= 1)
			{
				this.bindTexture(field_147526_d);
			}

			if (j == 1)
			{
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(1, 1);
				f6 = 0.5F;
			}

			float f8 = (float)(-(p_180544_4_ + (double)f4));
			float f9 = f8 + (float)ActiveRenderInfo.getPosition().yCoord;
			float f10 = f8 + f5 + (float)ActiveRenderInfo.getPosition().yCoord;
			float f11 = f9 / f10;
			f11 += (float)(p_180544_4_ + (double)f4);
			GlStateManager.translate(f1, f11, f3);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
			GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
			GlStateManager.scale(f6, f6, f6);
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.rotate((float)(j * j * 4321 + j * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			GlStateManager.translate(-f1, -f3, -f2);
			f9 = f8 + (float)ActiveRenderInfo.getPosition().yCoord;
			GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f5 / f9, (float)ActiveRenderInfo.getPosition().zCoord * f5 / f9, -f2);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.startDrawingQuads();
			float f12 = field_147527_e.nextFloat() * 0.5F + 0.1F;
			float f13 = field_147527_e.nextFloat() * 0.5F + 0.4F;
			float f14 = field_147527_e.nextFloat() * 0.5F + 0.5F;

			if (j == 0)
			{
				f14 = 1.0F;
				f13 = 1.0F;
				f12 = 1.0F;
			}

			worldrenderer.setColorRGBA_F(f12 * f7, f13 * f7, f14 * f7, 1.0F);
			worldrenderer.addVertex(p_180544_2_, p_180544_4_ + (double)f4, p_180544_6_);
			worldrenderer.addVertex(p_180544_2_, p_180544_4_ + (double)f4, p_180544_6_ + 1.0D);
			worldrenderer.addVertex(p_180544_2_ + 1.0D, p_180544_4_ + (double)f4, p_180544_6_ + 1.0D);
			worldrenderer.addVertex(p_180544_2_ + 1.0D, p_180544_4_ + (double)f4, p_180544_6_);
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
		GlStateManager.enableLighting();
	}

	private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
		field_147528_b.clear();
		field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		field_147528_b.flip();
		return field_147528_b;
	}
}
