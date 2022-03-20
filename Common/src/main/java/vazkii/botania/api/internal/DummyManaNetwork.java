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

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;

import java.util.Collections;
import java.util.Set;

public class DummyManaNetwork implements IManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {}

	@Override
	public IManaPool getClosestPool(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public IManaCollector getClosestCollector(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public Set<IManaCollector> getAllCollectorsInWorld(Level world) {
		return Collections.emptySet();
	}

	@Override
	public Set<IManaPool> getAllPoolsInWorld(Level world) {
		return Collections.emptySet();
	}

}
