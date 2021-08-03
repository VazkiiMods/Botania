/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.handler.MiscellaneousIcons;

public class ItemLavaPendant extends ItemBauble {

	public ItemLavaPendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
		bipedModel.body.translateAndRotate(ms);
		ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);
		BakedModel model = MiscellaneousIcons.INSTANCE.pyroclastGem;
		VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
		Minecraft.getInstance().getBlockRenderer().getModelRenderer()
				.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}
}
