/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemInvisibilityCloak extends ItemBauble implements IManaUsingItem {

	public ItemInvisibilityCloak(Properties props) {
		super(props);
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity player) {
		EffectInstance effect = player.getActivePotionEffect(Effects.INVISIBILITY);
		if (effect != null && player instanceof PlayerEntity && effect.getAmplifier() == -42) {
			player.removePotionEffect(Effects.INVISIBILITY);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof PlayerEntity && !player.world.isRemote) {
			int manaCost = 2;
			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) player, manaCost, true);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				if (player.getActivePotionEffect(Effects.INVISIBILITY) != null) {
					player.removePotionEffect(Effects.INVISIBILITY);
				}

				player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, -42, true, true));
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
