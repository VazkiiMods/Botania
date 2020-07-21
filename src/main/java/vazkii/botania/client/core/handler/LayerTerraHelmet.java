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
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

import javax.annotation.Nonnull;

public class LayerTerraHelmet extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public LayerTerraHelmet(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(@Nonnull MatrixStack ms, @Nonnull VertexConsumerProvider buffers, int light, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack helm = player.getEquippedStack(EquipmentSlot.HEAD);
		if (!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm) {
			if (ItemTerrasteelHelm.hasAnyWill(helm) && !((ItemTerrasteelArmor) helm.getItem()).hasPhantomInk(helm)) {
				ms.push();
				getContextModel().head.rotate(ms);
				ms.translate(-0.2, -0.15, -0.3);
				ms.scale(0.4F, -0.4F, -0.4F);
				BakedModel model = MiscellaneousIcons.INSTANCE.terrasteelHelmWillModel;
				VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
				MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
						.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
				ms.pop();
			}
		}
	}
}
