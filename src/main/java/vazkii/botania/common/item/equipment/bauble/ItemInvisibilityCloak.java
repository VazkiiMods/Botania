/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 11:51:06 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemInvisibilityCloak extends ItemBauble implements IManaUsingItem {

	public ItemInvisibilityCloak() {
		super(LibItemNames.INVISIBILITY_CLOAK);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		PotionEffect effect = player.getActivePotionEffect(MobEffects.INVISIBILITY);
		if(effect != null && player instanceof EntityPlayer && effect.getAmplifier() == -42)
			player.removePotionEffect(MobEffects.INVISIBILITY);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		if(player instanceof EntityPlayer && !player.world.isRemote) {
			int manaCost = 2;
			boolean hasMana = ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, manaCost, false);
			if(!hasMana)
				onUnequipped(stack, player);
			else {
				if(player.getActivePotionEffect(MobEffects.INVISIBILITY) != null)
					player.removePotionEffect(MobEffects.INVISIBILITY);

				player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, -42, true, true));
			}

		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
