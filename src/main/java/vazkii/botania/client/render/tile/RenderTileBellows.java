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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.ModelBellows;
import vazkii.botania.common.block.tile.mana.TileBellows;

import javax.annotation.Nullable;

public class RenderTileBellows implements BlockEntityRenderer<TileBellows> {
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BELLOWS);
	private final ModelBellows model;

	public RenderTileBellows(BlockEntityRendererProvider.Context ctx) {
		model = new ModelBellows(ctx.bakeLayer(ModModelLayers.BELLOWS));
	}

	@Override
	public void render(@Nullable TileBellows bellows, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		float angle = 0;
		if (bellows != null) {
			switch (bellows.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case SOUTH:
				break;
			case NORTH:
				angle = 180F;
				break;
			case EAST:
				angle = 270F;
				break;
			case WEST:
				angle = 90F;
				break;
			}
		}
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));
		float fract = Math.max(0.1F, 1F - (bellows == null ? 0 : bellows.movePos + bellows.moving * f + 0.1F));
		VertexConsumer buffer = buffers.getBuffer(model.renderType(texture));
		model.render(ms, buffer, light, overlay, 1, 1, 1, 1, fract);
		ms.popPose();
	}

}
