/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2016, 11:59:12 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ManaItemsEvent extends Event {

	private final PlayerEntity entityPlayer;
	private List<ItemStack> items;

	public ManaItemsEvent(PlayerEntity entityPlayer, List<ItemStack> items) {
		this.entityPlayer = entityPlayer;
		this.items = items;
	}

	public PlayerEntity getEntityPlayer() {
		return entityPlayer;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void add(ItemStack item) {
		items.add(item);
	}
}
