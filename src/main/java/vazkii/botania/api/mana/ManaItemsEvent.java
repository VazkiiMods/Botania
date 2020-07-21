/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Fired when the mod is gathering all items on a player that have mana.
 * For example, if you are an accessory mod, you would listen to this event and add stacks in your
 * accessory slots to the list using {@link #add}.
 */
public class ManaItemsEvent extends Event {

	private final PlayerEntity entityPlayer;
	private List<ItemStack> items;

	public ManaItemsEvent(PlayerEntity entityPlayer, List<ItemStack> items) {
		this.entityPlayer = entityPlayer;
		this.items = items;
	}

	public PlayerEntity getPlayer() {
		return entityPlayer;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void add(ItemStack item) {
		items.add(item);
	}
}
