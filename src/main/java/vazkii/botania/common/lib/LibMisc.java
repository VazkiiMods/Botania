/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 13, 2014, 6:32:05 PM (GMT)]
 */
package vazkii.botania.common.lib;

import net.minecraftforge.common.util.ForgeDirection;

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "Botania";
	public static final String MOD_NAME = MOD_ID;
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:Forge@[10.12.2.1147,);required-after:Baubles;after:Thaumcraft";

	// Network Contants
	public static final String NETWORK_CHANNEL = MOD_ID;

	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.botania.common.core.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.botania.client.core.proxy.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.botania.client.core.proxy.GuiFactory";

	public static final ForgeDirection[] CARDINAL_DIRECTIONS = new ForgeDirection[] {
		ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST
	};

}
