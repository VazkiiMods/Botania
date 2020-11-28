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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final int COST = 3;

	public ItemWaterRing(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (living.isInsideWaterOrBubbleColumn()) {
			// only activate for one ring at a time
			ItemStack result = EquipmentHandler.findOrEmpty(ModItems.waterRing, living);
			if (result != stack) {
				return;
			}

			if (!living.world.isClient) {
				if (living instanceof PlayerEntity && !ManaItemHandler.instance().requestManaExact(stack, (PlayerEntity) living, COST, true)) {
					onUnequipped(stack, living);
				} else {
					addEffect(living, StatusEffects.CONDUIT_POWER);
					addEffect(living, StatusEffects.DOLPHINS_GRACE);
				}
			}
		} else {
			onUnequipped(stack, living);
		}
	}

	private static void addEffect(LivingEntity living, StatusEffect effect) {
		StatusEffectInstance inst = living.getStatusEffect(effect);
		if (inst == null || (inst.getAmplifier() == 0 && inst.getDuration() == 1)) {
			StatusEffectInstance neweffect = new StatusEffectInstance(effect, 100, 0, true, true);
			living.addStatusEffect(neweffect);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
