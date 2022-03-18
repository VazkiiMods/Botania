/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Any TileEntity that implements this can receive mana from mana bursts.
 */
public interface IManaReceiver {
	/**
	 * Gets the amount of mana currently in this block.
	 */
	int getCurrentMana();

	/**
	 * @return the TileEntity underlying this Mana Block
	 */
	default BlockEntity tileEntity() {
		return (BlockEntity) this;
	}

	/**
	 * Is this Mana Receiver is full? Being full means no mana bursts will be sent.
	 */
	boolean isFull();

	/**
	 * Called when this receiver receives mana.
	 */
	void receiveMana(int mana);

	/**
	 * Can this tile receive mana from bursts? Generally set to false for
	 * implementations of IManaCollector.
	 */
	boolean canReceiveManaFromBursts();

}
