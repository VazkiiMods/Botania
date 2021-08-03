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
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.tile.mana.TilePump;

public class RenderTilePump extends BlockEntityRenderer<TilePump> {
	public static BakedModel headModel = null;

	public RenderTilePump(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(TilePump pump, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.translate(0.5, 0, 0.5);
		float angle = 0;
		switch (pump.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
		default:
		case NORTH:
			break;
		case SOUTH:
			angle = 180;
			break;
		case EAST:
			angle = -90;
			break;
		case WEST:
			angle = 90;
			break;
		}
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));
		ms.translate(-0.5, 0, -0.5);
		double diff = Math.max(0F, Math.min(8F, pump.innerRingPos + pump.moving * partialTicks));
		ms.translate(0, 0, diff / 14);
		VertexConsumer buffer = buffers.getBuffer(RenderType.solid());
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, null, headModel, 1, 1, 1, light, overlay);
		ms.popPose();
	}
}
