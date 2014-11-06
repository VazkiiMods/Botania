/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 21, 2014, 4:58:55 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import vazkii.botania.client.fx.ParticleRenderDispatcher;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class DebugHandler {

	private static final String PREFIX = EnumChatFormatting.GREEN + "[Botania] " + EnumChatFormatting.RESET;

	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
		World world = Minecraft.getMinecraft().theWorld;
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			event.left.add(null);
			event.left.add(PREFIX + "pS: " + ParticleRenderDispatcher.sparkleFxCount + ", pFS: " + ParticleRenderDispatcher.fakeSparkleFxCount + ", pW: " + ParticleRenderDispatcher.wispFxCount + ", pDIW: " + ParticleRenderDispatcher.depthIgnoringWispFxCount + ", pLB: " + ParticleRenderDispatcher.lightningCount);
			event.left.add(PREFIX + "netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size());
		}
	}


}
