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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.ManaNetworkEvent.Action;
import vazkii.botania.api.mana.ManaNetworkEvent.ManaBlockType;
import vazkii.botania.api.mana.TileSignature;
import vazkii.botania.common.core.helper.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	public WeakHashMap<World, List<TileSignature>> manaPools = new WeakHashMap();
	public WeakHashMap<World, List<TileSignature>> manaCollectors = new WeakHashMap();

	@SubscribeEvent
	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<World, List<TileSignature>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
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
	public TileEntity getClosestPool(ChunkCoordinates pos, World world, int limit) {
		if(manaPools.containsKey(world))
			return getClosest(manaPools.get(world), pos, world.isRemote, limit);
		return null;
	}

	@Override
	public TileEntity getClosestCollector(ChunkCoordinates pos, World world, int limit) {
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

	private synchronized boolean isIn(TileEntity tile, Map<World, List<TileSignature>> map) {
		List<TileSignature> list = map.get(tile.getWorldObj());
		if(list == null)
			return false;

		for(TileSignature sig : list)
			if(sig.tile == tile)
				return true;

		return false;
	}

	private synchronized TileEntity getClosest(List<TileSignature> tiles, ChunkCoordinates pos, boolean remoteCheck, int limit) {
		float closest = Float.MAX_VALUE;
		TileEntity closestTile = null;

		for(TileSignature sig : tiles) {
			if(sig.remoteWorld != remoteCheck)
				continue;

			TileEntity tile = sig.tile;
			if(tile.isInvalid())
				continue;
			float distance = MathHelper.pointDistanceSpace(tile.xCoord, tile.yCoord, tile.zCoord, pos.posX, pos.posY, pos.posZ);
			if(distance > limit)
				continue;

			if(distance < closest) {
				closest = distance;
				closestTile = tile;
			}
		}

		return closestTile;
	}

	private synchronized void remove(Map<World, List<TileSignature>> map, TileEntity tile) {
		World world = tile.getWorldObj();

		if(!map.containsKey(world))
			return;

		List<TileSignature> sigs = map.get(world);
		for(TileSignature sig : sigs)
			if(sig.tile.equals(tile)) {
				sigs.remove(sig);
				break;
			}
	}

	private synchronized void add(Map<World, List<TileSignature>> map, TileEntity tile) {
		World world = tile.getWorldObj();

		List<TileSignature> tiles;
		if(!map.containsKey(world))
			map.put(world, new ArrayList());

		tiles = map.get(world);

		if(!tiles.contains(tile))
			tiles.add(new TileSignature(tile, tile.getWorldObj().isRemote));
	}

	@Override
	public List<TileSignature> getAllCollectorsInWorld(World world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public List<TileSignature> getAllPoolsInWorld(World world) {
		return getAllInWorld(manaPools, world);
	}

	private List<TileSignature> getAllInWorld(Map<World, List<TileSignature>> map, World world) {
		if(!map.containsKey(world))
			return new ArrayList();

		return map.get(world);
	}


}
