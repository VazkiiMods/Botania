/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 3, 2014, 9:59:17 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import vazkii.botania.client.core.handler.LightningHandler.LightningBolt;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.lib.LibMisc;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// NO-OP
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		LightningBolt.update();

		if(Minecraft.getMinecraft().theWorld == null)
			ManaNetworkHandler.instance.clear();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return LibMisc.MOD_ID;
	}

}
