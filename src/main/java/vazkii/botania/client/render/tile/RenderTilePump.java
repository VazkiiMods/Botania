/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import vazkii.botania.common.block.tile.mana.TilePump;

public class RenderTilePump extends BlockEntityRenderer<TilePump> {
	public static BakedModel headModel = null;

	public RenderTilePump(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(TilePump pump, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		ms.translate(0.5, 0, 0.5);
		float angle = 0;
		switch (pump.getCachedState().get(Properties.HORIZONTAL_FACING)) {
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
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
		ms.translate(-0.5, 0, -0.5);
		double diff = Math.max(0F, Math.min(8F, pump.innerRingPos + pump.moving * partialTicks));
		ms.translate(0, 0, diff / 14);
		VertexConsumer buffer = buffers.getBuffer(RenderLayer.getSolid());
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(ms.peek(), buffer, null, headModel, 1, 1, 1, light, overlay);
		ms.pop();
	}
}
