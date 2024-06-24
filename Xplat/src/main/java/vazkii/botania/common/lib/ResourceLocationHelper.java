/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
	public static ResourceLocation prefix(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	public static ModelResourceLocation modelResourceLocation(String path, String variant) {
		return new ModelResourceLocation(prefix(path), variant);
	}
}
