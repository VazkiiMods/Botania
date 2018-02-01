/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 5:10:16 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class CommonTickHandler {

	private CommonTickHandler() {}

	@SubscribeEvent
	public static void onTick(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			CorporeaHelper.clearCache();
		}
	}

}
