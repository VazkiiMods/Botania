/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.mana.ManaItemHandler;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.helper.EntityHelper;
import org.violetmoon.botania.common.impl.mana.DeferredManaCharge;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.StoneOfTemperanceItem;

public class RingOfChordataItem extends BaubleItem {

	private static final int MANA_COST = 3;
	private static final int CONDUIT_POWER_AMPLIFIER = 0;
	private static final int DOLPHINS_GRACE_AMPLIFIER = 0;
	private static final int OUT_OF_MANA_DURATION = 60;

	public RingOfChordataItem(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (!(living instanceof Player player) || player.level().isClientSide()) {
			return;
		}
		if (player.isInWaterOrBubble()) {
			// only activate for one ring at a time
			ItemStack result = EquipmentHandler.findOrEmpty(BotaniaItems.RING_OF_CHORDATA, living);
			if (result != stack) {
				return;
			}

			// Batched, so the paying mana item isn't rewritten every tick
			boolean hasMana = DeferredManaCharge.canAfford(stack, player, MANA_COST);
			if (hasMana) {
				EntityHelper.addStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);

				if (StoneOfTemperanceItem.hasTemperanceActive(player)) {
					EntityHelper.removeStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
				} else {
					EntityHelper.addStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
				}
				DeferredManaCharge.charge(stack, player, MANA_COST);
			} else {
				// convert to short finite duration to prevent flickering and other weird behavior in cases where the
				// player is out of mana and an aura band causes the ring to temporarily turn one in short intervals
				EntityHelper.convertStaticEffectToFinite(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER,
						OUT_OF_MANA_DURATION);
				EntityHelper.convertStaticEffectToFinite(living, MobEffects.DOLPHINS_GRACE,
						DOLPHINS_GRACE_AMPLIFIER, OUT_OF_MANA_DURATION);
			}
		} else {
			onUnequipped(stack, living);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		if (living instanceof Player player && player.isInWaterOrBubble()
				&& ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false)) {
			EntityHelper.addStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);
			if (!StoneOfTemperanceItem.hasTemperanceActive(player)) {
				EntityHelper.addStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		EntityHelper.removeStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);
		EntityHelper.removeStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
	}

}
