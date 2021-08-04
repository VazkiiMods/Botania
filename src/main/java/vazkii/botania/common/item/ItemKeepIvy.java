/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.KeptItemsComponent;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemKeepIvy extends Item {

	public static final String TAG_KEEP = "Botania_keepIvy";

	public static final String TAG_PLAYER_KEPT_DROPS = "Botania_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "dropCount";
	private static final String TAG_DROP_PREFIX = "dropPrefix";

	public ItemKeepIvy(Properties props) {
		super(props);
	}

	public static boolean hasIvy(ItemStack stack) {
		return !stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false);
	}

	// Curios are handled in CurioIntegration#keepCurioDrops
	public static void onPlayerDrops(Player player) {
		List<ItemStack> keeps = new ArrayList<>();
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false)) {
				keeps.add(stack);
				player.getInventory().setItem(i, ItemStack.EMPTY);
			}
		}

		if (keeps.size() > 0) {
			KeptItemsComponent data = EntityComponents.KEPT_ITEMS.get(player);
			data.addAll(keeps);
		}
	}

	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		if (!alive) {
			KeptItemsComponent keeps = EntityComponents.KEPT_ITEMS.get(oldPlayer);

			for (ItemStack stack : keeps.getStacks()) {
				ItemStack copy = stack.copy();
				copy.removeTagKey(TAG_KEEP);
				if (!newPlayer.inventory.add(copy)) {
					newPlayer.spawnAtLocation(copy);
				}
			}
		}
	}

}
