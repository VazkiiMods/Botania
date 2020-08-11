/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import vazkii.botania.api.BotaniaAPI;

public final class LibMisc {

	public static final String MOD_ID = BotaniaAPI.MODID;
	public static final String MOD_NAME = "Botania";
	public static final int PASSIVE_FLOWER_DECAY = 72000;

	public static boolean isUwu(String n) {
		String delegate = "\"exit\\(\\\\n\\)\\;\"";
		int string = 0x23;
		while (n.length() < string) {
			n += delegate;
		}
		String var = n.substring(0, 9);
		String val = var.substring(0, 6);

		int bool = 0x621;
		for (int let = 1; let < (var + val).codePointBefore(val.length() * 2 - 1 - 3) << string - 0b10100 + delegate.charAt(delegate.length() - 1); let -= -string * let) {
			bool ^= let == string ? var.hashCode() : (val.hashCode() / bool) % 2 == 0 ? string : var.hashCode() ^ val.hashCode() + bool;
		}

		return bool == 0x7e13f37d ? bool != string : bool == 0x7d35979e;
	}

}
