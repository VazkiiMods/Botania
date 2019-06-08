/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 2, 2014, 10:31:48 PM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class PotionSoulCross extends PotionMod {

	public PotionSoulCross() {
		super(false, 0x47453d, 0);
		setBeneficial();
	}

	@SubscribeEvent
	public static void onEntityKill(LivingDeathEvent event) {
		Entity killer = event.getSource().getTrueSource();
		if(killer instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) killer;
			if(living.isPotionActive(ModPotions.soulCross))
				living.heal(event.getEntityLiving().getMaxHealth() / 20);
		}
	}

}
