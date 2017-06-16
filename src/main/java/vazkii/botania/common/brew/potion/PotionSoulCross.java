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
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibPotionNames;

public class PotionSoulCross extends PotionMod {

	public PotionSoulCross() {
		super(LibPotionNames.SOUL_CROSS, false, 0x47453d, 0);
		MinecraftForge.EVENT_BUS.register(this);
		setBeneficial();
	}

	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
		Entity killer = event.getSource().getTrueSource();
		if(killer != null && killer instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) killer;
			if(hasEffect(living))
				living.heal(event.getEntityLiving().getMaxHealth() / 20);
		}
	}

}
