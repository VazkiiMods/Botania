/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.util.Identifier;

import vazkii.botania.client.lib.LibResources;

public class ItemSuperTravelBelt extends ItemTravelBelt {

	private static final Identifier texture = new Identifier(LibResources.MODEL_SUPER_TRAVEL_BELT);

	public ItemSuperTravelBelt(Settings props) {
		super(props, 0.085F, 0.3F, 4F);
	}

	@Override
	public Identifier getRenderTexture() {
		return texture;
	}
}
