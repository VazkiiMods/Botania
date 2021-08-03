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
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public final class ManaTabletRenderHandler extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public ManaTabletRenderHandler(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack ms, MultiBufferSource buffers, int light, @Nonnull AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (ConfigHandler.CLIENT.renderAccessories.getValue() && !player.isInvisible()) {
			renderManaTablet(ms, buffers, player);
		}
	}

	private void renderManaTablet(PoseStack ms, MultiBufferSource buffers, Player player) {
		boolean renderedOne = false;
		for (int i = 0; i < player.inventory.getContainerSize(); i++) {
			ItemStack stack = player.inventory.getItem(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.manaTablet) {
				ms.pushPose();
				boolean armor = !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty();

				getParentModel().body.translateAndRotate(ms);
				ms.translate(0, 0.65, 0);
				if (renderedOne) {
					ms.translate(armor ? 0.3 : 0.25, 0, 0);
					ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
				} else {
					ms.translate(armor ? -0.3 : -0.25, 0, 0);
					ms.mulPose(Vector3f.YP.rotationDegrees(90F));
				}

				ms.scale(0.375F, -0.375F, -0.375F);
				Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.NONE, 0xF000F0, OverlayTexture.NO_OVERLAY, ms, buffers);
				ms.popPose();

				if (renderedOne) {
					return;
				}
				renderedOne = true;
			}
		}
	}
}
