/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemThorRing extends ItemRelicBauble {

	public ItemThorRing(Settings props) {
		super(props);
	}

	@Override
	public Identifier getAdvancement() {
		return prefix("challenge/thor_ring");
	}

	public static ItemStack getThorRing(PlayerEntity player) {
		return EquipmentHandler.findOrEmpty(ModItems.thorRing, player);
	}
}
