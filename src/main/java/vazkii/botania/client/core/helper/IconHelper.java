/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 5:28:21 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.lib.LibResources;

public final class IconHelper {

	public static TextureAtlasSprite forName(TextureMap ir, String name) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD + name));
	}

	public static TextureAtlasSprite forName(TextureMap ir, String name, String dir) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD + dir + "/" + name));
	}

	/**
	 * Renders a sprite from the spritesheet with depth, like a "builtin/generated" item model.
	 * Adapted from ItemRenderer.renderItemIn2D, 1.7.10
	 */
	public static void renderIconIn3D(Tessellator tess, float p_78439_1_, float p_78439_2_, float p_78439_3_, float p_78439_4_, int width, int height, float thickness) {
		BufferBuilder wr = tess.getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		wr.pos(0.0D, 0.0D, 0.0D).tex(p_78439_1_, p_78439_4_).normal(0, 0, 1).endVertex();
		wr.pos(1.0D, 0.0D, 0.0D).tex(p_78439_3_, p_78439_4_).normal(0, 0, 1).endVertex();
		wr.pos(1.0D, 1.0D, 0.0D).tex(p_78439_3_, p_78439_2_).normal(0, 0, 1).endVertex();
		wr.pos(0.0D, 1.0D, 0.0D).tex(p_78439_1_, p_78439_2_).normal(0, 0, 1).endVertex();
		tess.draw();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		wr.pos(0.0D, 1.0D, 0.0F - thickness).tex(p_78439_1_, p_78439_2_).normal(0, 0, -1).endVertex();
		wr.pos(1.0D, 1.0D, 0.0F - thickness).tex(p_78439_3_, p_78439_2_).normal(0, 0, -1).endVertex();
		wr.pos(1.0D, 0.0D, 0.0F - thickness).tex(p_78439_3_, p_78439_4_).normal(0, 0, -1).endVertex();
		wr.pos(0.0D, 0.0D, 0.0F - thickness).tex(p_78439_1_, p_78439_4_).normal(0, 0, -1).endVertex();
		tess.draw();
		float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / width;
		float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / height;
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		int k;
		float f7;
		float f8;

		for (k = 0; k < width; ++k)
		{
			f7 = (float)k / (float)width;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			wr.pos(f7, 0.0D, 0.0F - thickness).tex(f8, p_78439_4_).normal(-1, 0, 0).endVertex();
			wr.pos(f7, 0.0D, 0.0D).tex(f8, p_78439_4_).normal(-1, 0, 0).endVertex();
			wr.pos(f7, 1.0D, 0.0D).tex(f8, p_78439_2_).normal(-1, 0, 0).endVertex();
			wr.pos(f7, 1.0D, 0.0F - thickness).tex(f8, p_78439_2_).normal(-1, 0, 0).endVertex();
		}

		tess.draw();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		float f9;

		for (k = 0; k < width; ++k)
		{
			f7 = (float)k / (float)width;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			f9 = f7 + 1.0F / width;
			wr.pos(f9, 1.0D, 0.0F - thickness).tex(f8, p_78439_2_).normal(1, 0, 0).endVertex();
			wr.pos(f9, 1.0D, 0.0D).tex(f8, p_78439_2_).normal(1, 0, 0).endVertex();
			wr.pos(f9, 0.0D, 0.0D).tex(f8, p_78439_4_).normal(1, 0, 0).endVertex();
			wr.pos(f9, 0.0D, 0.0F - thickness).tex(f8, p_78439_4_).normal(1, 0, 0).endVertex();
		}

		tess.draw();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

		for (k = 0; k < height; ++k)
		{
			f7 = (float)k / (float)height;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			f9 = f7 + 1.0F / height;
			wr.pos(0.0D, f9, 0.0D).tex(p_78439_1_, f8).normal(0, 1, 0).endVertex();
			wr.pos(1.0D, f9, 0.0D).tex(p_78439_3_, f8).normal(0, 1, 0).endVertex();
			wr.pos(1.0D, f9, 0.0F - thickness).tex(p_78439_3_, f8).normal(0, 1, 0).endVertex();
			wr.pos(0.0D, f9, 0.0F - thickness).tex(p_78439_1_, f8).normal(0, 1, 0).endVertex();
		}

		tess.draw();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

		for (k = 0; k < height; ++k)
		{
			f7 = (float)k / (float)height;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			wr.pos(1.0D, f7, 0.0D).tex(p_78439_3_, f8).normal(0, -1, 0).endVertex();
			wr.pos(0.0D, f7, 0.0D).tex(p_78439_1_, f8).normal(0, -1, 0).endVertex();
			wr.pos(0.0D, f7, 0.0F - thickness).tex(p_78439_1_, f8).normal(0, -1, 0).endVertex();
			wr.pos(1.0D, f7, 0.0F - thickness).tex(p_78439_3_, f8).normal(0, -1, 0).endVertex();
		}

		tess.draw();

	}

}