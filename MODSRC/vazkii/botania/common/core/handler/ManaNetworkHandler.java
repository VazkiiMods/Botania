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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.ForgeSubscribe;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.ManaNetworkEvent.Action;
import vazkii.botania.api.mana.ManaNetworkEvent.ManaBlockType;
import vazkii.botania.common.core.helper.MathHelper;

public final class ManaNetworkHandler {

	public static final ManaNetworkHandler instance = new ManaNetworkHandler();
	
	public Map<Integer, List<TileEntity>> manaPools = new HashMap();
	public Map<Integer, List<TileEntity>> manaCollectors = new HashMap();

	@ForgeSubscribe
	public void onNetworkEvent(ManaNetworkEvent event) {
		Map<Integer, List<TileEntity>> map = event.type == ManaBlockType.COLLECTOR ? manaCollectors : manaPools;
		if(event.action == Action.ADD)
			add(map, event.tile);
		else remove(map, event.tile);
	}
	
	public void clear() {
		manaPools.clear();
		manaCollectors.clear();
	}
	
	public TileEntity getClosestPool(ChunkCoordinates pos, int dimension, int limit) {
		if(manaPools.containsKey(dimension))
			return getClosest(manaPools.get(dimension), pos, limit);
		return null;
	}

	public TileEntity getClosestCollector(ChunkCoordinates pos, int dimension, int limit) {
		if(manaCollectors.containsKey(dimension))
			return getClosest(manaCollectors.get(dimension), pos, limit);
		return null;
	}
	
	private TileEntity getClosest(List<TileEntity> tiles, ChunkCoordinates pos, int limit) {
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
	
	private void remove(Map<Integer, List<TileEntity>> map, TileEntity tile) {
		int dim = tile.worldObj.provider.dimensionId;
		
		if(!map.containsKey(dim))
			return;
		
		List<TileEntity> tiles = map.get(dim);
		tiles.remove(tile);
	}
	
	private void add(Map<Integer, List<TileEntity>> map, TileEntity tile) {
		int dim = tile.worldObj.provider.dimensionId;
		
		List<TileEntity> tiles;
		if(!map.containsKey(dim))
			map.put(dim, new ArrayList());
		
		tiles = map.get(dim);
		
		if(!tiles.contains(tile))
			tiles.add(tile);
	}
	
	public List<TileEntity> getAllInWorld(Map<Integer, List<TileEntity>> map, int dim) {
		if(!map.containsKey(dim))
			return new ArrayList();
		return map.get(dim);
	}
	
	public int getAmount(Map<Integer, List<TileEntity>> map, int dim) {
		if(!map.containsKey(dim))
			return 0;
		return map.get(dim).size();
	}
}
