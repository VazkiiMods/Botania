/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 27, 2015, 10:02:12 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.entity.EntityThornChakram;

// Basically a bit of an extension of RenderSnowball
public class RenderThornChakram extends Render<EntityThornChakram> {

	public RenderThornChakram(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityThornChakram c, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		boolean fire = c.isFire();
		TextureAtlasSprite iicon = null;//ModItems.thornChakram.getIconFromDamage(fire ? 1 : 0);

		if(iicon != null)  {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			bindEntityTexture(c);
			Tessellator tessellator = Tessellator.getInstance();

			func_77026_a(tessellator, iicon, fire ? 240 : -1);
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityThornChakram p_110775_1_) {
		return TextureMap.locationBlocksTexture;
	}

	private void func_77026_a(Tessellator p_77026_1_, TextureAtlasSprite p_77026_2_, int light) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		p_77026_1_.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		if(light != -1)
			//p_77026_1_.getWorldRenderer().setBrightness(light);
		p_77026_1_.getWorldRenderer().pos(0.0F - f5, 0.0F - f6, 0.0D).tex(f, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getWorldRenderer().pos(f4 - f5, 0.0F - f6, 0.0D).tex(f1, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getWorldRenderer().pos(f4 - f5, f4 - f6, 0.0D).tex(f1, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getWorldRenderer().pos(0.0F - f5, f4 - f6, 0.0D).tex(f, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.draw();
	}

}
