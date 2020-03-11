/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.capability;

import net.minecraft.item.ItemStack;

import vazkii.botania.api.item.IFloatingFlower;

public class FloatingFlowerImpl implements IFloatingFlower {
	private IslandType type = IslandType.GRASS;

	@Override
	public ItemStack getDisplayStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}
}
