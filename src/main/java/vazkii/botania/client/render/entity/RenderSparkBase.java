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

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
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

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		T tEntity = (T) par1Entity;
		IIcon iicon = getBaseIcon(tEntity);

		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.05F);

		double time = ClientTickHandler.ticksInGame + par9;
		time += new Random(par1Entity.getEntityId()).nextInt();
		float a = 0.1F + (1 - par1Entity.getDataWatcher().getWatchableObjectInt(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY)) * 0.8F;

		GL11.glColor4f(1F, 1F, 1F, (0.7F + 0.3F * (float) (Math.sin(time / 5.0) + 0.5) * 2) * a);

		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		GL11.glScalef(scale, scale, scale);
		bindEntityTexture(par1Entity);
		Tessellator tessellator = Tessellator.instance;

		GL11.glPushMatrix();
		float r = 180.0F - renderManager.playerViewY;
		GL11.glRotatef(r, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1F, 0F, 0F);
		func_77026_a(tessellator, iicon);

		IIcon spinningIcon = getSpinningIcon(tEntity);
		if(spinningIcon != null) {
			GL11.glTranslatef(-0.02F + (float) Math.sin(time / 20) * 0.2F, 0.24F + (float) Math.cos(time / 20) * 0.2F, 0.005F);
			GL11.glScalef(0.2F, 0.2F, 0.2F);
			colorSpinningIcon(tEntity, a);
			func_77026_a(tessellator, spinningIcon);
		}
		GL11.glPopMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		renderCallback(tEntity, par9);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
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
		return TextureMap.locationItemsTexture;
	}

	private void func_77026_a(Tessellator p_77026_1_, IIcon p_77026_2_) {
		float f = p_77026_2_.getMinU();
		float f1 = p_77026_2_.getMaxU();
		float f2 = p_77026_2_.getMinV();
		float f3 = p_77026_2_.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		p_77026_1_.startDrawingQuads();
		p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
		p_77026_1_.setBrightness(240);
		p_77026_1_.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
		p_77026_1_.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
		p_77026_1_.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
		p_77026_1_.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
		p_77026_1_.draw();

	}

}
