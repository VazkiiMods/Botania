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
import net.minecraft.potion.EffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final double SPEED_MULT = 1.2;
	private static final double MAX_SPEED = 1.3;

	public ItemWaterRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if(living.isInWaterOrBubbleColumn()) {
			if(living instanceof LivingEntity) {
				// only activate for one ring at a time
				ItemStack result = EquipmentHandler.findOrEmpty(ModItems.waterRing, living);
				if(result != stack)
					return;
			}

			double motionX = living.getMotion().getX() * SPEED_MULT;
			double motionY = living.getMotion().getY() * SPEED_MULT;
			double motionZ = living.getMotion().getZ() * SPEED_MULT;
			double newMX = living.getMotion().getX();
			double newMY = living.getMotion().getY();
			double newMZ = living.getMotion().getZ();

			boolean flying = living instanceof PlayerEntity && ((PlayerEntity) living).abilities.isFlying;

			if(Math.abs(motionX) < MAX_SPEED && !flying)
				newMX = motionX;
			if(Math.abs(motionY) < MAX_SPEED && !flying)
				newMY = motionY;
			if(Math.abs(motionZ) < MAX_SPEED && !flying)
				newMZ = motionZ;
			living.setMotion(newMX, newMY, newMZ);

			EffectInstance effect = living.getActivePotionEffect(Effects.NIGHT_VISION);
			if(effect == null) {
				EffectInstance neweffect = new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, true);
				living.addPotionEffect(neweffect);
			}

			if(living.getAir() <= 1 && living instanceof PlayerEntity) {
				int mana = ManaItemHandler.requestMana(stack, (PlayerEntity) living, 300, true);
				if(mana > 0)
					living.setAir(mana);
			}
		} else onUnequipped(stack, living);
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		EffectInstance effect = living.getActivePotionEffect(Effects.NIGHT_VISION);
		if(effect != null && effect.getAmplifier() == -42)
			living.removePotionEffect(Effects.NIGHT_VISION);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
