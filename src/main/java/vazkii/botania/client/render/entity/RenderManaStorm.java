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

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityManaStorm;

import javax.annotation.Nonnull;

public class RenderManaStorm extends EntityRenderer<EntityManaStorm> {

	public RenderManaStorm(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityManaStorm storm, float yaw, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		ms.push();
		float maxScale = 1.95F;
		float scale = 0.05F + ((float) storm.burstsFired / EntityManaStorm.TOTAL_BURSTS - (storm.deathTime == 0 ? 0 : storm.deathTime + pticks) / EntityManaStorm.DEATH_TIME) * maxScale;
		RenderHelper.renderStar(ms, buffers, 0x00FF00, scale, scale, scale, storm.getUniqueID().getMostSignificantBits());
		ms.pop();
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntityManaStorm entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
