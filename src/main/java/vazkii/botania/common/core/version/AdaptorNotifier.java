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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public final class AdaptorNotifier {

	boolean triedToWarnPlayer;
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(!triedToWarnPlayer && Minecraft.getMinecraft().thePlayer != null) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			ConfigHandler.adaptor.tellChanges(player);

			triedToWarnPlayer = true;
		}
	}
	
}
