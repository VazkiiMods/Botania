/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 4:12:46 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.lib.LibItemNames;

public class ItemMiningRing extends ItemBauble implements IManaUsingItem {

	public ItemMiningRing(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onCurioTick(String identifier, EntityLivingBase player) {
			if(player instanceof EntityPlayer && !player.world.isRemote) {
				int manaCost = 5;
				boolean hasMana = ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, manaCost, false);
				if(!hasMana)
					onUnequipped(identifier, player);
				else {
					if(player.getActivePotionEffect(MobEffects.HASTE) != null)
						player.removePotionEffect(MobEffects.HASTE);

					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, Integer.MAX_VALUE, 1, true, true));
				}

				if(player.swingProgress == 0.25F)
					ManaItemHandler.requestManaExact(stack, (EntityPlayer) player, manaCost, true);
			}
		}

		@Override
		public void onUnequipped(String identifier, EntityLivingBase player) {
			PotionEffect effect = player.getActivePotionEffect(MobEffects.HASTE);
			if(effect != null && effect.getAmplifier() == 1)
				player.removePotionEffect(MobEffects.HASTE);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
