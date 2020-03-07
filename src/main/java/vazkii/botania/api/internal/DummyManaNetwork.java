/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.internal;

import com.google.common.collect.ImmutableSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class DummyManaNetwork implements IManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {}

	@Override
	public TileEntity getClosestPool(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public TileEntity getClosestCollector(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public Set<TileEntity> getAllCollectorsInWorld(World world) {
		return ImmutableSet.of();
	}

	@Override
	public Set<TileEntity> getAllPoolsInWorld(World world) {
		return ImmutableSet.of();
	}

}
