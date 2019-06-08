/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 25, 2016, 9:02:12 PM (GMT)]
 */
package vazkii.botania.api.mana;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ManaDiscountEvent extends Event {

	private final PlayerEntity entityPlayer;
	private float discount;
	private ItemStack tool;
	
	public ManaDiscountEvent(PlayerEntity entityPlayer, float discount, ItemStack tool) {
		this.entityPlayer = entityPlayer;
		this.discount = discount;
		this.tool = tool;
	}

	public ItemStack getTool() {
		return tool;
	}

	public PlayerEntity getEntityPlayer() {
		return entityPlayer;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
