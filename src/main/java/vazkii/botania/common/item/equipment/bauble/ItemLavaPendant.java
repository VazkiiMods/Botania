/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

public class ItemLavaPendant extends ItemBauble {

	public ItemLavaPendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player.isBurning()) {
			player.extinguish();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		layer.getEntityModel().bipedBody.translateRotate(ms);
		ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);
		IBakedModel model = MiscellaneousIcons.INSTANCE.pyroclastGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getCutoutBlockType());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}
}
