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
import net.minecraft.util.math.Box;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.entity.EntityMagicLandmine;

import javax.annotation.Nonnull;

public class RenderMagicLandmine extends EntityRenderer<EntityMagicLandmine> {
	private static final double INITIAL_OFFSET = -1.0 / 16 + 0.005;
	// Global y offset so that overlapping landmines do not Z-fight
	public static double offY = INITIAL_OFFSET;

	public RenderMagicLandmine(EntityRenderDispatcher renderManager, EntityRendererRegistry.Context ctx) {
		super(renderManager);
	}

	public static void onWorldRenderLast() {
		offY = INITIAL_OFFSET;
	}

	@Override
	public void render(EntityMagicLandmine e, float entityYaw, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		super.render(e, entityYaw, partialTicks, ms, buffers, light);

		ms.push();
		Box aabb = e.getBoundingBox().offset(e.getPos().multiply(-1));

		float gs = (float) (Math.sin(ClientTickHandler.total / 20) + 1) * 0.2F + 0.6F;
		int r = (int) (105 * gs);
		int g = (int) (25 * gs);
		int b = (int) (145 * gs);
		int color = r << 16 | g << 8 | b;

		int alpha = 32;
		if (e.age < 8) {
			alpha *= Math.min((e.age + partialTicks) / 8F, 1F);
		} else if (e.age > 47) {
			alpha *= Math.min(1F - (e.age - 47 + partialTicks) / 8F, 1F);
		}

		RenderTileSpecialFlower.renderRectangle(ms, buffers, aabb, false, color, (byte) alpha);
		offY += 0.001;
		ms.pop();
	}

	@Nonnull
	@Override
	public Identifier getEntityTexture(@Nonnull EntityMagicLandmine entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
	}
}
