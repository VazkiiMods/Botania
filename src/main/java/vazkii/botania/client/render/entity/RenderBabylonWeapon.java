/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 16, 2015, 6:42:09 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.item.relic.ItemKingKey;

public class RenderBabylonWeapon extends Render {

	private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON);

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		EntityBabylonWeapon weapon = (EntityBabylonWeapon) par1Entity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glRotatef(weapon.getRotation(), 0F, 1F, 0F);

		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + par9);
		float chargeMul = charge / 10F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
		GL11.glPushMatrix();
		float s = 1.5F;
		GL11.glScalef(s, s, s);
		GL11.glRotatef(-90F, 0F, 1F, 0F);
		GL11.glRotatef(45F, 0F, 0F, 1F);
		IIcon icon = ItemKingKey.weaponIcons[weapon.getVariety()];
		GL11.glColor4f(1F, 1F, 1F, chargeMul);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GL11.glDisable(GL11.GL_LIGHTING);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glColor4f(1F, 1F, 1F, chargeMul);

		Minecraft.getMinecraft().renderEngine.bindTexture(babylon);

		Tessellator tes = Tessellator.instance;
		ShaderHelper.useShader(ShaderHelper.halo);
		Random rand = new Random(weapon.getUniqueID().getMostSignificantBits());
		GL11.glRotatef(-90F, 1F, 0F, 0F);
		GL11.glTranslatef(0F, -0.3F + rand.nextFloat() * 0.1F, 1F);

		s = chargeMul;
		if(live > delay)
			s -= Math.min(1F, (live - delay + par9) * 0.2F);
		s *= 2F;
		GL11.glScalef(s, s, s);

		GL11.glRotatef(charge * 9F + (weapon.ticksExisted + par9) * 0.5F + rand.nextFloat() * 360F, 0F, 1F, 0F);

		tes.startDrawingQuads();
		tes.addVertexWithUV(-1, 0, -1, 0, 0);
		tes.addVertexWithUV(-1, 0, 1, 0, 1);
		tes.addVertexWithUV(1, 0, 1, 1, 1);
		tes.addVertexWithUV(1, 0, -1, 1, 0);
		tes.draw();

		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}

}
