/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 3, 2014, 12:15:09 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.botania.common.lib.LibPotionNames;

public class PotionAllure extends PotionMod {

	public PotionAllure() {
		super(LibPotionNames.ALLURE, false, 0x0034E4, 5);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		EntityLivingBase e = event.entityLiving;
		if(e instanceof EntityPlayer && hasEffect(e)) {
			EntityFishHook hook = ((EntityPlayer) e).fishEntity;
			if(hook != null)
				hook.onUpdate();
		}
	}

}
