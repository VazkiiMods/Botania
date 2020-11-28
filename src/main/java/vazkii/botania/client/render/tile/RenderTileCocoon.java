/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

import vazkii.botania.common.block.tile.TileCocoon;

import javax.annotation.Nonnull;

public class RenderTileCocoon extends BlockEntityRenderer<TileCocoon> {

	public RenderTileCocoon(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileCocoon cocoon, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) TileCocoon.TOTAL_TIME * 30F;
		if (cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + partialTicks) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + partialTicks);
		}

		ms.push();
		ms.translate(0.5, 0, 0);
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(rot));
		ms.translate(-0.5, 0, 0);
		BlockState state = cocoon.getCachedState();
		BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state);
		VertexConsumer buffer = buffers.getBuffer(RenderLayers.getBlockLayer(state));
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(ms.peek(), buffer, state, model, 1, 1, 1, light, overlay);
		ms.pop();
	}
}
