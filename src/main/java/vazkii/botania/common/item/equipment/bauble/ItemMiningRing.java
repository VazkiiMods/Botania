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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemMiningRing extends ItemBauble implements IManaUsingItem {

	public ItemMiningRing(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (player instanceof PlayerEntity && !player.world.isClient) {
			int manaCost = 5;
			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) player, manaCost, false);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				if (player.getStatusEffect(StatusEffects.HASTE) != null) {
					player.removeStatusEffect(StatusEffects.HASTE);
				}

				player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, Integer.MAX_VALUE, 1, true, true));
			}

			if (player.handSwingProgress == 0.25F) {
				ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) player, manaCost, true);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity player) {
		StatusEffectInstance effect = player.getStatusEffect(StatusEffects.HASTE);
		if (effect != null && effect.getAmplifier() == 1) {
			player.removeStatusEffect(StatusEffects.HASTE);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
