/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.*;

import java.util.Set;

/**
 * A basic interface for a world's Mana Network.
 */
public interface ManaNetwork {

	/**
	 * Clears the entire Mana Network of all it's contents, you probably
	 * don't want to call this unless you have a very good reason.
	 */
	void clear();

	/**
	 * Gets read-only view of all Mana Collectors (eg. Mana Spreader) in the dimension
	 * passed in.
	 */
	Set<ManaCollector> getAllCollectorsInWorld(Level world);

	void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action);
}
