/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 4, 2014, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.botania.common.shedding.SheddingTracker;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class SheddingHandler {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entity.worldObj.isRemote)
			return;

		SheddingTracker.ShedPattern pattern = SheddingTracker.getShedPattern(event.entity);
		
		if(pattern != null) {
			if(event.entity.worldObj.rand.nextInt(pattern.getRate()) == 0)
				event.entity.entityDropItem(pattern.getItemStack(), 0.0F);
		}
	}
}
