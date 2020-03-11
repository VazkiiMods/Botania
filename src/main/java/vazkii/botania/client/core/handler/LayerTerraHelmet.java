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

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

public class LayerTerraHelmet extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
	public LayerTerraHelmet(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack helm = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
		if (!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm) {
			ItemTerrasteelHelm.renderOnPlayer(ms, buffers, light, helm, player, partialTicks);
		}
	}
}
