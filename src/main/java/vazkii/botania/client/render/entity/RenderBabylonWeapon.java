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

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityBabylonWeapon;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderBabylonWeapon extends EntityRenderer<EntityBabylonWeapon> {

	private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON);

	public RenderBabylonWeapon(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityBabylonWeapon weapon, double par2, double par4, double par6, float par8, float par9) {
		GlStateManager.pushMatrix();
		GlStateManager.translated(par2, par4, par6);
		GlStateManager.rotatef(weapon.getRotation(), 0F, 1F, 0F);

		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + par9);
		float chargeMul = charge / 10F;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		float s = 1.5F;
		GlStateManager.scalef(s, s, s);
		GlStateManager.rotatef(-90F, 0F, 1F, 0F);
		GlStateManager.rotatef(45F, 0F, 0F, 1F);
		TextureAtlasSprite icon = MiscellaneousIcons.INSTANCE.kingKeyWeaponIcons[weapon.getVariety()];
		GlStateManager.color4f(1F, 1F, 1F, chargeMul);
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();

		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240, 240);
		GlStateManager.disableLighting();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getWidth(), icon.getHeight(), 1F / 16F);
		GlStateManager.popMatrix();

		GlStateManager.disableCull();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.color4f(1F, 1F, 1F, chargeMul);

		Minecraft.getInstance().textureManager.bindTexture(babylon);

		Tessellator tes = Tessellator.getInstance();
		ShaderHelper.useShader(ShaderHelper.halo);
		Random rand = new Random(weapon.getUniqueID().getMostSignificantBits());
		GlStateManager.rotatef(-90F, 1F, 0F, 0F);
		GlStateManager.translatef(0F, -0.3F + rand.nextFloat() * 0.1F, 1F);

		s = chargeMul;
		if(live > delay)
			s -= Math.min(1F, (live - delay + par9) * 0.2F);
		s *= 2F;
		GlStateManager.scalef(s, s, s);

		GlStateManager.rotatef(charge * 9F + (weapon.ticksExisted + par9) * 0.5F + rand.nextFloat() * 360F, 0F, 1F, 0F);

		tes.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tes.getBuffer().pos(-1, 0, -1).tex(0, 0).endVertex();
		tes.getBuffer().pos(-1, 0, 1).tex(0, 1).endVertex();
		tes.getBuffer().pos(1, 0, 1).tex(1, 1).endVertex();
		tes.getBuffer().pos(1, 0, -1).tex(1, 0).endVertex();
		tes.draw();

		ShaderHelper.releaseShader();

		GlStateManager.enableLighting();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityBabylonWeapon entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
