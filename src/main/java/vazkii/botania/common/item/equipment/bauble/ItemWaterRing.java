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
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import top.theillusivec4.curios.api.CuriosAPI;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.integration.curios.CurioIntegration;
import vazkii.botania.common.item.ModItems;

public class ItemWaterRing extends ItemBauble implements IManaUsingItem {

	private static final double SPEED_MULT = 1.2;
	private static final double MAX_SPEED = 1.3;

	public ItemWaterRing(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onCurioTick(String identifier, LivingEntity living) {
			if(living.isInWaterOrBubbleColumn()) {
				if(living instanceof PlayerEntity) {
				    // only activate for one ring at a time
					ItemStack result = CurioIntegration.findOrEmpty(ModItems.waterRing, living);
					if(result != stack)
						return;
				}

				double motionX = living.motionX * SPEED_MULT;
				double motionY = living.motionY * SPEED_MULT;
				double motionZ = living.motionZ * SPEED_MULT;

				boolean flying = living instanceof PlayerEntity && ((PlayerEntity) living).abilities.isFlying;

				if(Math.abs(motionX) < MAX_SPEED && !flying)
					living.motionX = motionX;
				if(Math.abs(motionY) < MAX_SPEED && !flying)
					living.motionY = motionY;
				if(Math.abs(motionZ) < MAX_SPEED && !flying)
					living.motionZ = motionZ;

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
			} else onUnequipped(identifier, living);
		}

		@Override
		public void onUnequipped(String identifier, LivingEntity living) {
			EffectInstance effect = living.getActivePotionEffect(Effects.NIGHT_VISION);
			if(effect != null && effect.getAmplifier() == -42)
				living.removePotionEffect(Effects.NIGHT_VISION);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
