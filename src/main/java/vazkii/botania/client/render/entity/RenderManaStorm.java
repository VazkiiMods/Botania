/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityManaStorm;

import javax.annotation.Nonnull;

public class RenderManaStorm extends EntityRenderer<EntityManaStorm> {

	public RenderManaStorm(EntityRenderDispatcher renderManager, EntityRendererRegistry.Context ctx) {
		super(renderManager);
	}

	@Override
	public void render(EntityManaStorm storm, float yaw, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		ms.push();
		float maxScale = 1.95F;
		float scale = 0.05F + ((float) storm.burstsFired / EntityManaStorm.TOTAL_BURSTS - (storm.deathTime == 0 ? 0 : storm.deathTime + pticks) / EntityManaStorm.DEATH_TIME) * maxScale;
		RenderHelper.renderStar(ms, buffers, 0x00FF00, scale, scale, scale, storm.getUuid().getMostSignificantBits());
		ms.pop();
	}

	@Nonnull
	@Override
	public Identifier getTexture(@Nonnull EntityManaStorm entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

}
