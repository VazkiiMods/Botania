/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;

import vazkii.botania.mixin.AccessorModelLayers;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModModelLayers {
	public static final ModelLayerLocation AVATAR = register("avatar");
	public static final ModelLayerLocation PIXIE = register("pixie");

	private static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	private static ModelLayerLocation register(String name, String layer) {
		var ret = new ModelLayerLocation(prefix(name), layer);
		AccessorModelLayers.getAllModels().add(ret);
		return ret;
	}

	public static void init() {}
}
