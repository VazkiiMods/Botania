/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 13, 2015, 10:52:40 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;

public class EntityCorporeaSpark extends Entity implements ICorporeaSpark {

	private static final int SCAN_RANGE = 8;
	
	private static final String TAG_MASTER = "master";
	private static final String TAG_NETWORK = "network";
	
	ICorporeaSpark master;
	List<ICorporeaSpark> connections = new ArrayList();
	List<ICorporeaSpark> relatives = new ArrayList();
	boolean firstUpdate = true;
	boolean didStartupParticles = false;
	
	public EntityCorporeaSpark(World world) {
		super(world);
		isImmuneToFire = true;
	}
	
	@Override
	protected void entityInit() {
		setSize(0.5F, 0.5F);
		dataWatcher.addObject(28, 0);
		dataWatcher.addObject(29, 0);

		dataWatcher.setObjectWatched(28);
		dataWatcher.setObjectWatched(29);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		IInventory inv = getInventory();
		if(inv == null) {
			if(!worldObj.isRemote)
				setDead();
			return;
		}
		
		if(isMaster())
			master = this;
		
		if(firstUpdate) {
			if(isMaster())
				restartNetwork();
			else {
				List<ICorporeaSpark> sparks = getNearbySparks();
				if(sparks.size() > 0) {
					for(ICorporeaSpark spark : sparks) {
						ICorporeaSpark master = spark.getMaster();
						if(master != null) {
							this.master = master;
							restartNetwork();
							break;
						}
					}
				}
			}
			
			firstUpdate = false;
		}
		
		if(!didStartupParticles) {
			displayRelatives(new ArrayList(), master);
			didStartupParticles = true;
		}
	}
	
	@Override
	public void setDead() {
		super.setDead();
		if(!worldObj.isRemote)
			entityDropItem(new ItemStack(ModItems.corporeaSpark, 1, isMaster() ? 1 : 0), 0F);
		connections.remove(this);
		restartNetwork();
	}

	@Override
	public void registerConnections(ICorporeaSpark master, ICorporeaSpark referrer, List<ICorporeaSpark> connections) {
		List<ICorporeaSpark> sparks = getNearbySparks();
		relatives.clear();
		for(ICorporeaSpark spark : sparks) {
			if(spark == null || connections.contains(spark) || spark.getNetwork() != getNetwork() || spark.isMaster())
				continue;
			
			connections.add(spark);
			relatives.add(spark);
			spark.registerConnections(master, this, connections);
		}
		
		this.master = master;
		this.connections = connections;
	}
	
	List<ICorporeaSpark> getNearbySparks() {
		return worldObj.getEntitiesWithinAABB(ICorporeaSpark.class, AxisAlignedBB.getBoundingBox(posX - SCAN_RANGE, posY - SCAN_RANGE, posZ - SCAN_RANGE, posX + SCAN_RANGE, posY + SCAN_RANGE, posZ + SCAN_RANGE));
	}
	
	public void restartNetwork() {
		if(master != null)
			master.registerConnections(master, this, new ArrayList());
	}
	
	void displayRelatives(ArrayList<ICorporeaSpark> checked, ICorporeaSpark spark) {
		if(spark == null)
			return;
		
		List<ICorporeaSpark> sparks = spark.getRelatives();
		if(sparks.isEmpty())
			EntitySpark.particleBeam((Entity) spark, (Entity) spark.getMaster());
		else for(ICorporeaSpark endSpark : sparks) {
			if(!checked.contains(endSpark)) {
				EntitySpark.particleBeam((Entity) spark, (Entity) endSpark);
				checked.add(endSpark);
				displayRelatives(checked, endSpark);
			}
		}
	}

	@Override
	public IInventory getInventory() {
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY - 1);
		int z = MathHelper.floor_double(posZ);
		return InventoryHelper.getInventory(worldObj, x, y, z);
	}

	@Override
	public List<ICorporeaSpark> getConnections() {
		return connections;
	}

	@Override
	public List<ICorporeaSpark> getRelatives() {
		return relatives;
	}
	
	@Override
	public ICorporeaSpark getMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		dataWatcher.updateObject(28, master ? 1 : 0);
	}
	
	@Override
	public boolean isMaster() {
		return dataWatcher.getWatchableObjectInt(28) == 1;
	}
	
	public void setNetwork(int network) {
		dataWatcher.updateObject(29, network);
	}

	@Override
	public int getNetwork() {
		return dataWatcher.getWatchableObjectInt(29);
	}
	
	@Override
	public boolean interactFirst(EntityPlayer player) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null) {
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					setDead();
					if(player.worldObj.isRemote)
						player.swingItem();
					return true;
				} else {
					didStartupParticles = false;
					return true;
				}
			} // TODO Dyeing
		}

		return false;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
		setMaster(cmp.getBoolean(TAG_MASTER));
		setNetwork(cmp.getInteger(TAG_NETWORK));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_MASTER, isMaster());
		cmp.setInteger(TAG_NETWORK, getNetwork());
	}

}
