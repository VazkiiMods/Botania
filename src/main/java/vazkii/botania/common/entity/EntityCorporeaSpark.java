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
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
	private static final String TAG_INVIS = "invis";

	ICorporeaSpark master;
	List<ICorporeaSpark> connections = new ArrayList();
	List<ICorporeaSpark> connectionsClient = new ArrayList();
	List<ICorporeaSpark> relatives = new ArrayList();
	boolean firstUpdateClient = true;
	boolean firstUpdateServer = true;

	public EntityCorporeaSpark(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		setSize(0.1F, 0.5F);
		dataWatcher.addObject(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY, 0);
		dataWatcher.addObject(28, 0);
		dataWatcher.addObject(29, 0);
		dataWatcher.addObject(30, 0);
		dataWatcher.addObject(31, new ItemStack(Blocks.stone, 0, 0));

		dataWatcher.setObjectWatched(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY);
		dataWatcher.setObjectWatched(28);
		dataWatcher.setObjectWatched(29);
		dataWatcher.setObjectWatched(30);
		dataWatcher.setObjectWatched(31);
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

		if(worldObj.isRemote ? firstUpdateClient : firstUpdateServer) {
			if(isMaster())
				restartNetwork();
			else findNetwork();

			if(worldObj.isRemote)
				firstUpdateClient = false;
			else firstUpdateServer = false;
		}

		if(master != null && (((Entity) master).isDead || master.getNetwork() != getNetwork()))
			master = null;

		int displayTicks = getItemDisplayTicks();
		if(displayTicks > 0)
			setItemDisplayTicks(displayTicks - 1);
		else if(displayTicks < 0)
			setItemDisplayTicks(displayTicks + 1);
	}

	@Override
	public void setDead() {
		super.setDead();
		if(!worldObj.isRemote)
			entityDropItem(new ItemStack(ModItems.corporeaSpark, 1, isMaster() ? 1 : 0), 0F);
		connections.remove(this);
		connectionsClient.remove(this);
		restartNetwork();
	}

	@Override
	public void registerConnections(ICorporeaSpark master, ICorporeaSpark referrer, List<ICorporeaSpark> connections) {
		List<ICorporeaSpark> sparks = getNearbySparks();
		relatives.clear();
		for(ICorporeaSpark spark : sparks) {
			if(spark == null || connections.contains(spark) || spark.getNetwork() != getNetwork() || spark.isMaster() || ((Entity) spark).isDead)
				continue;

			connections.add(spark);
			relatives.add(spark);
			spark.registerConnections(master, this, connections);
		}

		this.master = master;
		if(worldObj.isRemote)
			connectionsClient = connections;
		else this.connections = connections;
	}

	List<ICorporeaSpark> getNearbySparks() {
		return worldObj.getEntitiesWithinAABB(ICorporeaSpark.class, AxisAlignedBB.getBoundingBox(posX - SCAN_RANGE, posY - SCAN_RANGE, posZ - SCAN_RANGE, posX + SCAN_RANGE, posY + SCAN_RANGE, posZ + SCAN_RANGE));
	}

	void restartNetwork() {
		if(worldObj.isRemote)
			connectionsClient = new ArrayList();
		else connections = new ArrayList();
		relatives = new ArrayList();

		if(master != null) {
			ICorporeaSpark oldMaster = master;
			master = null;

			oldMaster.registerConnections(oldMaster, this, new ArrayList());
		}
	}

	void findNetwork() {
		List<ICorporeaSpark> sparks = getNearbySparks();
		if(sparks.size() > 0) {
			for(ICorporeaSpark spark : sparks)
				if(spark.getNetwork() == getNetwork() && !((Entity) spark).isDead) {
					ICorporeaSpark master = spark.getMaster();
					if(master != null) {
						this.master = master;
						restartNetwork();

						break;
					}
				}
		}
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
		return worldObj.isRemote ? connectionsClient : connections;
	}

	@Override
	public List<ICorporeaSpark> getRelatives() {
		return relatives;
	}

	@Override
	public void onItemExtracted(ItemStack stack) {
		setItemDisplayTicks(10);
		setDisplayedItem(stack);
	}

	@Override
	public void onItemsRequested(List<ItemStack> stacks) {
		if(!stacks.isEmpty()) {
			setItemDisplayTicks(-10);
			setDisplayedItem(stacks.get(0));
		}
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

	public int getItemDisplayTicks() {
		return dataWatcher.getWatchableObjectInt(30);
	}

	public void setItemDisplayTicks(int ticks) {
		dataWatcher.updateObject(30, ticks);
	}

	public ItemStack getDisplayedItem() {
		return dataWatcher.getWatchableObjectItemStack(31);
	}

	public void setDisplayedItem(ItemStack stack) {
		dataWatcher.updateObject(31, stack);
	}

	@Override
	public boolean interactFirst(EntityPlayer player) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null) {
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					setDead();
					if(isMaster())
						restartNetwork();
					if(player.worldObj.isRemote)
						player.swingItem();
					return true;
				} else {
					displayRelatives(new ArrayList(), master);
					return true;
				}
			} else if(stack.getItem() == ModItems.dye) {
				int color = stack.getItemDamage();
				if(color != getNetwork()) {
					setNetwork(color);

					if(master != null)
						restartNetwork();
					else findNetwork();

					stack.stackSize--;
					if(player.worldObj.isRemote)
						player.swingItem();
				}
			}
		}

		return doPhantomInk(stack);
	}

	public boolean doPhantomInk(ItemStack stack) {
		if(stack != null && stack.getItem() == ModItems.phantomInk && !worldObj.isRemote) {
			int invis = dataWatcher.getWatchableObjectInt(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY);
			dataWatcher.updateObject(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY, ~invis & 1);
			return true;
		}

		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
		setMaster(cmp.getBoolean(TAG_MASTER));
		setNetwork(cmp.getInteger(TAG_NETWORK));
		dataWatcher.updateObject(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY, cmp.getInteger(TAG_INVIS));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_MASTER, isMaster());
		cmp.setInteger(TAG_NETWORK, getNetwork());
		cmp.setInteger(TAG_INVIS, dataWatcher.getWatchableObjectInt(EntitySpark.INVISIBILITY_DATA_WATCHER_KEY));
	}

}
