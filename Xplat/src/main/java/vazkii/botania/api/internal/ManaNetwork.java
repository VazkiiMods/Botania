/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

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
	 * Gets the closest Mana Collector (eg. Mana Spreader) in the network to the Chunk
	 * Coordinates passed in, in the given dimension.<br>
	 * Note that this function *can* get performance intensive, it's reccomended you
	 * call it sparingly and take cache of the value returned.
	 * 
	 * @param limit The maximum distance the closest block can be, if the closest block
	 *              is farther away than that, null will be returned instead.
	 */
	@Nullable
	ManaCollector getClosestCollector(BlockPos pos, Level world, int limit);

	/**
	 * Gets the closest Mana Pool in the network to the Chunk Coordinates passed in,
	 * in the given dimension.<br>
	 * Note that this function *can* get performance intensive, it's reccomended you
	 * call it sparingly and take cache of the value returned.
	 * 
	 * @param limit The maximum distance the closest block can be, if the closest block
	 *              is farther away than that, null will be returned instead.
	 */
	@Nullable
	ManaPool getClosestPool(BlockPos pos, Level world, int limit);

	/**
	 * Gets read-only view of all Mana Collectors (eg. Mana Spreader) in the dimension
	 * passed in.
	 */
	Set<ManaCollector> getAllCollectorsInWorld(Level world);

	/**
	 * Gets read-only view of all Mana Pools in the dimension passed in.
	 */
	Set<ManaPool> getAllPoolsInWorld(Level world);

	void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action);
}
