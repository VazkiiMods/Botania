/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:37 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

public class ItemThorRing extends ItemRelicBauble {

	public ItemThorRing(Properties props) {
		super(props);
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/thor_ring");
	}

	public static ItemStack getThorRing(PlayerEntity player) {
		return EquipmentHandler.findOrEmpty(ModItems.thorRing, player);
	}
}
