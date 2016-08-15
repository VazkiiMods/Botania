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

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EntityCorporeaSpark extends Entity implements ICorporeaSpark {

	private static final int SCAN_RANGE = 8;

	private static final String TAG_MASTER = "master";
	private static final String TAG_NETWORK = "network";
	private static final String TAG_INVIS = "invis";

	private static final DataParameter<Boolean> MASTER = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> NETWORK = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> INVISIBILITY = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ITEM_DISPLAY_TICKS = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<Optional<ItemStack>> DISPLAY_STACK = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.OPTIONAL_ITEM_STACK);

	private ICorporeaSpark master;
	private List<ICorporeaSpark> connections = new ArrayList<>();
	private List<ICorporeaSpark> relatives = new ArrayList<>();
	private boolean firstTick = true;

	public EntityCorporeaSpark(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		setSize(0.1F, 0.5F);
		dataManager.register(INVISIBILITY, 0);
		dataManager.register(MASTER, false);
		dataManager.register(NETWORK, 0);
		dataManager.register(ITEM_DISPLAY_TICKS, 0);
		dataManager.register(DISPLAY_STACK, Optional.absent());
	}

	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return isMaster() ? new ItemStack(ModItems.corporeaSpark, 1, 1) : new ItemStack(ModItems.corporeaSpark);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(worldObj.isRemote)
			return;

		InvWithLocation inv = getSparkInventory();
		if(inv == null) {
			dropAndKill();
			return;
		}

		if(isMaster())
			master = this;

		if(firstTick) {
			if(isMaster())
				restartNetwork();
			else findNetwork();

			firstTick = false;
		}

		if(master != null && (((Entity) master).isDead || master.getNetwork() != getNetwork()))
			master = null;

		int displayTicks = getItemDisplayTicks();
		if(displayTicks > 0)
			setItemDisplayTicks(displayTicks - 1);
		else if(displayTicks < 0)
			setItemDisplayTicks(displayTicks + 1);
	}

	private void dropAndKill() {
		entityDropItem(new ItemStack(ModItems.corporeaSpark, 1, isMaster() ? 1 : 0), 0F);
		setDead();
	}

	@Override
	public void setDead() {
		super.setDead();
		connections.remove(this);
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
		this.connections = connections;
	}

	private List<ICorporeaSpark> getNearbySparks() {
		List ret = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - SCAN_RANGE, posY - SCAN_RANGE, posZ - SCAN_RANGE, posX + SCAN_RANGE, posY + SCAN_RANGE, posZ + SCAN_RANGE), Predicates.instanceOf(ICorporeaSpark.class));
		return ((List<ICorporeaSpark>) ret);
	}

	private void restartNetwork() {
		connections = new ArrayList<>();
		relatives = new ArrayList<>();

		if(master != null) {
			ICorporeaSpark oldMaster = master;
			master = null;

			oldMaster.registerConnections(oldMaster, this, new ArrayList<>());
		}
	}

	private void findNetwork() {
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

	private static void displayRelatives(EntityPlayer player, List<ICorporeaSpark> checked, ICorporeaSpark spark) {
		if(spark == null)
			return;

		List<ICorporeaSpark> sparks = spark.getRelatives();
		if(sparks.isEmpty())
			EntitySpark.particleBeam(player, (Entity) spark, (Entity) spark.getMaster());
		else for(ICorporeaSpark endSpark : sparks) {
			if(!checked.contains(endSpark)) {
				EntitySpark.particleBeam(player, (Entity) spark, (Entity) endSpark);
				checked.add(endSpark);
				displayRelatives(player, checked, endSpark);
			}
		}
	}

	@Override
	public InvWithLocation getSparkInventory() {
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY - 1);
		int z = MathHelper.floor_double(posZ);
		return InventoryHelper.getInventoryWithLocation(worldObj, new BlockPos(x, y, z), EnumFacing.UP);
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
		dataManager.set(MASTER, master);
	}

	@Override
	public boolean isMaster() {
		return dataManager.get(MASTER);
	}

	public void setNetwork(EnumDyeColor network) {
		dataManager.set(NETWORK, network.getMetadata());
	}

	@Override
	public EnumDyeColor getNetwork() {
		return EnumDyeColor.byMetadata(dataManager.get(NETWORK));
	}

	public int getItemDisplayTicks() {
		return dataManager.get(ITEM_DISPLAY_TICKS);
	}

	public void setItemDisplayTicks(int ticks) {
		dataManager.set(ITEM_DISPLAY_TICKS, ticks);
	}

	public Optional<ItemStack> getDisplayedItem() {
		return dataManager.get(DISPLAY_STACK);
	}

	public void setDisplayedItem(ItemStack stack) {
		dataManager.set(DISPLAY_STACK, Optional.fromNullable(stack));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
		if(stack != null) {
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					if(!player.worldObj.isRemote) {
						dropAndKill();
						if(isMaster())
							restartNetwork();
					} else player.swingArm(hand);
					return true;
				} else {
					if(!player.worldObj.isRemote) {
						displayRelatives(player, new ArrayList<>(), master);
					}
					return true;
				}
			} else if(stack.getItem() == ModItems.dye) {
				int color = stack.getItemDamage();
				if(color != getNetwork().getMetadata()) {
					if(!worldObj.isRemote) {
						setNetwork(EnumDyeColor.byMetadata(color));

						if(master != null)
							restartNetwork();
						else findNetwork();

						stack.stackSize--;
					} else player.swingArm(hand);
				}
			}
		}

		return doPhantomInk(stack);
	}

	public boolean doPhantomInk(ItemStack stack) {
		if(stack != null && stack.getItem() == ModItems.phantomInk && !worldObj.isRemote) {
			int invis = dataManager.get(INVISIBILITY);
			dataManager.set(INVISIBILITY, ~invis & 1);
			return true;
		}

		return false;
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound cmp) {
		setMaster(cmp.getBoolean(TAG_MASTER));
		setNetwork(EnumDyeColor.byMetadata(cmp.getInteger(TAG_NETWORK)));
		dataManager.set(INVISIBILITY, cmp.getInteger(TAG_INVIS));
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
		cmp.setBoolean(TAG_MASTER, isMaster());
		cmp.setInteger(TAG_NETWORK, getNetwork().getMetadata());
		cmp.setInteger(TAG_INVIS, dataManager.get(INVISIBILITY));
	}

}
