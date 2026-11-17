/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.relic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.item.Relic;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.item.BotaniaItems;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class RingOfThorItem extends RelicBaubleItem {

	public RingOfThorItem(Properties props) {
		super(props);
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, botaniaRL("challenge/ring_of_thor"));
	}

	public static ItemStack getThorRing(Player player) {
		return EquipmentHandler.findOrEmpty(BotaniaItems.RING_OF_THOR, player);
	}
}
