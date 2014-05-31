/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 31, 2014, 9:58:26 PM (GMT)]
 */
package vazkii.botania.common.core.version;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public final class VersionChecker {

	public static boolean doneChecking = false;
	public static String onlineVersion = "";
	public static boolean triedToWarnPlayer = false;
	
	public void init() {
		new ThreadVersionChecker();
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(doneChecking && event.phase == Phase.END && Minecraft.getMinecraft().thePlayer != null && !triedToWarnPlayer) {
			if(!onlineVersion.isEmpty()) {
				// TODO
			}
			
			triedToWarnPlayer = true;
		}
	}
	
}
