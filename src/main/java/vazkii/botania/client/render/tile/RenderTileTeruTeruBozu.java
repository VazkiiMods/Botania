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

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.ModelTeruTeruBozu;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;

import javax.annotation.Nullable;

import java.util.Random;

public class RenderTileTeruTeruBozu implements BlockEntityRenderer<TileTeruTeruBozu> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	private final ModelTeruTeruBozu model;

	public RenderTileTeruTeruBozu(BlockEntityRendererProvider.Context ctx) {
		model = new ModelTeruTeruBozu(ctx.bakeLayer(ModModelLayers.TERU_TERU_BOZU));
	}

	@Override
	public void render(@Nullable TileTeruTeruBozu tileentity, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.mulPose(Vector3f.XP.rotationDegrees(180));
		double time = ClientTickHandler.ticksInGame + partialTicks;
		boolean hasWorld = tileentity != null && tileentity.getLevel() != null;
		if (hasWorld) {
			time += new Random(tileentity.getBlockPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, -1.25F + (hasWorld ? (float) Math.sin(time * 0.01F) * 0.05F : 0F), -0.5F);
		if (hasWorld) {
			ms.mulPose(Vector3f.YP.rotationDegrees((float) (time * 0.3)));
			ms.mulPose(Vector3f.ZP.rotationDegrees(4F * (float) Math.sin(time * 0.05F)));
			float s = 0.75F;
			ms.scale(s, s, s);
		}

		VertexConsumer buffer = buffers.getBuffer(model.renderType(ClientProxy.dootDoot ? textureHalloween : texture));
		model.renderToBuffer(ms, buffer, light, overlay, 1, 1, 1, 1);
		ms.popPose();
	}

}
