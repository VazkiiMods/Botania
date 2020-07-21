/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketJump;

public class ItemCloudPendant extends ItemBauble {

	private static int timesJumped;
	private static boolean jumpDown;

	public ItemCloudPendant(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			if (player == MinecraftClient.getInstance().player) {
				ClientPlayerEntity playerSp = (ClientPlayerEntity) player;

				if (playerSp.isOnGround()) {
					timesJumped = 0;
				} else {
					if (playerSp.input.jumping) {
						if (!jumpDown && timesJumped < ((ItemCloudPendant) stack.getItem()).getMaxAllowedJumps()) {
							playerSp.jump();
							PacketHandler.sendToServer(new PacketJump());
							timesJumped++;
						}
						jumpDown = true;
					} else {
						jumpDown = false;
					}
				}
			}
		});
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
		bipedModel.torso.rotate(ms);
		ms.translate(-0.3, 0.4, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		BakedModel model = stack.getItem() == ModItems.superCloudPendant
				? MiscellaneousIcons.INSTANCE.nimbusGem
				: MiscellaneousIcons.INSTANCE.cirrusGem;
		VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
