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

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.core.helper.InventoryHelper2;
import vazkii.botania.common.item.ModItems;

public class EntityCorporeaSpark extends Entity implements ICorporeaSpark {

	private static final int SCAN_RANGE = 8;

	private static final String TAG_MASTER = "master";
	private static final String TAG_NETWORK = "network";
	private static final String TAG_INVIS = "invis";

	private static final DataParameter<Boolean> MASTER = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> NETWORK = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> INVISIBILITY = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ITEM_DISPLAY_TICKS = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<Optional<ItemStack>> DISPLAY_STACK = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.OPTIONAL_ITEM_STACK);

	private ICorporeaSpark master;
	private List<ICorporeaSpark> connections = new ArrayList<>();
	private List<ICorporeaSpark> connectionsClient = new ArrayList<>();
	private List<ICorporeaSpark> relatives = new ArrayList<>();
	private boolean firstUpdateClient = true;
	private boolean firstUpdateServer = true;

	public EntityCorporeaSpark(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		setSize(0.1F, 0.5F);
		dataWatcher.register(INVISIBILITY, 0);
		dataWatcher.register(MASTER, false);
		dataWatcher.register(NETWORK, 0);
		dataWatcher.register(ITEM_DISPLAY_TICKS, 0);
		dataWatcher.register(DISPLAY_STACK, Optional.absent());
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		InvWithLocation inv = getSparkInventory();
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
		List ret = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - SCAN_RANGE, posY - SCAN_RANGE, posZ - SCAN_RANGE, posX + SCAN_RANGE, posY + SCAN_RANGE, posZ + SCAN_RANGE), Predicates.instanceOf(ICorporeaSpark.class));
		return ((List<ICorporeaSpark>) ret);
	}

	void restartNetwork() {
		if(worldObj.isRemote)
			connectionsClient = new ArrayList<>();
		else connections = new ArrayList<>();
		relatives = new ArrayList<>();

		if(master != null) {
			ICorporeaSpark oldMaster = master;
			master = null;

			oldMaster.registerConnections(oldMaster, this, new ArrayList<>());
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
	public InvWithLocation getSparkInventory() {
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY - 1);
		int z = MathHelper.floor_double(posZ);
		return InventoryHelper2.getInventoryWithLocation(worldObj, new BlockPos(x, y, z), EnumFacing.UP);
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
		dataWatcher.set(MASTER, master);
	}

	@Override
	public boolean isMaster() {
		return dataWatcher.get(MASTER);
	}

	public void setNetwork(EnumDyeColor network) {
		dataWatcher.set(NETWORK, network.getMetadata());
	}

	@Override
	public EnumDyeColor getNetwork() {
		return EnumDyeColor.byMetadata(dataWatcher.get(NETWORK));
	}

	public int getItemDisplayTicks() {
		return dataWatcher.get(ITEM_DISPLAY_TICKS);
	}

	public void setItemDisplayTicks(int ticks) {
		dataWatcher.set(ITEM_DISPLAY_TICKS, ticks);
	}

	public Optional<ItemStack> getDisplayedItem() {
		return dataWatcher.get(DISPLAY_STACK);
	}

	public void setDisplayedItem(ItemStack stack) {
		dataWatcher.set(DISPLAY_STACK, Optional.fromNullable(stack));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
		if(stack != null) {
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					setDead();
					if(isMaster())
						restartNetwork();
					if(player.worldObj.isRemote)
						player.swingArm(hand);
					return true;
				} else {
					displayRelatives(new ArrayList<>(), master);
					return true;
				}
			} else if(stack.getItem() == ModItems.dye) {
				int color = stack.getItemDamage();
				if(color != getNetwork().getMetadata()) {
					setNetwork(EnumDyeColor.byMetadata(color));

					if(master != null)
						restartNetwork();
					else findNetwork();

					stack.stackSize--;
					if(player.worldObj.isRemote)
						player.swingArm(hand);
				}
			}
		}

		return doPhantomInk(stack);
	}

	public boolean doPhantomInk(ItemStack stack) {
		if(stack != null && stack.getItem() == ModItems.phantomInk && !worldObj.isRemote) {
			int invis = dataWatcher.get(INVISIBILITY);
			dataWatcher.set(INVISIBILITY, ~invis & 1);
			return true;
		}

		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
		setMaster(cmp.getBoolean(TAG_MASTER));
		setNetwork(EnumDyeColor.byMetadata(cmp.getInteger(TAG_NETWORK)));
		dataWatcher.set(INVISIBILITY, cmp.getInteger(TAG_INVIS));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_MASTER, isMaster());
		cmp.setInteger(TAG_NETWORK, getNetwork().getMetadata());
		cmp.setInteger(TAG_INVIS, dataWatcher.get(INVISIBILITY));
	}

}
