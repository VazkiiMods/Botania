/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileStarfield;

public class RenderTileStarfield implements BlockEntityRenderer<TileStarfield> {
	public RenderTileStarfield(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TileStarfield tile, float partialTicsk, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
		// [VanillaCopy] Adapted from TheEndPortalRenderer, only renders UP face and sets the offset low in the blockspace
		Matrix4f matrix4f = pose.last().pose();
		float offset = 0.24F;
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.STARFIELD);
		this.renderFace(buffer, matrix4f, 0.0F, 1.0F, offset, offset, 1.0F, 1.0F, 0.0F, 0.0F);
	}

	private void renderFace(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float g, float h, float i, float j, float k, float l, float m) {
		vertexConsumer.vertex(matrix4f, f, h, j).endVertex();
		vertexConsumer.vertex(matrix4f, g, h, k).endVertex();
		vertexConsumer.vertex(matrix4f, g, i, l).endVertex();
		vertexConsumer.vertex(matrix4f, f, i, m).endVertex();
	}
}
