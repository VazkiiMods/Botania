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

import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.item.ItemSpark;

public class RenderSparkBase<T extends Entity> extends RenderEntity {

	public RenderSparkBase(RenderManager p_i46185_1_) {
		super(p_i46185_1_);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		T tEntity = (T) par1Entity;
		IIcon iicon = getBaseIcon(tEntity);

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)par2, (float)par4, (float)par6);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + par9;
		time += new Random(par1Entity.getEntityId()).nextInt();

		float a = 0.1F + (1 - par1Entity.getDataWatcher().getWatchableObjectInt(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY)) * 0.8F;

		GlStateManager.color(1F, 1F, 1F, (0.7F + 0.3F * (float) (Math.sin(time / 5.0) + 0.5) * 2) * a);

		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		GlStateManager.scale(scale, scale, scale);
		bindEntityTexture(par1Entity);
		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.pushMatrix();
		float r = 180.0F - renderManager.playerViewY;
		GlStateManager.rotate(r, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewX, 1F, 0F, 0F);
		func_77026_a(tessellator, iicon);

		IIcon spinningIcon = getSpinningIcon(tEntity);
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

	public IIcon getBaseIcon(T entity) {
		return ItemSpark.worldIcon;
	}

	public void colorSpinningIcon(T entity, float a) {
		// NO-OP
	}

	public IIcon getSpinningIcon(T entity) {
		return null;
	}

	public void renderCallback(T entity, float pticks) {
		// NO-OP
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return TextureMap.locationBlocksTexture;
	}

	private void func_77026_a(Tessellator p_77026_1_, TextureAtlasSprite p_77026_2_) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		p_77026_1_.getWorldRenderer().startDrawingQuads();
		p_77026_1_.getWorldRenderer().setNormal(0.0F, 1.0F, 0.0F);
		p_77026_1_.getWorldRenderer().setBrightness(240);
		p_77026_1_.getWorldRenderer().addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
		p_77026_1_.getWorldRenderer().addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
		p_77026_1_.getWorldRenderer().addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
		p_77026_1_.getWorldRenderer().addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
		p_77026_1_.draw();

	}

}
