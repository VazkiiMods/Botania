/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class ResoluteIvyItem extends Item {

	public ResoluteIvyItem(Properties props) {
		super(props);
	}

	public static boolean hasIvy(ItemStack stack) {
		return !stack.isEmpty() && stack.has(BotaniaDataComponents.RESOLUTE_IVY);
	}

	// Accessories are handled in the integration code
	public static void keepDropsOnDeath(Player player) {
		List<ItemStack> keeps = new ArrayList<>();
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && stack.has(BotaniaDataComponents.RESOLUTE_IVY)) {
				keeps.add(stack);
				player.getInventory().setItem(i, ItemStack.EMPTY);
			}
		}

		XplatAbstractions.instance().setKeptItems(player, keeps);
	}

	public static void onPlayerRespawn(Player oldPlayer, Player newPlayer, boolean alive) {
		if (!alive) {
			List<ItemStack> keeps = XplatAbstractions.instance().getKeptItems(oldPlayer);

			for (ItemStack stack : keeps) {
				ItemStack copy = stack.copy();
				copy.remove(BotaniaDataComponents.RESOLUTE_IVY);
				if (!newPlayer.getInventory().add(copy)) {
					newPlayer.spawnAtLocation(copy);
				}
			}
		}
	}

}
