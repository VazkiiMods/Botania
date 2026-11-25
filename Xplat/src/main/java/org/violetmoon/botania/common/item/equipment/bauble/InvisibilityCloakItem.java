/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.common.impl.mana.DeferredManaCharge;

public class InvisibilityCloakItem extends BaubleItem {

	public InvisibilityCloakItem(Properties props) {
		super(props);
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		MobEffectInstance effect = living.getEffect(MobEffects.INVISIBILITY);
		if (effect != null && effect.isInfiniteDuration()) {
			living.removeEffect(MobEffects.INVISIBILITY);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player player && !player.level().isClientSide()) {
			int manaCost = 2;
			// Batched, so the paying mana item isn't rewritten every tick
			boolean hasMana = DeferredManaCharge.charge(stack, player, manaCost);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				MobEffectInstance effect = player.getEffect(MobEffects.INVISIBILITY);
				if (effect == null || effect.getDuration() <= 1 && !effect.isInfiniteDuration()) {
					player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,
							MobEffectInstance.INFINITE_DURATION, 0, true, true));
				}
			}
		}
	}
}
