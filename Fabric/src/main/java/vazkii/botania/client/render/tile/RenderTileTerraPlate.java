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

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileTerraPlate;

import javax.annotation.Nonnull;

public class RenderTileTerraPlate implements BlockEntityRenderer<TileTerraPlate> {

	public RenderTileTerraPlate(BlockEntityRendererProvider.Context manager) {}

	@Override
	public void render(@Nonnull TileTerraPlate plate, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);

		ms.pushPose();
		ms.mulPose(Vector3f.XP.rotationDegrees(90F));
		ms.translate(0F, 0F, -3F / 16F - 0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.TERRA_PLATE);
		IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.terraPlateOverlay.sprite(), 1, 1, alpha);

		ms.popPose();
	}

}
