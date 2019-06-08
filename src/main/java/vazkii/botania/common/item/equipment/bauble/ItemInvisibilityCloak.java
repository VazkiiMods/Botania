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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.lib.LibItemNames;

public class ItemInvisibilityCloak extends ItemBauble implements IManaUsingItem {

	public ItemInvisibilityCloak(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onUnequipped(String identifier, LivingEntity player) {
			EffectInstance effect = player.getActivePotionEffect(Effects.INVISIBILITY);
			if(effect != null && player instanceof PlayerEntity && effect.getAmplifier() == -42)
				player.removePotionEffect(Effects.INVISIBILITY);
		}

		@Override
		public void onCurioTick(String identifier, LivingEntity player) {
			if(player instanceof PlayerEntity && !player.world.isRemote) {
				int manaCost = 2;
				boolean hasMana = ManaItemHandler.requestManaExact(stack, (PlayerEntity) player, manaCost, false);
				if(!hasMana)
					onUnequipped(identifier, player);
				else {
					if(player.getActivePotionEffect(Effects.INVISIBILITY) != null)
						player.removePotionEffect(Effects.INVISIBILITY);

					player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, -42, true, true));
				}
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
