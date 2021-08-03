/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final int COST = 3;

	public ItemWaterRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (living.isInWaterOrBubble()) {
			// only activate for one ring at a time
			ItemStack result = EquipmentHandler.findOrEmpty(ModItems.waterRing, living);
			if (result != stack) {
				return;
			}

			if (!living.level.isClientSide) {
				if (living instanceof Player && !ManaItemHandler.instance().requestManaExact(stack, (Player) living, COST, true)) {
					onUnequipped(stack, living);
				} else {
					addEffect(living, MobEffects.CONDUIT_POWER);
					addEffect(living, MobEffects.DOLPHINS_GRACE);
				}
			}
		} else {
			onUnequipped(stack, living);
		}
	}

	private static void addEffect(LivingEntity living, MobEffect effect) {
		MobEffectInstance inst = living.getEffect(effect);
		if (inst == null || (inst.getAmplifier() == 0 && inst.getDuration() == 1)) {
			MobEffectInstance neweffect = new MobEffectInstance(effect, 100, 0, true, true);
			living.addEffect(neweffect);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
