/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 22, 2014, 3:13:17 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class BaubleLoadHandler {

	public static Set<EntityPlayer> tickedPlayers = Collections.newSetFromMap(new WeakHashMap());
	public static Set<EntityPlayer> tickingPlayers = Collections.newSetFromMap(new WeakHashMap());

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTick(LivingUpdateEvent event) {
		if(event.entity instanceof EntityPlayer && tickingPlayers.contains(event.entity)) {
			tickingPlayers.remove(event.entity);
			tickedPlayers.add((EntityPlayer) event.entity);
		}
	}
	
}
