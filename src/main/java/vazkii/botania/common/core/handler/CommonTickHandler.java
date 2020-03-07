/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class CommonTickHandler {

	private CommonTickHandler() {}

	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			CorporeaHelper.clearCache();
		}
	}

}
