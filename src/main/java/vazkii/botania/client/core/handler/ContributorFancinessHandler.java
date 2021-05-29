/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.core.handler.ContributorList;

import javax.annotation.Nonnull;

import java.util.*;

public final class ContributorFancinessHandler extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public ContributorFancinessHandler(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumerProvider buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ContributorList.firstStart();

		if (player.isInvisible()) {
			return;
		}

		String name = player.getGameProfile().getName();

		if (name.equals("haighyorkie")) {
			renderGoldfish(ms, buffers);
		}

		if (player.isPartVisible(PlayerModelPart.CAPE)) {
			ItemStack flower = ContributorList.getFlower(name.toLowerCase(Locale.ROOT));
			if (!flower.isEmpty()) {
				renderFlower(ms, buffers, player, flower);
			}
		}

	}

	private void renderGoldfish(MatrixStack ms, VertexConsumerProvider buffers) {
		ms.push();
		getContextModel().head.rotate(ms);
		ms.translate(-0.15F, -0.60F, 0F);
		ms.scale(0.4F, -0.4F, -0.4F);
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(ms.peek(), buffers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull()), null, MiscellaneousIcons.INSTANCE.goldfishModel, 1, 1, 1, 0xF000F0, OverlayTexture.DEFAULT_UV);
		ms.pop();
	}

	private void renderFlower(MatrixStack ms, VertexConsumerProvider buffers, PlayerEntity player, ItemStack flower) {
		ms.push();
		getContextModel().head.rotate(ms);
		ms.translate(0, -0.75, 0);
		ms.scale(0.5F, -0.5F, -0.5F);
		MinecraftClient.getInstance().getItemRenderer().renderItem(player, flower, ModelTransformation.Mode.NONE, false, ms, buffers, player.world, 0xF000F0, OverlayTexture.DEFAULT_UV);
		ms.pop();
	}
}
