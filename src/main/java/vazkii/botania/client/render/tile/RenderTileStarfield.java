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

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.tile.TileStarfield;

// This is copied from the vanilla end portal TESR, relevant edits are commented
public class RenderTileStarfield extends TileEntitySpecialRenderer<TileStarfield> {

	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random field_147527_e = new Random(31100L);
	final FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

	@Override
	public void renderTileEntityAt(@Nonnull TileStarfield starfield, double x, double y, double z, float partialTicks, int destroyStage)
	{
		float f = (float)rendererDispatcher.entityX;
		float f1 = (float)rendererDispatcher.entityY;
		float f2 = (float)rendererDispatcher.entityZ;
		GlStateManager.disableLighting();
		field_147527_e.setSeed(31100L);
		float f3 = 0.24F; // Botania: move to bottom of block space

		for (int i = 0; i < 16; ++i)
		{
			GlStateManager.pushMatrix();
			float f4 = 16 - i;
			float f5 = 0.0625F;
			float f6 = 1.0F / (f4 + 1.0F);

			if (i == 0)
			{
				bindTexture(END_SKY_TEXTURE);
				f6 = 0.1F;
				f4 = 65.0F;
				f5 = 0.125F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
			}

			if (i >= 1)
			{
				bindTexture(END_PORTAL_TEXTURE);
			}

			if (i == 1)
			{
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(1, 1);
				f5 = 0.5F;
			}

			float f7 = (float)-(y + f3);
			float f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
			float f9 = f7 + f4 + (float)ActiveRenderInfo.getPosition().yCoord;
			float f10 = f8 / f9;
			f10 = (float)(y + f3) + f10;
			GlStateManager.translate(f, f10, f2);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
			GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9473, func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.T, 9473, func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.R, 9473, func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
			GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.0F, Minecraft.getSystemTime() % 20000 / 20000.0F, 0.0F);
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.rotate((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			GlStateManager.translate(-f, -f2, -f1);
			f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
			GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float)ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer worldrenderer = tessellator.getBuffer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			float f11 = (field_147527_e.nextFloat() * 0.5F + 0.1F) * f6;
			float f12 = (field_147527_e.nextFloat() * 0.5F + 0.4F) * f6;
			float f13 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;

			if (i == 0)
			{
				f11 = f12 = 1.0F * f6;
			}

			// Botania: change color based on time
			Color color = Color.getHSBColor(Minecraft.getSystemTime() / 20F % 360 / 360F, 1F, 1F);
			f11 = color.getRed() / 255F;
			f12 = color.getGreen() / 255F;
			f13 = color.getBlue() / 255F;

			f11 *= f6; f12 *= f6; f13 *= f6;

			worldrenderer.pos(x, y + f3, z).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x, y + f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x + 1.0D, y + f3, z + 1.0D).color(f11, f12, f13, 1.0F).endVertex();
			worldrenderer.pos(x + 1.0D, y + f3, z).color(f11, f12, f13, 1.0F).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			bindTexture(END_SKY_TEXTURE);
		}

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
		GlStateManager.enableLighting();
	}

	private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
	{
		field_147528_b.clear();
		field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		field_147528_b.flip();
		return field_147528_b;
	}
}
