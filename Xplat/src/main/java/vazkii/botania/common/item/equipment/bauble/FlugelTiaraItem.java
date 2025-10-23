/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.*;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.StoneOfTemperanceItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlugelTiaraItem extends BaubleItem implements CustomCreativeTabContents {

	private static final ResourceLocation textureHud = ResourceLocation.parse(ResourcesLib.GUI_HUD_ICONS);
	public static final ResourceLocation textureHalo = ResourceLocation.parse(ResourcesLib.MISC_HALO);

	private static final List<String> playersWithFlight = Collections.synchronizedList(new ArrayList<>());
	private static final int COST = 35;
	private static final int COST_OVERKILL = COST * 3;
	private static final int DEFAULT_MAX_FLY_TIME = 1200;

	private static final int SUBTYPES = 8;
	public static final int WING_TYPES = 9;

	private static final String SUPER_AWESOME_HASH = "4D0F274C5E3001C95640B5E88A821422C8B1E132264492C043A3D746B705C025";

	public FlugelTiaraItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		for (int i = 0; i < SUBTYPES + 1; i++) {
			ItemStack stack = new ItemStack(this);
			stack.set(BotaniaDataComponents.TIARA_VARIANT, i);
			output.accept(stack);
		}

	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		tooltip.add(Component.translatable("botania.wings" + getVariant(stack)));
	}

	public static int getMaxFlightTime(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.MAX_USE_TICKS, DEFAULT_MAX_FLY_TIME);
	}

	public static void updatePlayerFlyStatus(Player player) {
		ItemStack tiara = EquipmentHandler.findOrEmpty(BotaniaItems.flightTiara, player);
		int left = tiara.getOrDefault(BotaniaDataComponents.REMAINING_TICKS, getMaxFlightTime(tiara));

		if (playersWithFlight.contains(playerStr(player))) {
			if (shouldPlayerHaveFlight(player)) {
				player.getAbilities().mayfly = true;
				if (player.getAbilities().flying) {
					if (!player.level().isClientSide) {
						if (!player.isCreative() && !player.isSpectator()) {
							ManaItemHandler.instance().requestManaExact(tiara, player, getCost(tiara, left), true);
						}
					} else if (Math.abs(player.getDeltaMovement().x()) > 0.1 || Math.abs(player.getDeltaMovement().z()) > 0.1) {
						double x = player.getX() - 0.5;
						double y = player.getY() - 0.5;
						double z = player.getZ() - 0.5;

						float r = 1F;
						float g = 1F;
						float b = 1F;

						int variant = getVariant(tiara);
						switch (variant) {
							case 2 -> {
								r = 0.1F;
								g = 0.1F;
								b = 0.1F;
							}
							case 3 -> {
								r = 0F;
								g = 0.6F;
							}
							case 4 -> {
								g = 0.3F;
								b = 0.3F;
							}
							case 5 -> {
								r = 0.6F;
								g = 0F;
								b = 0.6F;
							}
							case 6 -> {
								r = 0.4F;
								g = 0F;
								b = 0F;
							}
							case 7 -> {
								r = 0.2F;
								g = 0.6F;
								b = 0.2F;
							}
							case 8 -> {
								r = 0.85F;
								g = 0.85F;
								b = 0F;
							}
							case 9 -> {
								r = 0F;
								b = 0F;
							}
						}

						for (int i = 0; i < 2; i++) {
							SparkleParticleData data = SparkleParticleData.sparkle(2F * (float) Math.random(), r, g, b, 20);
							player.level().addParticle(data, x + Math.random() * player.getBbWidth(), y + Math.random() * 0.4, z + Math.random() * player.getBbWidth(), 0, 0, 0);
						}
					}
				}
			} else {
				if (!player.isSpectator() && !player.isCreative()) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.getAbilities().invulnerable = false;
				}
				playersWithFlight.remove(playerStr(player));
			}
		} else if (shouldPlayerHaveFlight(player)) {
			playersWithFlight.add(playerStr(player));
			player.getAbilities().mayfly = true;
		}
	}

	public static void playerLoggedOut(ServerPlayer player) {
		String username = player.getGameProfile().getName();
		playersWithFlight.remove(username + ":false");
		playersWithFlight.remove(username + ":true");
	}

	private static String playerStr(Player player) {
		return player.getGameProfile().getName() + ":" + player.level().isClientSide;
	}

	private static boolean shouldPlayerHaveFlight(Player player) {
		ItemStack armor = EquipmentHandler.findOrEmpty(BotaniaItems.flightTiara, player);
		if (!armor.isEmpty()) {
			int maxFlightTime = getMaxFlightTime(armor);
			int left = armor.getOrDefault(BotaniaDataComponents.REMAINING_TICKS, maxFlightTime);
			boolean flying = armor.has(BotaniaDataComponents.FLYING);
			return (left > (flying ? 0 : maxFlightTime / 10) || InventoryHelper.containsType(player.getInventory(), BotaniaItems.flugelEye)) && ManaItemHandler.instance().requestManaExact(armor, player, getCost(armor, left), false);
		}

		return false;
	}

	// TODO: make configurable via components?
	public static int getCost(ItemStack stack, int timeLeft) {
		return timeLeft <= 0 ? COST_OVERKILL : COST;
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		super.onEquipped(stack, living);
		int variant = getVariant(stack);
		if (variant != WING_TYPES && StringObfuscator.matchesHash(stack.getHoverName().getString(), SUPER_AWESOME_HASH)) {
			stack.set(BotaniaDataComponents.TIARA_VARIANT, WING_TYPES);
			stack.remove(DataComponents.CUSTOM_NAME);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (living instanceof Player player) {
			boolean flying = player.getAbilities().flying;

			boolean wasSprting = stack.has(BotaniaDataComponents.IS_SPRINTING);
			boolean isSprinting = player.isSprinting();
			if (isSprinting != wasSprting) {
				DataComponentHelper.setFlag(stack, BotaniaDataComponents.IS_SPRINTING, isSprinting);
			}

			int maxFlightTime = getMaxFlightTime(stack);
			int time = stack.getOrDefault(BotaniaDataComponents.REMAINING_TICKS, maxFlightTime);
			int newTime = time;
			Vec3 look = player.getLookAngle().multiply(1, 0, 1).normalize();

			if (flying) {
				if (time > 0 && !player.isSpectator() && !player.isCreative()
						&& !stack.has(BotaniaDataComponents.CREATIVE_FLIGHT)) {
					newTime--;
				}
				final int maxCd = 40;
				boolean isOnCooldown = player.getCooldowns().isOnCooldown(this);
				if (!wasSprting && isSprinting && !isOnCooldown && !StoneOfTemperanceItem.hasTemperanceActive(player)) {
					player.setDeltaMovement(player.getDeltaMovement().add(look.x, 0, look.z));
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.dash, SoundSource.PLAYERS, 1F, 1F);
					player.getCooldowns().addCooldown(this, maxCd);
					DataComponentHelper.setFlag(stack, BotaniaDataComponents.BOOST_PENDING, true);
				} else if (isOnCooldown) {
					if (stack.has(BotaniaDataComponents.BOOST_PENDING)) {
						living.moveRelative(5F, new Vec3(0F, 0F, 1F));
						stack.remove(BotaniaDataComponents.BOOST_PENDING);
					}
				}
			} else {
				boolean wasGliding = stack.has(BotaniaDataComponents.IS_GLIDING);
				boolean doGlide = living.isShiftKeyDown() && !living.onGround() && (living.getDeltaMovement().y() < -.7F || wasGliding);
				if (time < maxFlightTime && living.tickCount % (doGlide ? 6 : 2) == 0) {
					newTime++;
				}

				if (doGlide) {
					float mul = 0.6F;
					living.setDeltaMovement(look.x * mul, Math.max(-0.15F, living.getDeltaMovement().y()), look.z * mul);
					living.fallDistance = 2F;
				}
				DataComponentHelper.setFlag(stack, BotaniaDataComponents.IS_GLIDING, doGlide);
			}

			DataComponentHelper.setFlag(stack, BotaniaDataComponents.FLYING, flying);
			if (newTime != time) {
				stack.set(BotaniaDataComponents.REMAINING_TICKS, newTime);
			}
		}
	}

	@Override
	public boolean hasRender(ItemStack stack, LivingEntity living) {
		return super.hasRender(stack, living) && living instanceof Player;
	}

	public static class Renderer implements AccessoryRenderer {
		/*
			NB: All of the following methods are somewhat similar, but they are split apart to isolate the logic.
			Trying too hard to factor things out of each case led to very spaghetti-looking code.
			As such, only Jibril's is commented, the rest are variations on the same theme
		*/

		private static void renderBasic(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
			ms.pushPose();

			// attach to body
			bipedModel.body.translateAndRotate(ms);

			// position on body
			ms.translate(0, 0.5, 0.2);

			for (int i = 0; i < 2; i++) {
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180 - flap));

				// move so flapping about the edge instead of center of texture
				ms.translate(-1, 0, 0);

				// rotate since the textures are stored rotated
				ms.mulPose(VecHelper.rotateZ(-60));
				ms.scale(1.5F, -1.5F, -1.5F);
				Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
				ms.popPose();
			}

			ms.popPose();
		}

		private static void renderSephiroth(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light, float flap) {
			ms.pushPose();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(0, 0.5, 0.2);

			ms.mulPose(VecHelper.rotateY(flap));
			ms.translate(-1.1, 0, 0);

			ms.mulPose(VecHelper.rotateZ(-60));
			ms.scale(1.6F, -1.6F, -1.6F);
			Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
			ms.popPose();
		}

		private static void renderCirno(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, int light) {
			ms.pushPose();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(-0.8, 0.15, 0.25);

			for (int i = 0; i < 2; i++) {
				ms.pushPose();

				if (i == 1) {
					ms.mulPose(VecHelper.rotateY(180));
					ms.translate(-1.6, 0, 0);
				}

				ms.scale(1.6F, -1.6F, -1.6F);
				Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, light, OverlayTexture.NO_OVERLAY, model);
				ms.popPose();
			}

			ms.popPose();
		}

		private static void renderPhoenix(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
			ms.pushPose();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(0, -0.2, 0.2);

			for (int i = 0; i < 2; i++) {
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180 - flap));

				ms.translate(-0.9, 0, 0);

				ms.scale(1.7F, -1.7F, -1.7F);
				Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
				ms.popPose();
			}

			ms.popPose();
		}

		private static void renderKuroyukihime(HumanoidModel<?> bipedModel, BakedModel model, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap) {
			ms.pushPose();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(0, -0.4, 0.2);

			for (int i = 0; i < 2; i++) {
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180 - flap));

				ms.translate(-1.3, 0, 0);

				ms.scale(2.5F, -2.5F, -2.5F);
				Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
				ms.popPose();
			}

			ms.popPose();
		}

		private static void renderCustomColor(HumanoidModel<?> bipedModel, BakedModel model, LivingEntity living, ItemStack stack, PoseStack ms, MultiBufferSource buffers, float flap, int color) {
			ms.pushPose();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(0, 0, 0.2);

			for (int i = 0; i < 2; i++) {
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(i == 0 ? flap : 180 - flap));
				ms.translate(-0.7, 0, 0);

				ms.scale(1.5F, -1.5F, -1.5F);

				RenderHelper.renderItemCustomColor(living, stack, color, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
				ms.popPose();
			}

			ms.popPose();
		}

		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			int meta = getVariant(stack);
			if (meta <= 0 || meta >= MiscellaneousModels.INSTANCE.tiaraWingIcons.length + 1) {
				return;
			}

			BakedModel model = MiscellaneousModels.INSTANCE.tiaraWingIcons[meta - 1];
			ClientXplatAbstractions.instance().markSpriteActive(model.getParticleIcon());
			boolean flying = living instanceof Player player && player.getAbilities().flying;
			float tickTime = living.tickCount + partialTicks;
			float flap = 20 + (Mth.sin(tickTime * (flying ? 0.4f : 0.2f)) + 0.5f) * (flying ? 30 : 5);

			switch (meta) {
				case 1:
					renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
					ms.pushPose();
					ClientLogic.renderHalo(bipedModel, ms, buffers, tickTime);
					ms.popPose();
					break;
				case 2:
					renderSephiroth(bipedModel, model, stack, ms, buffers, light, flap);
					break;
				case 3:
					renderCirno(bipedModel, model, stack, ms, buffers, light);
					break;
				case 4:
					renderPhoenix(bipedModel, model, stack, ms, buffers, flap);
					break;
				case 5:
					renderKuroyukihime(bipedModel, model, stack, ms, buffers, flap);
					break;
				case 6:
				case 8:
					renderBasic(bipedModel, model, stack, ms, buffers, light, flap);
					break;
				case 7: {
					float alpha = 0.5f + Mth.cos(tickTime * 0.3f) * 0.2f;
					int color = FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), 0xFFFFFF);
					renderCustomColor(bipedModel, model, living, stack, ms, buffers, flap, color);
					break;
				}
				case 9: {
					float customFlap = -(Mth.sin(tickTime * 0.2f) + 0.6f) * (flying ? 12f : 5f);
					float alpha = 0.5f + (flying ? Mth.cos(tickTime * 0.3f) * 0.25f + 0.25f : 0);
					int color = FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), 0xFFFFFF);
					renderCustomColor(bipedModel, model, living, stack, ms, buffers, customFlap, color);
					break;
				}
			}
		}
	}

	public static class ClientLogic {
		public static void renderHalo(@Nullable HumanoidModel<?> model, PoseStack ms, MultiBufferSource buffers, float tickTime) {
			if (model != null) {
				model.body.translateAndRotate(ms);
			}

			ms.translate(0.2, -0.65, 0);
			ms.mulPose(VecHelper.rotateZ(30));
			ms.mulPose(VecHelper.rotateY(tickTime));

			ms.scale(0.75F, -0.75F, -0.75F);
			VertexConsumer buffer = buffers.getBuffer(RenderHelper.HALO);
			Matrix4f mat = ms.last().pose();
			buffer.addVertex(mat, -1F, 0, -1F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0, 0);
			buffer.addVertex(mat, 1F, 0, -1F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1, 0);
			buffer.addVertex(mat, 1F, 0, 1F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1, 1);
			buffer.addVertex(mat, -1F, 0, 1F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0, 1);
		}

		private static int estimateAdditionalNumRowsRendered(Player player) {
			if (player.isEyeInFluid(FluidTags.WATER) || player.getAirSupply() < player.getMaxAirSupply()) {
				// shift up single row if player is underwater or still recovering air
				return 1;
			}

			Entity playerVehicle = player.getVehicle();
			if (playerVehicle instanceof LivingEntity vehicle && vehicle.showVehicleHealth()) {
				// shift up if vehicle health requires more than one row (vanilla HUD limits vehicle hearts to 3 rows)
				return (Math.min(30, (int) (vehicle.getMaxHealth() + 0.5) / 2) - 1) / 10;
			}

			return 0;
		}

		public static void renderHUD(GuiGraphics gui, float partialTicks, Player player, ItemStack stack) {
			int u = Math.max(1, getVariant(stack)) * 9 - 9;
			int v = 0;

			Minecraft mc = Minecraft.getInstance();
			int xo = mc.getWindow().getGuiScaledWidth() / 2 + 10;
			int y = mc.getWindow().getGuiScaledHeight() - 10 * estimateAdditionalNumRowsRendered(player) - 49;

			int maxFlightTime = getMaxFlightTime(stack);
			int left = stack.getOrDefault(BotaniaDataComponents.REMAINING_TICKS, maxFlightTime);

			int segTime = maxFlightTime / 10;
			int segs = left / segTime + 1;
			int last = left % segTime;

			for (int i = 0; i < segs; i++) {
				float trans = 1F;
				if (i == segs - 1) {
					trans = (float) last / (float) segTime;
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}

				RenderSystem.setShaderColor(1F, 1F, 1F, trans);
				RenderHelper.drawTexturedModalRect(gui, textureHud, xo + 8 * i, y, u, v, 9, 9);
			}

			if (player.getAbilities().flying) {
				int width = (int) (player.getCooldowns().getCooldownPercent(stack.getItem(), partialTicks) * 80);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				if (width > 0) {
					gui.fill(xo, y - 2, xo + 80, y - 1, 0x88000000);
				}
				gui.fill(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		}
	}

	public static int getVariant(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.TIARA_VARIANT, 0);
	}

	public static void setVariant(ItemStack stack, int variant) {
		DataComponentHelper.setIntNonZero(stack, BotaniaDataComponents.TIARA_VARIANT, variant);
	}
}
