/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.entity.EntityCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	public RenderCorporeaSpark(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public TextureAtlasSprite getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? MiscellaneousIcons.INSTANCE.corporeaWorldIconMaster : MiscellaneousIcons.INSTANCE.corporeaWorldIcon;
	}

	@Override
	public void renderCallback(EntityCorporeaSpark entity, float pticks, MatrixStack ms, IRenderTypeBuffer buffers) {
		int time = entity.getItemDisplayTicks();
		if (time == 0) {
			return;
		}

		float absTime = Math.abs(time) - pticks;

		ItemStack stack = entity.getDisplayedItem();
		if (stack.isEmpty()) {
			return;
		}

		ms.push();
		ms.rotate(Vector3f.XP.rotationDegrees(90));
		float scalef = 1F / 6F;
		ms.scale(scalef, scalef, scalef);
		//todo 1.15 GlStateManager.color4f(1F, 1F, 1F, absTime / 10);
		ms.translate(0F, 0F, -2F + (time < 0 ? -absTime : absTime) / 6);

		TextureAtlasSprite icon = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, null).getParticleTexture();

		if (icon != null) {
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
			ms.scale(scale, scale, 1F);
			for (int i = -shift; i < shift; i++) {
				ms.translate(gap * i, 0F, 0F);
				for (int j = -shift; j < shift; j++) {
					ms.translate(0F, gap * j, 0F);
					// todo 1.15 do this another way IconHelper.renderIconIn3D(Tessellator.getInstance(), minU + stepU * (i + shift), minV + stepV * (j + shift + 1), minU + stepU * (i + shift + 1), minV + stepV * (j + shift), icon.getWidth() / pieces, icon.getHeight() / pieces, 1F / 8F);
					ms.translate(0F, -gap * j, 0F);
				}
				ms.translate(-gap * i, 0F, 0F);
			}
		}

		ms.pop();
	}

}
