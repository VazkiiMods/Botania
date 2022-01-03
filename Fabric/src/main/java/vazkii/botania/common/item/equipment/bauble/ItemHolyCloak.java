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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.mutable.MutableFloat;

import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.ModelCloak;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemHolyCloak extends ItemBauble {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK);
	private static final ResourceLocation textureGlow = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK_GLOW);

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_IN_EFFECT = "inEffect";

	public ItemHolyCloak(Properties props) {
		super(props);
		Botania.runOnClient.accept(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	public float onPlayerDamage(Player player, DamageSource src, float amount) {
		if (!src.isBypassInvul()) {
			ItemStack stack = EquipmentHandler.findOrEmpty(this, player);

			if (!stack.isEmpty() && !isInEffect(stack)) {
				ItemHolyCloak cloak = (ItemHolyCloak) stack.getItem();
				int cooldown = getCooldown(stack);

				// Used to prevent StackOverflows with mobs that deal damage when damaged
				setInEffect(stack, true);
				MutableFloat mutAmount = new MutableFloat(amount);
				if (cooldown == 0 && cloak.effectOnDamage(src, mutAmount, player, stack)) {
					setCooldown(stack, cloak.getCooldownTime(stack));
				}
				setInEffect(stack, false);
				return mutAmount.getValue();
			}
		}
		return amount;
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		int cooldown = getCooldown(stack);
		if (cooldown > 0) {
			setCooldown(stack, cooldown - 1);
		}
	}

	public static class Renderer implements AccessoryRenderer {
		private static ModelCloak model = null;

		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity player, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			ItemHolyCloak item = ((ItemHolyCloak) stack.getItem());
			AccessoryRenderHelper.rotateIfSneaking(ms, player);
			boolean armor = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
			ms.translate(0F, armor ? -0.07F : -0.01F, 0F);

			if (model == null) {
				model = new ModelCloak(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayers.CLOAK));
			}

			VertexConsumer buffer = buffers.getBuffer(model.renderType(item.getCloakTexture()));
			model.renderToBuffer(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

			buffer = buffers.getBuffer(model.renderType(item.getCloakGlowTexture()));
			model.renderToBuffer(ms, buffer, 0xF000F0, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}
	}

	protected boolean effectOnDamage(DamageSource src, MutableFloat amount, Player player, ItemStack stack) {
		if (!src.isMagic()) {
			amount.setValue(0);
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.holyCloak, SoundSource.PLAYERS, 1F, 1F);
			for (int i = 0; i < 30; i++) {
				double x = player.getX() + Math.random() * player.getBbWidth() * 2 - player.getBbWidth();
				double y = player.getY() + Math.random() * player.getBbHeight();
				double z = player.getZ() + Math.random() * player.getBbWidth() * 2 - player.getBbWidth();
				boolean yellow = Math.random() > 0.5;
				float r = yellow ? 1F : 0.3F;
				float g = yellow ? 1F : 0.3F;
				float b = yellow ? 0.3F : 1F;
				SparkleParticleData data = SparkleParticleData.sparkle(0.8F + (float) Math.random() * 0.4F, r, g, b, 3);
				player.level.addParticle(data, x, y, z, 0, 0, 0);
			}
			return true;
		}

		return false;
	}

	public int getCooldownTime(ItemStack stack) {
		return 200;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}

	public static boolean isInEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_IN_EFFECT, false);
	}

	public static void setInEffect(ItemStack stack, boolean effect) {
		ItemNBTHelper.setBoolean(stack, TAG_IN_EFFECT, effect);
	}

	ResourceLocation getCloakTexture() {
		return texture;
	}

	ResourceLocation getCloakGlowTexture() {
		return textureGlow;
	}
}
