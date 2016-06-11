/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 5:53:22 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class RenderSparkBase<T extends Entity> extends Render<T> {

	public RenderSparkBase(RenderManager manager) {
		super(manager);
	}

	@Override
	public void doRender(@Nonnull T tEntity, double par2, double par4, double par6, float par8, float par9) {
		TextureAtlasSprite iicon = getBaseIcon(tEntity);

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)par2, (float)par4, (float)par6);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + par9;
		time += new Random(tEntity.getEntityId()).nextInt();

		float a = 0.1F + (1 - tEntity.getDataManager().get(getInvisibilityParam())) * 0.8F;

		GlStateManager.color(1F, 1F, 1F, (0.7F + 0.3F * (float) (Math.sin(time / 5.0) + 0.5) * 2) * a);

		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		GlStateManager.scale(scale, scale, scale);
		bindEntityTexture(tEntity);
		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.pushMatrix();
		float r = 180.0F - renderManager.playerViewY;
		GlStateManager.rotate(r, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewX, 1F, 0F, 0F);
		func_77026_a(tessellator, iicon);

		TextureAtlasSprite spinningIcon = getSpinningIcon(tEntity);
		if(spinningIcon != null) {
			GlStateManager.translate(-0.02F + (float) Math.sin(time / 20) * 0.2F, 0.24F + (float) Math.cos(time / 20) * 0.2F, 0.005F);
			GlStateManager.scale(0.2F, 0.2F, 0.2F);
			colorSpinningIcon(tEntity, a);
			func_77026_a(tessellator, spinningIcon);
		}
		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		renderCallback(tEntity, par9);

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	protected abstract DataParameter<Integer> getInvisibilityParam();

	protected TextureAtlasSprite getBaseIcon(T entity) {
		return MiscellaneousIcons.INSTANCE.sparkWorldIcon;
	}

	protected void colorSpinningIcon(T entity, float a) {
		// NO-OP
	}

	protected TextureAtlasSprite getSpinningIcon(T entity) {
		return null;
	}

	protected void renderCallback(T entity, float pticks) {
		// NO-OP
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	private void func_77026_a(Tessellator p_77026_1_, TextureAtlasSprite p_77026_2_) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		p_77026_1_.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		//p_77026_1_.getBuffer().setBrightness(240);
		p_77026_1_.getBuffer().pos(0.0F - f5, 0.0F - f6, 0.0D).tex(f, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getBuffer().pos(f4 - f5, 0.0F - f6, 0.0D).tex(f1, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getBuffer().pos(f4 - f5, f4 - f6, 0.0D).tex(f1, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.getBuffer().pos(0.0F - f5, f4 - f6, 0.0D).tex(f, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
		p_77026_1_.draw();

	}

}
