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
	public static final ModelLayerLocation BELLOWS = register("bellows");
	public static final ModelLayerLocation BREWERY = register("brewery");
	public static final ModelLayerLocation CLOAK = register("cloak");
	public static final ModelLayerLocation CORPOREA_INDEX = register("corporea_index");
	public static final ModelLayerLocation HOURGLASS = register("hourglass");
	public static final ModelLayerLocation PIXIE = register("pixie");
	public static final ModelLayerLocation PYLON_GAIA = register("pylon_gaia");
	public static final ModelLayerLocation PYLON_MANA = register("pylon_mana");
	public static final ModelLayerLocation PYLON_NATURA = register("pylon_natura");
	public static final ModelLayerLocation TERU_TERU_BOZU = register("teru_teru_bozu");

	private static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	private static ModelLayerLocation register(String name, String layer) {
		var ret = new ModelLayerLocation(prefix(name), layer);
		AccessorModelLayers.getAllModels().add(ret);
		return ret;
	}

	// todo call from somewhere
	public static void init() {}
}
