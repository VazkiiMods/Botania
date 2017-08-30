/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [05/12/2015, 15:31:51 (GMT)]
 */
package vazkii.botania.common.core.version;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import vazkii.botania.common.core.handler.ConfigHandler;

public final class AdaptorNotifier {

	private AdaptorNotifier() {}

	private static boolean triedToWarnPlayer;

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if(!triedToWarnPlayer && Minecraft.getMinecraft().player != null) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ConfigHandler.adaptor.tellChanges(player);

			triedToWarnPlayer = true;
		}
	}

}
