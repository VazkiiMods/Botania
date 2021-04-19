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
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketJump;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ItemCloudPendant extends ItemBauble {

	private static final Set<PlayerEntity> JUMPING_PLAYERS = Collections.newSetFromMap(new WeakHashMap<>());

	private static int timesJumped;
	private static boolean jumpDown;

	public ItemCloudPendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			if (player == Minecraft.getInstance().player) {
				ClientPlayerEntity playerSp = (ClientPlayerEntity) player;

				if (playerSp.isOnGround()) {
					timesJumped = 0;
				} else {
					if (playerSp.movementInput.jump) {
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

	public static void setJumping(PlayerEntity entity) {
		JUMPING_PLAYERS.add(entity);
	}

	public static boolean popJumping(PlayerEntity entity) {
		if (entity.world.isRemote) {
			return timesJumped > 0;
		}
		return JUMPING_PLAYERS.remove(entity);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		bipedModel.bipedBody.translateRotate(ms);
		ms.translate(-0.3, 0.4, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		IBakedModel model = stack.getItem() == ModItems.superCloudPendant
				? MiscellaneousIcons.INSTANCE.nimbusGem
				: MiscellaneousIcons.INSTANCE.cirrusGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getCutoutBlockType());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
