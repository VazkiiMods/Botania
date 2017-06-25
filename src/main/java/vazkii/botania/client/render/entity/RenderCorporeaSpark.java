/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 14, 2015, 1:04:34 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	public RenderCorporeaSpark(RenderManager manager) {
		super(manager);
	}

	@Override
	public TextureAtlasSprite getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? MiscellaneousIcons.INSTANCE.corporeaWorldIconMaster : MiscellaneousIcons.INSTANCE.corporeaWorldIcon;
	}

	@Override
	public void colorSpinningIcon(EntityCorporeaSpark entity, float a) {
		int hex = entity.getNetwork().getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;
		GlStateManager.color(r / 255F, g / 255F, b / 255F, a);
	}

	@Override
	public TextureAtlasSprite getSpinningIcon(EntityCorporeaSpark entity) {
		return MiscellaneousIcons.INSTANCE.corporeaIconStar;
	}

	@Override
	public void renderCallback(EntityCorporeaSpark entity, float pticks) {
		int time = entity.getItemDisplayTicks();
		if(time == 0)
			return;

		float absTime = Math.abs(time) - pticks;

		ItemStack stack = entity.getDisplayedItem();
		if(stack.isEmpty())
			return;

		GlStateManager.pushMatrix();
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		float scalef = 1F / 6F;
		GlStateManager.scale(scalef, scalef, scalef);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, absTime / 10);
		GlStateManager.translate(0F, 0F, -2F + (time < 0 ? -absTime : absTime) / 6);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite icon = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, entity.world, null).getParticleTexture();

		if(icon != null) {
			float minU = icon.getMinU();
			float maxU = icon.getMaxU();
			float minV = icon.getMinV();
			float maxV = icon.getMaxV();

			int pieces = 8;
			float stepU = (maxU - minU) / pieces;
			float stepV = (maxV - minV) / pieces;
			float gap = 1F + (time > 0 ? 10F - absTime : absTime) * 0.2F;
			int shift = pieces / 2;

			float scale = 1F / pieces * 3F;
			GlStateManager.scale(scale, scale, 1F);
			for(int i = -shift; i < shift; i++) {
				GlStateManager.translate(gap * i, 0F, 0F);
				for(int j = -shift; j < shift; j++) {
					GlStateManager.translate(0F, gap * j, 0F);
					IconHelper.renderIconIn3D(Tessellator.getInstance(), minU + stepU * (i + shift), minV + stepV * (j + shift + 1), minU + stepU * (i + shift + 1), minV + stepV * (j + shift), icon.getIconWidth() / pieces, icon.getIconHeight() / pieces, 1F / 8F);
					GlStateManager.translate(0F, -gap * j, 0F);
				}
				GlStateManager.translate(-gap * i, 0F, 0F);
			}
		}

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

}
