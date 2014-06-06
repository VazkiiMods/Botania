/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 6:32:05 PM (GMT)]
 */
package vazkii.botania.common.lib;

import net.minecraftforge.common.util.ForgeDirection;

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "Botania";
	public static final String MOD_NAME = MOD_ID;
	public static final String BUILD = "ANT:BUILD";
	public static final String VERSION = "ANT:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:Forge@[10.12.0.1065,);";

	// Network Contants
	public static final String NETWORK_CHANNEL = MOD_ID;

	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.botania.common.core.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.botania.client.core.proxy.ClientProxy";
	
	public static final ForgeDirection[] CARDINAL_DIRECTIONS = new ForgeDirection[] {
		ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST
	};

}
