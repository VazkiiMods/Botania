/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
import vazkii.botania.common.core.helper.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	public WeakHashMap<World, List<TileEntity>> manaPools = new WeakHashMap();
	public WeakHashMap<World, List<TileEntity>> manaCollectors = new WeakHashMap();

	@SubscribeEvent
	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<World, List<TileEntity>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
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
			return getClosest(manaPools.get(world), pos, limit);
		return null;
	}

	@Override
	public TileEntity getClosestCollector(ChunkCoordinates pos, World world, int limit) {
		if(manaCollectors.containsKey(world))
			return getClosest(manaCollectors.get(world), pos, limit);
		return null;
	}

	private synchronized TileEntity getClosest(List<TileEntity> tiles, ChunkCoordinates pos, int limit) {
		float closest = Float.MAX_VALUE;
		TileEntity closestTile = null;

		for(TileEntity tile : tiles) {
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

	private synchronized void remove(Map<World, List<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorldObj();

		if(!map.containsKey(world))
			return;

		List<TileEntity> tiles = map.get(world);
		tiles.remove(tile);
	}

	private synchronized void add(Map<World, List<TileEntity>> map, TileEntity tile) {
		World world = tile.getWorldObj();

		List<TileEntity> tiles;
		if(!map.containsKey(world))
			map.put(world, new ArrayList());

		tiles = map.get(world);

		if(!tiles.contains(tile))
			tiles.add(tile);
	}

	@Override
	public List<TileEntity> getAllCollectorsInWorld(World world) {
		return getAllInWorld(manaCollectors, world);
	}

	@Override
	public List<TileEntity> getAllPoolsInWorld(World world) {
		return getAllInWorld(manaPools, world);
	}

	private List<TileEntity> getAllInWorld(Map<World, List<TileEntity>> map, World world) {
		if(!map.containsKey(world))
			return new ArrayList();
		
		return map.get(world);
	}
}
