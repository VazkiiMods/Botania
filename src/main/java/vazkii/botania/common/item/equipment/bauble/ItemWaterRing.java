/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 3:44:24 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final int COST = 3;
	private static final int MAGIC_AMPLIFIER = 123;

	public ItemWaterRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if(living.isInWaterOrBubbleColumn()) {
			// only activate for one ring at a time
			ItemStack result = EquipmentHandler.findOrEmpty(ModItems.waterRing, living);
			if(result != stack)
				return;

			if (!living.world.isRemote) {
				if (living instanceof PlayerEntity && !ManaItemHandler.requestManaExact(stack, (PlayerEntity) living, COST, true)) {
					onUnequipped(stack, living);
				} else {
					addEffect(living, Effects.CONDUIT_POWER);
					addEffect(living, Effects.DOLPHINS_GRACE);
				}
			}
		} else onUnequipped(stack, living);
	}

	private static void addEffect(LivingEntity living, Effect effect) {
		EffectInstance inst = living.getActivePotionEffect(effect);
		if (inst == null) {
			EffectInstance neweffect = new EffectInstance(effect, Short.MAX_VALUE, MAGIC_AMPLIFIER, true, true);
			living.addPotionEffect(neweffect);
		}
	}

	private static void removeEffect(LivingEntity living, Effect effect) {
		EffectInstance inst = living.getActivePotionEffect(effect);
		if (inst != null && inst.getAmplifier() == MAGIC_AMPLIFIER) {
			living.removePotionEffect(effect);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		if (!living.world.isRemote) {
			removeEffect(living, Effects.CONDUIT_POWER);
			removeEffect(living, Effects.DOLPHINS_GRACE);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
