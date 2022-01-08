/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.core.handler.ContributorList;

import javax.annotation.Nonnull;

import java.util.*;

public final class ContributorFancinessHandler extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public ContributorFancinessHandler(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack ms, MultiBufferSource buffers, int light, @Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ContributorList.firstStart();

		if (player.isInvisible()) {
			return;
		}

		String name = player.getGameProfile().getName();

		if (name.equals("haighyorkie")) {
			renderGoldfish(ms, buffers);
		}

		if (player.isModelPartShown(PlayerModelPart.CAPE)) {
			ItemStack flower = ContributorList.getFlower(name.toLowerCase(Locale.ROOT));
			if (!flower.isEmpty()) {
				renderFlower(ms, buffers, player, flower);
			}
		}

	}

	private void renderGoldfish(PoseStack ms, MultiBufferSource buffers) {
		ms.pushPose();
		getParentModel().head.translateAndRotate(ms);
		ms.translate(-0.15F, -0.60F, 0F);
		ms.scale(0.4F, -0.4F, -0.4F);
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffers.getBuffer(Sheets.translucentCullBlockSheet()), null, MiscellaneousIcons.INSTANCE.goldfishModel, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.popPose();
	}

	private void renderFlower(PoseStack ms, MultiBufferSource buffers, Player player, ItemStack flower) {
		ms.pushPose();
		getParentModel().head.translateAndRotate(ms);
		ms.translate(0, -0.75, 0);
		ms.scale(0.5F, -0.5F, -0.5F);
		Minecraft.getInstance().getItemRenderer().renderStatic(player, flower, ItemTransforms.TransformType.NONE, false,
				ms, buffers, player.level, 0xF000F0, OverlayTexture.NO_OVERLAY, player.getId());
		ms.popPose();
	}
}
