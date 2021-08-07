/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.entity.EntityMagicLandmine;

import javax.annotation.Nonnull;

public class RenderMagicLandmine extends EntityRenderer<EntityMagicLandmine> {
	private static final double INITIAL_OFFSET = -1.0 / 16 + 0.005;
	// Global y offset so that overlapping landmines do not Z-fight
	public static double offY = INITIAL_OFFSET;

	public RenderMagicLandmine(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	public static void onWorldRenderLast() {
		offY = INITIAL_OFFSET;
	}

	@Override
	public void render(EntityMagicLandmine e, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		super.render(e, entityYaw, partialTicks, ms, buffers, light);

		ms.pushPose();
		AABB aabb = e.getBoundingBox().move(e.position().scale(-1));

		float gs = (float) (Math.sin(ClientTickHandler.total / 20) + 1) * 0.2F + 0.6F;
		int r = (int) (105 * gs);
		int g = (int) (25 * gs);
		int b = (int) (145 * gs);
		int color = r << 16 | g << 8 | b;

		int alpha = 32;
		if (e.tickCount < 8) {
			alpha *= Math.min((e.tickCount + partialTicks) / 8F, 1F);
		} else if (e.tickCount > 47) {
			alpha *= Math.min(1F - (e.tickCount - 47 + partialTicks) / 8F, 1F);
		}

		RenderTileSpecialFlower.renderRectangle(ms, buffers, aabb, false, color, (byte) alpha);
		offY += 0.001;
		ms.popPose();
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull EntityMagicLandmine entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
