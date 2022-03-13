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
 * Just a common interface to expose getCurrentMana from
 * {@link IManaSpreader} and {@link IManaReceiver}.
 * This interface is probably not very useful, see the two aforementioned.
 * There is no guarantee whether this interface is exposed via capabilities
 * or direct implementation.
 */
public interface IManaBlock {

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
}
