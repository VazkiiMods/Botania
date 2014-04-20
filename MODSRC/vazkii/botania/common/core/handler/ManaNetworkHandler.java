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

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.ManaNetworkEvent.Action;
import vazkii.botania.api.mana.ManaNetworkEvent.ManaBlockType;
import vazkii.botania.common.core.helper.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ManaNetworkHandler implements IManaNetwork {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();

	public Map<Integer, List<TileEntity>> manaPools = new HashMap();
	public Map<Integer, List<TileEntity>> manaCollectors = new HashMap();

	@SubscribeEvent
	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<Integer, List<TileEntity>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
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
	public TileEntity getClosestPool(ChunkCoordinates pos, int dimension, int limit) {
		if(manaPools.containsKey(dimension))
			return getClosest(manaPools.get(dimension), pos, limit);
		return null;
	}

	@Override
	public TileEntity getClosestCollector(ChunkCoordinates pos, int dimension, int limit) {
		if(manaCollectors.containsKey(dimension))
			return getClosest(manaCollectors.get(dimension), pos, limit);
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

	private synchronized void remove(Map<Integer, List<TileEntity>> map, TileEntity tile) {
		int dim = tile.getWorldObj().provider.dimensionId;

		if(!map.containsKey(dim))
			return;

		List<TileEntity> tiles = map.get(dim);
		tiles.remove(tile);
	}

	private synchronized void add(Map<Integer, List<TileEntity>> map, TileEntity tile) {
		int dim = tile.getWorldObj().provider.dimensionId;

		List<TileEntity> tiles;
		if(!map.containsKey(dim))
			map.put(dim, new ArrayList());

		tiles = map.get(dim);

		if(!tiles.contains(tile))
			tiles.add(tile);
	}

	@Override
	public List<TileEntity> getAllCollectorsInWorld(int dim) {
		return getAllInWorld(manaCollectors, dim);
	}

	@Override
	public List<TileEntity> getAllPoolsInWorld(int dim) {
		return getAllInWorld(manaPools, dim);
	}

	private List<TileEntity> getAllInWorld(Map<Integer, List<TileEntity>> map, int dim) {
		if(!map.containsKey(dim))
			return new ArrayList();
		return map.get(dim);
	}
}
