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
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.UUID;

public class ItemTravelBelt extends ItemBauble {

	private static final UUID STEP_BOOST_UUID = UUID.fromString("8511cd62-2650-4078-8d69-9ebe80b21eb5");
	private static final AttributeModifier STEP_BOOST = new AttributeModifier(
			STEP_BOOST_UUID,
			"botania:travel_belt",
			0.65, AttributeModifier.Operation.ADDITION);

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TRAVEL_BELT);

	private static final int COST = 1;
	private static final int COST_INTERVAL = 10;

	public final float speed;
	public final float jump;
	public final float fallBuffer;

	public ItemTravelBelt(Properties props) {
		this(props, 0.035F, 0.2F, 2F);
		IProxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	public static float onPlayerFall(Player entity, float dist) {
		boolean pendantJump = ItemCloudPendant.popJumping(entity);
		ItemStack stack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, entity);

		if (!stack.isEmpty()) {
			float fallBuffer = ((ItemTravelBelt) stack.getItem()).fallBuffer;

			if (pendantJump) {
				ItemStack amulet = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemCloudPendant, entity);
				if (!amulet.isEmpty()) {
					fallBuffer *= ((ItemCloudPendant) amulet.getItem()).getMaxAllowedJumps();
				}
			}

			return Math.max(0, dist - fallBuffer);
		}
		return dist;
	}

	public ItemTravelBelt(Properties props, float speed, float jump, float fallBuffer) {
		super(props);
		this.speed = speed;
		this.jump = jump;
		this.fallBuffer = fallBuffer;
	}

	public static void updatePlayerStepStatus(Player player) {
		ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);

		var reachDist = IXplatAbstractions.INSTANCE.getStepHeightAttribute();
		if (reachDist == null) {
			// TODO 1.18-forge step height is not yet an attribute. PR?
			return;
		}
		AttributeInstance attrib = player.getAttribute(reachDist);
		boolean hasBoost = attrib.hasModifier(STEP_BOOST);

		if (tryConsumeMana(player)) {
			if (player.level.isClientSide) {
				ItemTravelBelt beltItem = (ItemTravelBelt) belt.getItem();
				if ((player.isOnGround() || player.getAbilities().flying) && player.zza > 0F && !player.isInWaterOrBubble()) {
					float speed = beltItem.getSpeed(belt);
					player.moveRelative(player.getAbilities().flying ? speed : speed, new Vec3(0, 0, 1));
					beltItem.onMovedTick(belt, player);

					if (player.tickCount % COST_INTERVAL == 0) {
						ManaItemHandler.instance().requestManaExact(belt, player, COST, true);
					}
				} else {
					beltItem.onNotMovingTick(belt, player);
				}
			} else {
				if (player.isShiftKeyDown()) {
					if (hasBoost) {
						attrib.removeModifier(STEP_BOOST);
					}
				} else {
					if (!hasBoost) {
						attrib.addTransientModifier(STEP_BOOST);
					}
				}
			}
		} else if (!player.level.isClientSide && hasBoost) {
			attrib.removeModifier(STEP_BOOST);
		}
	}

	public float getSpeed(ItemStack stack) {
		return speed;
	}

	public void onMovedTick(ItemStack stack, Player player) {}

	public void onNotMovingTick(ItemStack stack, Player player) {}

	public static void onPlayerJump(LivingEntity living) {
		if (living instanceof Player player) {
			ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);

			if (!belt.isEmpty() && ManaItemHandler.instance().requestManaExact(belt, player, COST, false)) {
				player.setDeltaMovement(player.getDeltaMovement().add(0, ((ItemTravelBelt) belt.getItem()).jump, 0));
			}
		}
	}

	private static boolean tryConsumeMana(Player player) {
		ItemStack result = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemTravelBelt, player);
		return !result.isEmpty() && ManaItemHandler.instance().requestManaExact(result, player, COST, false);
	}

	ResourceLocation getRenderTexture() {
		return texture;
	}

	public static class Renderer implements AccessoryRenderer {
		private static HumanoidModel<LivingEntity> model = null;

		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			AccessoryRenderHelper.rotateIfSneaking(ms, living);

			float s = 1.15F;
			ms.scale(s, s, s);
			if (model == null) {
				model = new HumanoidModel<>(Minecraft.getInstance()
						.getEntityModels().bakeLayer(ModelLayers.PLAYER));
			}

			ResourceLocation texture = ((ItemTravelBelt) stack.getItem()).getRenderTexture();
			VertexConsumer buffer = buffers.getBuffer(model.renderType(texture));
			model.body.render(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}
	}

}
