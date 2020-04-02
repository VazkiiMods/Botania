/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

import javax.annotation.Nonnull;

public class LayerTerraHelmet extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
	public LayerTerraHelmet(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(@Nonnull MatrixStack ms, @Nonnull IRenderTypeBuffer buffers, int light, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack helm = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
		if (!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm) {
			if (ItemTerrasteelHelm.hasAnyWill(helm) && !((ItemTerrasteelArmor) helm.getItem()).hasPhantomInk(helm)) {
				ms.push();
				getEntityModel().bipedHead.rotate(ms);
				ms.translate(-0.2, -0.15, -0.3);
				ms.scale(0.4F, -0.4F, -0.4F);
				IBakedModel model = MiscellaneousIcons.INSTANCE.terrasteelHelmWillModel;
				IVertexBuilder buffer = buffers.getBuffer(Atlases.getEntityCutout());
				Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
								.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
				ms.pop();
			}
		}
	}
}
