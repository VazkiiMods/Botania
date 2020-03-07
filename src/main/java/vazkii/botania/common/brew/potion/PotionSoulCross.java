/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class PotionSoulCross extends Effect {

	public PotionSoulCross() {
		super(EffectType.BENEFICIAL, 0x47453d);
	}

	@SubscribeEvent
	public static void onEntityKill(LivingDeathEvent event) {
		Entity killer = event.getSource().getTrueSource();
		if (killer instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) killer;
			if (living.isPotionActive(ModPotions.soulCross)) {
				living.heal(event.getEntityLiving().getMaxHealth() / 20);
			}
		}
	}

}
