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

import vazkii.botania.api.mana.ManaItemHandler;

public class RingOfTheMantleItem extends BaubleItem {

	public static final int MANA_COST = 5;
	public static final int HASTE_AMPLIFIER = 1; // Haste 2

	public RingOfTheMantleItem(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player player && !player.level().isClientSide) {
			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false);
			if (!hasMana) {
				onUnequipped(stack, player);
			} else {
				onEquipped(stack, player);
			}

			if (player.attackAnim == 0.25F) {
				ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, true);
			}
		}
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		boolean hasMana = living instanceof Player player
				&& ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false);
		MobEffectInstance effect = living.getEffect(MobEffects.DIG_SPEED);
		if (hasMana && (effect == null || effect.getAmplifier() < HASTE_AMPLIFIER
				|| effect.getAmplifier() == HASTE_AMPLIFIER && !effect.isInfiniteDuration())) {
			living.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, MobEffectInstance.INFINITE_DURATION, HASTE_AMPLIFIER, true, true));
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		MobEffectInstance effect = living.getEffect(MobEffects.DIG_SPEED);
		if (effect != null && effect.getAmplifier() == HASTE_AMPLIFIER && effect.isInfiniteDuration()) {
			living.removeEffect(MobEffects.DIG_SPEED);
		}
	}

}
