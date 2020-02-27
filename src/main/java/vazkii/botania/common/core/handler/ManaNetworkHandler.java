/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 5:35:10 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.ManaNetworkEvent.Action;
import vazkii.botania.api.mana.ManaNetworkEvent.ManaBlockType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BinaryOperator;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	private final WeakHashMap<World, Set<TileEntity>> manaPools = new WeakHashMap<>();
	private final WeakHashMap<World, Set<TileEntity>> manaCollectors = new WeakHashMap<>();

	@SubscribeEvent
	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<World, Set<TileEntity>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
		if(event.action == Action.ADD)
			add(map, event.tile);
		else remove(map, event.tile);
	}

	@Override
	public void clear() {
		manaPools.clear();
		manaCollectors.clear();
	}

	@Override
	public TileEntity getClosestPool(BlockPos pos, World world, int limit) {
		if(manaPools.containsKey(world))
			return getClosest(manaPools.get(world), pos, world.isRemote, limit);
		return null;
	}

	@Override
	public TileEntity getClosestCollector(BlockPos pos, World world, int limit) {
		if(manaCollectors.containsKey(world))
			return getClosest(manaCollectors.get(world), pos, world.isRemote, limit);
		return null;
	}

	public boolean isCollectorIn(TileEntity tile) {
		return isIn(tile, manaCollectors);
	}

	public boolean isPoolIn(TileEntity tile) {
		return isIn(tile, manaPools);
	}

	private boolean isIn(TileEntity tile, Map<World, Set<TileEntity>> map) {
		Set<TileEntity> set = map.get(tile.getWorld());
		return set != null && set.contains(tile);
	}

	private TileEntity getClosest(Set<TileEntity> tiles, BlockPos pos, boolean remoteCheck, int limit) {
		return tiles.stream()
				.filter(t -> t.getWorld().isRemote == remoteCheck && !t.isRemoved()
						&& t.getPos().distanceSq(pos) <= limit * limit)
				.reduce(BinaryOperator.minBy(Comparator.comparing(t -> t.getPos().distanceSq(pos), Double::compare)))
				.orElse(null);
	}

	private void remove(Map<World, Set<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorld();

		if(!map.containsKey(world))
			return;

		map.get(world).remove(tile);
	}

	private void add(Map<World, Set<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorld();
		map.computeIfAbsent(world, k -> new HashSet<>()).add(tile);
	}

	@Override
	public Set<TileEntity> getAllCollectorsInWorld(World world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public Set<TileEntity> getAllPoolsInWorld(World world) {
		return getAllInWorld(manaPools, world);
	}

	private Set<TileEntity> getAllInWorld(Map<World, Set<TileEntity>> map, World world) {
		return map.getOrDefault(world, new HashSet<>());
	}

}
