/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;

public class AlfheimPortalBlockEntityRenderer implements BlockEntityRenderer<AlfheimPortalBlockEntity> {

	public AlfheimPortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@NotNull AlfheimPortalBlockEntity portal, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		AlfheimPortalState state = portal.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
		if (state == AlfheimPortalState.OFF) {
			return;
		}

		float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.ticksOpen) / 60F) * 0.5F;

		ms.pushPose();
		if (state == AlfheimPortalState.ON_X) {
			ms.translate(0.75, 1, 2);
			ms.mulPose(Vector3f.YP.rotationDegrees(90));
		} else {
			ms.translate(-1, 1, 0.75);
		}
		renderIcon(ms, buffers, MiscellaneousModels.INSTANCE.alfPortalTex.sprite(), 0, 0, 3, 3, alpha, overlay);
		ms.popPose();

		ms.pushPose();
		if (state == AlfheimPortalState.ON_X) {
			ms.translate(0.25, 1, -1);
			ms.mulPose(Vector3f.YP.rotationDegrees(90));
		} else {
			ms.translate(2, 1, 0.25);
		}
		ms.mulPose(Vector3f.YP.rotationDegrees(180));
		renderIcon(ms, buffers, MiscellaneousModels.INSTANCE.alfPortalTex.sprite(), 0, 0, 3, 3, alpha, overlay);
		ms.popPose();
	}

	public void renderIcon(PoseStack ms, MultiBufferSource buffers, TextureAtlasSprite icon, int x, int y, int width, int height, float alpha, int overlay) {
		VertexConsumer buffer = buffers.getBuffer(Sheets.translucentItemSheet());
		Matrix4f model = ms.last().pose();
		Matrix3f normal = ms.last().normal();
		buffer.vertex(model, x, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x + width, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x + width, y, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x, y, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
	}

}
