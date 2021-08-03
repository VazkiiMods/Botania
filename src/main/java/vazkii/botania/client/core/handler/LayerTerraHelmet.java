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
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

import javax.annotation.Nonnull;

public class LayerTerraHelmet extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public LayerTerraHelmet(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
		super(renderer);
	}

	@Override
	public void render(@Nonnull PoseStack ms, @Nonnull MultiBufferSource buffers, int light, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm) {
			if (ItemTerrasteelHelm.hasAnyWill(helm) && !((ItemTerrasteelArmor) helm.getItem()).hasPhantomInk(helm)) {
				ms.pushPose();
				getParentModel().head.translateAndRotate(ms);
				ms.translate(-0.2, -0.15, -0.3);
				ms.scale(0.4F, -0.4F, -0.4F);
				BakedModel model = MiscellaneousIcons.INSTANCE.terrasteelHelmWillModel;
				VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
				Minecraft.getInstance().getBlockRenderer().getModelRenderer()
						.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
				ms.popPose();
			}
		}
	}
}
