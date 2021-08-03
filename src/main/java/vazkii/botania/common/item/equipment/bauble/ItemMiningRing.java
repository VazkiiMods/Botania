/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemMiningRing extends ItemBauble implements IManaUsingItem {

	public ItemMiningRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof Player && !player.level.isClientSide) {
			int manaCost = 5;
			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, (Player) player, manaCost, false);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				if (player.getEffect(MobEffects.DIG_SPEED) != null) {
					player.removeEffect(MobEffects.DIG_SPEED);
				}

				player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, Integer.MAX_VALUE, 1, true, true));
			}

			if (player.attackAnim == 0.25F) {
				ManaItemHandler.instance().requestManaExact(stack, (Player) player, manaCost, true);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity player) {
		MobEffectInstance effect = player.getEffect(MobEffects.DIG_SPEED);
		if (effect != null && effect.getAmplifier() == 1) {
			player.removeEffect(MobEffects.DIG_SPEED);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
