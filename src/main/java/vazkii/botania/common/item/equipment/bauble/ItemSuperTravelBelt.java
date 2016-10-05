/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 6:26:40 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSuperTravelBelt extends ItemTravelBelt {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SUPER_TRAVEL_BELT);

	public ItemSuperTravelBelt() {
		super(LibItemNames.SUPER_TRAVEL_BELT, 0.085F, 0.3F, 4F);
	}

	@Override
	public ResourceLocation getRenderTexture() {
		return texture;
	}
}
