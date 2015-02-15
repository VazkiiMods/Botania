/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 15, 2015, 12:42:29 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TileCorporeaIndex extends TileCorporeaBase {

	public static final double RADIUS = 2.5;
	
	public static final InputHandler input = new InputHandler();
	public static final Set<TileCorporeaIndex> indexes = Collections.newSetFromMap(new WeakHashMap());
	
	public int ticks = 0;
	public int ticksWithCloseby = 0;
	public float closeby = 0F;
	public boolean hasCloseby;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		double x = xCoord + 0.5;
		double y = yCoord + 0.5;
		double z = zCoord + 0.5;
		
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - RADIUS, y - RADIUS, z - RADIUS, x + RADIUS, y + RADIUS, z + RADIUS));
		hasCloseby = false;
		for(EntityPlayer player : players)
			if(isInRangeOfIndex(player, this)) {
				hasCloseby = true;
				break;
			}
		
		float step = 0.2F;
		ticks++;
		if(hasCloseby) {
			ticksWithCloseby++;
			if(closeby < 1F)
				closeby += step;
		} else if(closeby > 0F)
			closeby -= step;
		
		if(!isInvalid() && !indexes.contains(this))
			indexes.add(this);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		indexes.remove(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		indexes.remove(this);
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return null;
	}
	
	public static boolean isInRangeOfIndex(EntityPlayer player, TileCorporeaIndex index) {
		return MathHelper.pointDistanceSpace(index.xCoord + 0.5, index.yCoord + 0.5, index.zCoord + 0.5, player.posX, player.posY, player.posZ) < RADIUS;
	}
	
	public static final class InputHandler {
		
		@SubscribeEvent
		public void onChatMessage(ServerChatEvent event) {
			List<TileCorporeaIndex> nearbyIndexes = getNearbyIndexes(event.player);
			if(!nearbyIndexes.isEmpty()) {
				String msg = event.message.toLowerCase().trim();
				for(TileCorporeaIndex index : nearbyIndexes) {
					ICorporeaSpark spark = index.getSpark();
					if(spark != null) {
						List<ItemStack> stacks = CorporeaHelper.requestItem(msg, 64, spark, true);
						for(ItemStack stack : stacks)
							if(stack != null) {
								EntityItem item = new EntityItem(index.worldObj, index.xCoord + 0.5, index.yCoord + 1.5, index.zCoord + 0.5, stack);
								index.worldObj.spawnEntityInWorld(item);
							}
					}
				}
				
				event.setCanceled(true);
			}
		}
		
		public List<TileCorporeaIndex> getNearbyIndexes(EntityPlayer player) {
			List<TileCorporeaIndex> indexList = new ArrayList();
			for(TileCorporeaIndex index : indexes)
				if(isInRangeOfIndex(player, index) && index.worldObj.isRemote == player.worldObj.isRemote)
					indexList.add(index);
			return indexList;
		}
		
	}

}
