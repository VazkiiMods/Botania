/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationHelper {
	public static ResourceLocation prefix(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}
}
