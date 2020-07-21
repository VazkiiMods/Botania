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
