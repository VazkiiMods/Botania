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
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.Set;

public class DummyManaNetwork implements IManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {}

	@Override
	public BlockEntity getClosestPool(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public BlockEntity getClosestCollector(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public Set<BlockEntity> getAllCollectorsInWorld(Level world) {
		return Collections.emptySet();
	}

	@Override
	public Set<BlockEntity> getAllPoolsInWorld(Level world) {
		return Collections.emptySet();
	}

}
