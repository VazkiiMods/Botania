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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderTileFloatingFlower extends BlockEntityRenderer<TileFloatingFlower> {

	public RenderTileFloatingFlower(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileFloatingFlower tile, float t, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		renderFloatingIsland(tile, t, ms, buffers, light, overlay);
	}

	public static void renderFloatingIsland(BlockEntity tile, float t, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		if (ConfigHandler.CLIENT.staticFloaters.getValue()) {
			return;
		}

		IModelData data = tile.getModelData();
		if (!data.hasProperty(BotaniaStateProps.FLOATING_DATA)) {
			return;
		}

		ms.push();

		double worldTime = ClientTickHandler.ticksInGame + t;
		if (tile.getWorld() != null) {
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, 0, 0.5F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-((float) worldTime * 0.5F)));
		ms.translate(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(4F * (float) Math.sin(worldTime * 0.04F)));
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

		BlockRenderManager brd = MinecraftClient.getInstance().getBlockRenderManager();
		BlockState state = tile.getCachedState();

		BakedModel ibakedmodel = brd.getModel(state);
		brd.getModelRenderer().renderModel(ms.peek(), buffers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, ibakedmodel, 1, 1, 1, light, overlay, data);

		ms.pop();
	}

}
