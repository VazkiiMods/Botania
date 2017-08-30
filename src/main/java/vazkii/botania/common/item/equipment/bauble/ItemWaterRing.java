/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 3:44:24 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final double SPEED_MULT = 1.2;
	private static final double MAX_SPEED = 1.3;

	public ItemWaterRing() {
		super(LibItemNames.WATER_RING);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(player.isInsideOfMaterial(Material.WATER)) {
			if(player instanceof EntityPlayer) {
				ItemStack firstRing = BaublesApi.getBaublesHandler((EntityPlayer) player).getStackInSlot(1);
				if(!firstRing.isEmpty() && firstRing.getItem() instanceof ItemWaterRing && firstRing != stack) {
					return;
				}
			}

			double motionX = player.motionX * SPEED_MULT;
			double motionY = player.motionY * SPEED_MULT;
			double motionZ = player.motionZ * SPEED_MULT;

			boolean flying = player instanceof EntityPlayer && ((EntityPlayer) player).capabilities.isFlying;

			if(Math.abs(motionX) < MAX_SPEED && !flying)
				player.motionX = motionX;
			if(Math.abs(motionY) < MAX_SPEED && !flying)
				player.motionY = motionY;
			if(Math.abs(motionZ) < MAX_SPEED && !flying)
				player.motionZ = motionZ;

			PotionEffect effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
			if(effect == null) {
				PotionEffect neweffect = new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, true);
				player.addPotionEffect(neweffect);
			}

			if(player.getAir() <= 1 && player instanceof EntityPlayer) {
				int mana = ManaItemHandler.requestMana(stack, (EntityPlayer) player, 300, true);
				if (mana > 0)
					player.setAir(mana);
			}
		} else onUnequipped(stack, player);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
		if(effect != null && effect.getAmplifier() == -42)
			player.removePotionEffect(MobEffects.NIGHT_VISION);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
