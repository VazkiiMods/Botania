/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 4, 2014, 11:03:13 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCloak;
import vazkii.botania.common.Botania;
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
		if(event.getEntityLiving() instanceof PlayerEntity && !event.getSource().canHarmInCreative()) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			ItemStack stack = EquipmentHandler.findOrEmpty(this, player);

			if(!stack.isEmpty() && !isInEffect(stack)) {
				ItemHolyCloak cloak = (ItemHolyCloak) stack.getItem();
				int cooldown = getCooldown(stack);

				// Used to prevent StackOverflows with mobs that deal damage when damaged
				setInEffect(stack, true);
				if(cooldown == 0 && cloak.effectOnDamage(event, player, stack))
					setCooldown(stack, cloak.getCooldownTime(stack));
				setInEffect(stack, false);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		int cooldown = getCooldown(stack);
		if(cooldown > 0)
			setCooldown(stack, cooldown - 1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemHolyCloak item = ((ItemHolyCloak) stack.getItem());
		AccessoryRenderHelper.rotateIfSneaking(player);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		GlStateManager.translatef(0F, armor ? -0.07F : -0.01F, 0F);

		float s = 1F / 16F;
		GlStateManager.scalef(s, s, s);
		if(model == null)
			model = new ModelCloak();

		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();

		Minecraft.getInstance().textureManager.bindTexture(item.getCloakTexture());
		model.render(1F);

		int light = 15728880;
		int lightmapX = light % 65536;
		int lightmapY = light / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapX, lightmapY);
		Minecraft.getInstance().textureManager.bindTexture(item.getCloakGlowTexture());
		model.render(1F);
	}

	public boolean effectOnDamage(LivingHurtEvent event, PlayerEntity player, ItemStack stack) {
		if(!event.getSource().isMagicDamage()) {
			event.setCanceled(true);
			player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.holyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for(int i = 0; i < 30; i++) {
				double x = player.posX + Math.random() * player.getWidth() * 2 - player.getWidth();
				double y = player.posY + Math.random() * player.getHeight();
				double z = player.posZ + Math.random() * player.getWidth() * 2 - player.getWidth();
				boolean yellow = Math.random() > 0.5;
				Botania.proxy.sparkleFX(x, y, z, yellow ? 1F : 0.3F, yellow ? 1F : 0.3F, yellow ? 0.3F : 1F, 0.8F + (float) Math.random() * 0.4F, 3);
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

