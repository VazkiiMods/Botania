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

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "botania";
	public static final String MOD_NAME = "Botania";
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:baubles@[1.5.2,);after:thaumcraft@[6.1.BETA21,);after:jei@[1.12.2-4.13.1.220,);after:albedo@[1.0.0,)";

	// Network Contants
	public static final String NETWORK_CHANNEL = MOD_ID;

	// Proxy Constants
	public static final String PROXY_SERVER = "vazkii.botania.common.core.proxy.ServerProxy";
	public static final String PROXY_CLIENT = "vazkii.botania.client.core.proxy.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.botania.client.core.proxy.GuiFactory";

	// IMC Keys
	public static final String BLACKLIST_ITEM = "blackListItem";

	public static final int[] CONTROL_CODE_COLORS = new int[] {
			0x000000, 0x0000AA, 0x00AA00, 0x00AAAA,
			0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA,
			0x555555, 0x5555FF, 0x55FF55, 0x55FFFF,
			0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF
	};

	public static final int PASSIVE_FLOWER_DECAY = 72000;

}
