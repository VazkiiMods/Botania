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

public class ItemMiningRing extends ItemBauble implements IManaUsingItem {

	public ItemMiningRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof PlayerEntity && !player.world.isRemote) {
			int manaCost = 5;
			boolean hasMana = ManaItemHandler.requestManaExact(stack, (PlayerEntity) player, manaCost, false);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				if (player.getActivePotionEffect(Effects.HASTE) != null) {
					player.removePotionEffect(Effects.HASTE);
				}

				player.addPotionEffect(new EffectInstance(Effects.HASTE, Integer.MAX_VALUE, 1, true, true));
			}

			if (player.swingProgress == 0.25F) {
				ManaItemHandler.requestManaExact(stack, (PlayerEntity) player, manaCost, true);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity player) {
		EffectInstance effect = player.getActivePotionEffect(Effects.HASTE);
		if (effect != null && effect.getAmplifier() == 1) {
			player.removePotionEffect(Effects.HASTE);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
