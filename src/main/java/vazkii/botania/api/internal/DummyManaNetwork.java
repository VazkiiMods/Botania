/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 7, 2014, 3:47:43 PM (GMT)]
 */
package vazkii.botania.api.internal;

import java.util.List;

import com.google.common.collect.ImmutableList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.TileSignature;

public class DummyManaNetwork implements IManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {
		// NO-OP
	}

	@Override
	public TileEntity getClosestPool(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public TileEntity getClosestCollector(BlockPos pos, World world, int limit) {
		return null;
	}

	@Override
	public List<TileSignature> getAllCollectorsInWorld(World world) {
		return ImmutableList.of();
	}

	@Override
	public List<TileSignature> getAllPoolsInWorld(World world) {
		return ImmutableList.of();
	}

}
