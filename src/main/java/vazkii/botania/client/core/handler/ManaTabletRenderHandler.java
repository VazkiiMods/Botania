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
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public final class ManaTabletRenderHandler extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public ManaTabletRenderHandler(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumerProvider buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (ConfigHandler.CLIENT.renderAccessories.get() && !player.isInvisible()) {
			renderManaTablet(ms, buffers, player);
		}
	}

	private void renderManaTablet(MatrixStack ms, VertexConsumerProvider buffers, PlayerEntity player) {
		boolean renderedOne = false;
		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack stack = player.inventory.getStack(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.manaTablet) {
				ms.push();
				boolean armor = !player.getEquippedStack(EquipmentSlot.LEGS).isEmpty();

				getContextModel().torso.rotate(ms);
				ms.translate(0, 0.65, 0);
				if (renderedOne) {
					ms.translate(armor ? 0.3 : 0.25, 0, 0);
					ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
				} else {
					ms.translate(armor ? -0.3 : -0.25, 0, 0);
					ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
				}

				ms.scale(0.375F, -0.375F, -0.375F);
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, 0xF000F0, OverlayTexture.DEFAULT_UV, ms, buffers);
				ms.pop();

				if (renderedOne) {
					return;
				}
				renderedOne = true;
			}
		}
	}
}
