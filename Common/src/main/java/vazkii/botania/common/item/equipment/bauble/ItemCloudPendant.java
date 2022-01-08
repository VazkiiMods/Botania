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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ItemCloudPendant extends ItemBauble {

	private static final Set<Player> JUMPING_PLAYERS = Collections.newSetFromMap(new WeakHashMap<>());

	private static int timesJumped;
	private static boolean jumpDown;

	public ItemCloudPendant(Properties props) {
		super(props);
		Botania.runOnClient.accept(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		Botania.runOnClient.accept(() -> () -> {
			if (player == Minecraft.getInstance().player) {
				LocalPlayer playerSp = (LocalPlayer) player;

				if (playerSp.isOnGround()) {
					timesJumped = 0;
				} else {
					if (playerSp.input.jumping) {
						if (!jumpDown && timesJumped < ((ItemCloudPendant) stack.getItem()).getMaxAllowedJumps()) {
							playerSp.jumpFromGround();
							IXplatAbstractions.INSTANCE.sendJumpPacket();
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

	public static void setJumping(Player entity) {
		JUMPING_PLAYERS.add(entity);
	}

	public static boolean popJumping(Player entity) {
		if (entity.level.isClientSide) {
			return timesJumped > 0;
		}
		return JUMPING_PLAYERS.remove(entity);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean armor = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(-0.3, 0.4, armor ? 0.05 : 0.12);
			ms.scale(0.5F, -0.5F, -0.5F);

			BakedModel model = stack.is(ModItems.superCloudPendant)
					? MiscellaneousIcons.INSTANCE.nimbusGem
					: MiscellaneousIcons.INSTANCE.cirrusGem;
			VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer()
					.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
		}
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
