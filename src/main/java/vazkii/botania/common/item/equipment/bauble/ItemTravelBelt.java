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

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.EquipmentHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemTravelBelt extends ItemBauble implements IManaUsingItem {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TRAVEL_BELT);
	@OnlyIn(Dist.CLIENT)
	private static BipedModel<LivingEntity> model;

	private static final int COST = 1;
	private static final int COST_INTERVAL = 10;

	public static final List<String> playersWithStepup = new ArrayList<>();

	public final float speed;
	public final float jump;
	public final float fallBuffer;

	public ItemTravelBelt(Properties props) {
		this(props, 0.035F, 0.2F, 2F);
		MinecraftForge.EVENT_BUS.addListener(this::updatePlayerStepStatus);
		MinecraftForge.EVENT_BUS.addListener(this::onPlayerJump);
		MinecraftForge.EVENT_BUS.addListener(this::playerLoggedOut);
	}

	public ItemTravelBelt(Properties props, float speed, float jump, float fallBuffer) {
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

					if (player.world.isRemote) {
						if ((player.func_233570_aj_() || player.abilities.isFlying) && player.moveForward > 0F && !player.isInWaterOrBubbleColumn()) {
							float speed = beltItem.getSpeed(belt);
							player.moveRelative(player.abilities.isFlying ? speed : speed, new Vector3d(0, 0, 1));
							beltItem.onMovedTick(belt, player);

							if (player.ticksExisted % COST_INTERVAL == 0) {
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
				player.setMotion(player.getMotion().add(0, ((ItemTravelBelt) belt.getItem()).jump, 0));
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
		return player.getGameProfile().getName() + ":" + player.world.isRemote;
	}

	@OnlyIn(Dist.CLIENT)
	ResourceLocation getRenderTexture() {
		return texture;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.rotateIfSneaking(ms, player);
		ms.translate(0F, 0.2F, 0F);

		float s = 0.85F;
		ms.scale(s, s, s);
		if (model == null) {
			model = new BipedModel<>(1F);
		}

		ResourceLocation texture = ((ItemTravelBelt) stack.getItem()).getRenderTexture();
		IVertexBuilder buffer = buffers.getBuffer(model.getRenderType(texture));
		model.bipedBody.render(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
