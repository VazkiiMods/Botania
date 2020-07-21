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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.EquipmentHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemTravelBelt extends ItemBauble implements IManaUsingItem {

	private static final Identifier texture = new Identifier(LibResources.MODEL_TRAVEL_BELT);
	@Environment(EnvType.CLIENT)
	private static BipedEntityModel<LivingEntity> model;

	private static final int COST = 1;
	private static final int COST_INTERVAL = 10;

	public static final List<String> playersWithStepup = new ArrayList<>();

	public final float speed;
	public final float jump;
	public final float fallBuffer;

	public ItemTravelBelt(Settings props) {
		this(props, 0.035F, 0.2F, 2F);
		MinecraftForge.EVENT_BUS.addListener(this::updatePlayerStepStatus);
		MinecraftForge.EVENT_BUS.addListener(this::onPlayerJump);
		MinecraftForge.EVENT_BUS.addListener(this::playerLoggedOut);
	}

	public ItemTravelBelt(Settings props, float speed, float jump, float fallBuffer) {
		super(props);
		this.speed = speed;
		this.jump = jump;
		this.fallBuffer = fallBuffer;
	}

	private void updatePlayerStepStatus(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);
			String s = playerStr(player);

			if (playersWithStepup.contains(s)) {
				if (shouldPlayerHaveStepup(player)) {
					ItemTravelBelt beltItem = (ItemTravelBelt) belt.getItem();

					if (player.world.isClient) {
						if ((player.isOnGround() || player.abilities.flying) && player.forwardSpeed > 0F && !player.isInsideWaterOrBubbleColumn()) {
							float speed = beltItem.getSpeed(belt);
							player.updateVelocity(player.abilities.flying ? speed : speed, new Vec3d(0, 0, 1));
							beltItem.onMovedTick(belt, player);

							if (player.age % COST_INTERVAL == 0) {
								ManaItemHandler.instance().requestManaExact(belt, player, COST, true);
							}
						} else {
							beltItem.onNotMovingTick(belt, player);
						}
					}

					if (player.isSneaking()) {
						player.stepHeight = 0.60001F; // Not 0.6F because that is the default
					} else {
						player.stepHeight = 1.25F;
					}

				} else {
					player.stepHeight = 0.6F;
					playersWithStepup.remove(s);
				}
			} else if (shouldPlayerHaveStepup(player)) {
				playersWithStepup.add(s);
				player.stepHeight = 1.25F;
			}
		}
	}

	public float getSpeed(ItemStack stack) {
		return speed;
	}

	public void onMovedTick(ItemStack stack, PlayerEntity player) {}

	public void onNotMovingTick(ItemStack stack, PlayerEntity player) {}

	private void onPlayerJump(LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);

			if (!belt.isEmpty() && ManaItemHandler.instance().requestManaExact(belt, player, COST, false)) {
				player.setVelocity(player.getVelocity().add(0, ((ItemTravelBelt) belt.getItem()).jump, 0));
				player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer;
			}
		}
	}

	private boolean shouldPlayerHaveStepup(PlayerEntity player) {
		ItemStack result = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);
		return !result.isEmpty() && ManaItemHandler.instance().requestManaExact(result, player, COST, false);
	}

	private void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.getPlayer().getGameProfile().getName();
		playersWithStepup.remove(username + ":false");
		playersWithStepup.remove(username + ":true");
	}

	public static String playerStr(PlayerEntity player) {
		return player.getGameProfile().getName() + ":" + player.world.isClient;
	}

	@Environment(EnvType.CLIENT)
	Identifier getRenderTexture() {
		return texture;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.rotateIfSneaking(ms, player);
		ms.translate(0F, 0.2F, 0F);

		float s = 0.85F;
		ms.scale(s, s, s);
		if (model == null) {
			model = new BipedEntityModel<>(1F);
		}

		Identifier texture = ((ItemTravelBelt) stack.getItem()).getRenderTexture();
		VertexConsumer buffer = buffers.getBuffer(model.getLayer(texture));
		model.torso.render(ms, buffer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
