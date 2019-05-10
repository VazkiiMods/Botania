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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import top.theillusivec4.curios.api.CuriosAPI;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.integration.curios.BaseCurio;
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
		public void onCurioTick(String identifier, EntityLivingBase living) {
			if(living.isInWaterOrBubbleColumn()) {
				if(living instanceof EntityPlayer) {
				    // only activate for one ring at a time
					CuriosAPI.FinderData result = CuriosAPI.getCurioEquipped(ModItems.waterRing, living);
					if(result == null || result.getStack() != stack)
						return;
				}

				double motionX = living.motionX * SPEED_MULT;
				double motionY = living.motionY * SPEED_MULT;
				double motionZ = living.motionZ * SPEED_MULT;

				boolean flying = living instanceof EntityPlayer && ((EntityPlayer) living).abilities.isFlying;

				if(Math.abs(motionX) < MAX_SPEED && !flying)
					living.motionX = motionX;
				if(Math.abs(motionY) < MAX_SPEED && !flying)
					living.motionY = motionY;
				if(Math.abs(motionZ) < MAX_SPEED && !flying)
					living.motionZ = motionZ;

				PotionEffect effect = living.getActivePotionEffect(MobEffects.NIGHT_VISION);
				if(effect == null) {
					PotionEffect neweffect = new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, true);
					living.addPotionEffect(neweffect);
				}

				if(living.getAir() <= 1 && living instanceof EntityPlayer) {
					int mana = ManaItemHandler.requestMana(stack, (EntityPlayer) living, 300, true);
					if(mana > 0)
						living.setAir(mana);
				}
			} else onUnequipped(identifier, living);
		}

		@Override
		public void onUnequipped(String identifier, EntityLivingBase living) {
			PotionEffect effect = living.getActivePotionEffect(MobEffects.NIGHT_VISION);
			if(effect != null && effect.getAmplifier() == -42)
				living.removePotionEffect(MobEffects.NIGHT_VISION);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
