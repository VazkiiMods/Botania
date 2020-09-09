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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import org.apache.commons.lang3.mutable.MutableFloat;
import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCloak;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemHolyCloak extends ItemBauble {

	private static final Identifier texture = new Identifier(LibResources.MODEL_HOLY_CLOAK);
	private static final Identifier textureGlow = new Identifier(LibResources.MODEL_HOLY_CLOAK_GLOW);

	@Environment(EnvType.CLIENT)
	private static ModelCloak model;

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_IN_EFFECT = "inEffect";

	public ItemHolyCloak(Settings props) {
		super(props);
	}

	public float onPlayerDamage(PlayerEntity player, DamageSource src, float amount) {
		if (!src.isOutOfWorld()) {
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

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemHolyCloak item = ((ItemHolyCloak) stack.getItem());
		AccessoryRenderHelper.rotateIfSneaking(ms, player);
		boolean armor = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
		ms.translate(0F, armor ? -0.07F : -0.01F, 0F);

		if (model == null) {
			model = new ModelCloak();
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(item.getCloakTexture());
		VertexConsumer buffer = buffers.getBuffer(model.getLayer(item.getCloakTexture()));
		model.render(ms, buffer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

		buffer = buffers.getBuffer(model.getLayer(item.getCloakGlowTexture()));
		model.render(ms, buffer, 0xF000F0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}

	public boolean effectOnDamage(DamageSource src, MutableFloat amount, PlayerEntity player, ItemStack stack) {
		if (!src.getMagic()) {
			amount.setValue(0);
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.holyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for (int i = 0; i < 30; i++) {
				double x = player.getX() + Math.random() * player.getWidth() * 2 - player.getWidth();
				double y = player.getY() + Math.random() * player.getHeight();
				double z = player.getZ() + Math.random() * player.getWidth() * 2 - player.getWidth();
				boolean yellow = Math.random() > 0.5;
				float r = yellow ? 1F : 0.3F;
				float g = yellow ? 1F : 0.3F;
				float b = yellow ? 0.3F : 1F;
				SparkleParticleData data = SparkleParticleData.sparkle(0.8F + (float) Math.random() * 0.4F, r, g, b, 3);
				player.world.addParticle(data, x, y, z, 0, 0, 0);
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

	@Environment(EnvType.CLIENT)
	Identifier getCloakTexture() {
		return texture;
	}

	@Environment(EnvType.CLIENT)
	Identifier getCloakGlowTexture() {
		return textureGlow;
	}
}
