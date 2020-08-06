/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.Direction;

import vazkii.botania.api.mana.IManaReceiver;

/**
 * A TileEntity that implements this is considered an Avatar.
 */
public interface IAvatarTile extends IManaReceiver {

	/**
	 * Gets the avatar's inventory
	 */
	public IInventory getInventory();

	/**
	 * Gets the avatar's facing.
	 */
	public Direction getAvatarFacing();

	/**
	 * Gets the amount of ticks that have elapsed on this avatar while it's functional
	 * (has redstone signal).
	 */
	public int getElapsedFunctionalTicks();

	/**
	 * Gets if this avatar is enabled (isn't powered by a redstone signal).
	 */
	public boolean isEnabled();

}
