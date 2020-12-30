/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

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

	public ItemKeepIvy(Settings props) {
		super(props);
	}

	public static boolean hasIvy(ItemStack stack) {
		return !stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false);
	}

	// Curios are handled in CurioIntegration#keepCurioDrops
	public static void onPlayerDrops(PlayerEntity player) {
		List<ItemStack> keeps = new ArrayList<>();
		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack stack = player.inventory.getStack(i);
			if (!stack.isEmpty() && stack.hasTag() && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false)) {
				keeps.add(stack);
				player.inventory.setStack(i, ItemStack.EMPTY);
			}
		}

		if (keeps.size() > 0) {
			KeptItemsComponent data = EntityComponents.KEPT_ITEMS.get(player);
			data.addAll(keeps);
		}
	}

	public static void onPlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		if (!alive) {
			KeptItemsComponent keeps = EntityComponents.KEPT_ITEMS.get(oldPlayer);

			for (ItemStack stack : keeps.getStacks()) {
				ItemStack copy = stack.copy();
				copy.removeSubTag(TAG_KEEP);
				if (!newPlayer.inventory.insertStack(copy)) {
					newPlayer.dropStack(copy);
				}
			}
		}
	}

}
