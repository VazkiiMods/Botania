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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public final class ManaTabletRenderHandler extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public ManaTabletRenderHandler(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (ConfigHandler.CLIENT.renderAccessories.get() && player.isInvisible()) {
			renderManaTablet(ms, buffers, player);
		}
	}

	private void renderManaTablet(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player) {
		boolean renderedOne = false;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.manaTablet) {
				ms.push();
				boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty();

				getEntityModel().bipedBody.translateRotate(ms);
				ms.translate(0, 0.65, 0);
				if (renderedOne) {
					ms.translate(armor ? 0.3 : 0.25, 0, 0);
					ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				} else {
					ms.translate(armor ? -0.3 : -0.25, 0, 0);
					ms.rotate(Vector3f.YP.rotationDegrees(90F));
				}

				ms.scale(0.375F, -0.375F, -0.375F);
				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, 0xF000F0, OverlayTexture.NO_OVERLAY, ms, buffers);
				ms.pop();

				if (renderedOne) {
					return;
				}
				renderedOne = true;
			}
		}
	}
}
