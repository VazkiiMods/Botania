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
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCloak;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemHolyCloak extends ItemBauble {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK);
	private static final ResourceLocation textureGlow = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK_GLOW);

	@OnlyIn(Dist.CLIENT)
	private static ModelCloak model;

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_IN_EFFECT = "inEffect";

	public ItemHolyCloak(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onPlayerDamage);
	}

	private void onPlayerDamage(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity && !event.getSource().canHarmInCreative()) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack stack = EquipmentHandler.findOrEmpty(this, player);

			if (!stack.isEmpty() && !isInEffect(stack)) {
				ItemHolyCloak cloak = (ItemHolyCloak) stack.getItem();
				int cooldown = getCooldown(stack);

				// Used to prevent StackOverflows with mobs that deal damage when damaged
				setInEffect(stack, true);
				if (cooldown == 0 && cloak.effectOnDamage(event, player, stack)) {
					setCooldown(stack, cloak.getCooldownTime(stack));
				}
				setInEffect(stack, false);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		int cooldown = getCooldown(stack);
		if (cooldown > 0) {
			setCooldown(stack, cooldown - 1);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemHolyCloak item = ((ItemHolyCloak) stack.getItem());
		AccessoryRenderHelper.rotateIfSneaking(ms, player);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		ms.translate(0F, armor ? -0.07F : -0.01F, 0F);

		if (model == null) {
			model = new ModelCloak();
		}

		Minecraft.getInstance().textureManager.bindTexture(item.getCloakTexture());
		IVertexBuilder buffer = buffers.getBuffer(model.getRenderType(item.getCloakTexture()));
		model.render(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

		buffer = buffers.getBuffer(model.getRenderType(item.getCloakGlowTexture()));
		model.render(ms, buffer, 0xF000F0, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}

	public boolean effectOnDamage(LivingHurtEvent event, PlayerEntity player, ItemStack stack) {
		if (!event.getSource().isMagicDamage()) {
			event.setCanceled(true);
			player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.holyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for (int i = 0; i < 30; i++) {
				double x = player.getPosX() + Math.random() * player.getWidth() * 2 - player.getWidth();
				double y = player.getPosY() + Math.random() * player.getHeight();
				double z = player.getPosZ() + Math.random() * player.getWidth() * 2 - player.getWidth();
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

	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCloakTexture() {
		return texture;
	}

	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCloakGlowTexture() {
		return textureGlow;
	}
}
