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

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	public ItemWaterRing() {
		super(LibItemNames.WATER_RING);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(player.isInsideOfMaterial(Material.water)) {
			double motionX = player.motionX * 1.2;
			double motionY = player.motionY * 1.2;
			double motionZ = player.motionZ * 1.2;

			boolean changeX = Math.min(1.3, Math.abs(motionX)) == Math.abs(motionX);
			boolean changeY = Math.min(1.3, Math.abs(motionY)) == Math.abs(motionY);
			boolean changeZ = Math.min(1.3, Math.abs(motionZ)) == Math.abs(motionZ);

			if(player instanceof EntityPlayer && ((EntityPlayer) player).capabilities.isFlying)
				changeX = changeY = changeZ = false;

			if(changeX)
				player.motionX = motionX;
			if(changeY)
				player.motionY = motionY;
			if(changeZ)
				player.motionZ = motionZ;

			PotionEffect effect = player.getActivePotionEffect(Potion.nightVision);
			if(effect == null) {
				PotionEffect neweffect = new PotionEffect(Potion.nightVision.id, Integer.MAX_VALUE, -42, true);
				player.addPotionEffect(neweffect);
			}

			if(player.getAir() <= 1 && player instanceof EntityPlayer) {
				int mana = ManaItemHandler.requestMana(stack, (EntityPlayer) player, 300, true);
				if(mana > 0) // If zero gets in the player has no air but won't drown.
					player.setAir(mana);
			}
		} else onUnequipped(stack, player);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(Potion.nightVision);
		if(effect != null && effect.getAmplifier() == -42)
			player.removePotionEffect(Potion.nightVision.id);
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
