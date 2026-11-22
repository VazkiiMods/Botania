/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.impl.mana;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.mana.ManaItemHandler;

import java.util.UUID;

/**
 * Server-side batching of small mana costs that items pay every tick.
 * <p>
 * Taking mana out of a mana item rewrites its data component. For a mana band in an accessory slot, that makes the
 * accessory mod resend the whole item stack to the wearer and every tracking player, so paying every tick meant a
 * full item packet per tick. This class keeps the cost owed per player and requesting item, and takes it out of the
 * mana items at most every {@link #SETTLE_INTERVAL} ticks.
 * <p>
 * Every tick, the check still asks for the whole outstanding amount (owed plus this tick's cost) in a simulated
 * request, so whether an item has mana to work on a given tick comes out the same as when paying every tick.
 * Known differences, all bounded by {@link #SETTLE_INTERVAL} ticks of cost: mana owed when the player removes the
 * paying mana item before settlement is taken from what is left (and dropped if nothing is left); mana owed by an
 * item the player stopped using is only taken at its next use or at logout; and two batched costs of the same
 * player only see their own owed mana in the check.
 * <p>
 * Server thread only. Client calls must keep using {@link ManaItemHandler} directly.
 */
public final class DeferredManaCharge {
	private static final int SETTLE_INTERVAL = 10;

	private record Key(UUID player, Item requester) {
	}

	private static final Object2IntMap<Key> OWED = new Object2IntOpenHashMap<>();
	private static final Object2LongMap<Key> LAST_SETTLED = new Object2LongOpenHashMap<>();

	private DeferredManaCharge() {}

	private static Key key(ItemStack requester, Player player) {
		return new Key(player.getUUID(), requester.getItem());
	}

	private static long now(Player player) {
		return player.level().getGameTime();
	}

	/**
	 * Whether the player could pay {@code cost} mana for {@code requester} this tick, taking mana that is owed but
	 * not yet taken into account. Equivalent of {@code requestManaExact(requester, player, cost, false)}.
	 */
	public static boolean canAfford(ItemStack requester, Player player, int cost) {
		int owed = OWED.getInt(key(requester, player));
		return ManaItemHandler.instance().requestManaExact(requester, player, owed + cost, false);
	}

	/**
	 * Pays {@code cost} mana for {@code requester}. Equivalent of
	 * {@code requestManaExact(requester, player, cost, true)}: returns false and takes nothing for this tick if the
	 * cost can't be paid in full.
	 */
	public static boolean charge(ItemStack requester, Player player, int cost) {
		if (cost <= 0) {
			return ManaItemHandler.instance().requestManaExact(requester, player, cost, true);
		}
		Key key = key(requester, player);
		int owed = OWED.getInt(key);
		if (!ManaItemHandler.instance().requestManaExact(requester, player, owed + cost, false)) {
			settle(key, player);
			return false;
		}
		OWED.put(key, owed + cost);
		if (!LAST_SETTLED.containsKey(key)) {
			LAST_SETTLED.put(key, now(player));
		} else if (now(player) - LAST_SETTLED.getLong(key) >= SETTLE_INTERVAL) {
			settle(key, player);
		}
		return true;
	}

	/** Takes all mana owed for {@code requester} now, e.g. when the item stops being used. */
	public static void settle(ItemStack requester, Player player) {
		settle(key(requester, player), player);
	}

	private static void settle(Key key, Player player) {
		int owed = OWED.removeInt(key);
		LAST_SETTLED.put(key, now(player));
		if (owed > 0) {
			// The requesting stack only serves to filter mana items by the requesting item type and to skip itself,
			// so a fresh stack of the same item works, also after the original has left the player.
			ItemStack requester = new ItemStack(key.requester());
			if (!ManaItemHandler.instance().requestManaExact(requester, player, owed, true)) {
				// the paying item was removed or drained in the meantime: take what is left
				ManaItemHandler.instance().requestMana(requester, player, owed, true);
			}
		}
	}

	/** Settles everything the player owes and forgets them. */
	public static void onPlayerLoggedOut(Player player) {
		UUID id = player.getUUID();
		for (Key key : OWED.keySet().toArray(new Key[0])) {
			if (key.player().equals(id)) {
				settle(key, player);
			}
		}
		OWED.keySet().removeIf(k -> k.player().equals(id));
		LAST_SETTLED.keySet().removeIf(k -> k.player().equals(id));
	}
}
