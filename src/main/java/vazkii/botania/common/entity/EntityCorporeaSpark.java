/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class EntityCorporeaSpark extends EntitySparkBase implements ICorporeaSpark {
	private static final int SCAN_RANGE = 8;

	private static final String TAG_MASTER = "master";

	private static final DataParameter<Boolean> MASTER = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> ITEM_DISPLAY_TICKS = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.VARINT);
	private static final DataParameter<ItemStack> DISPLAY_STACK = EntityDataManager.createKey(EntityCorporeaSpark.class, DataSerializers.ITEMSTACK);

	private ICorporeaSpark master;
	private List<ICorporeaSpark> connections = new ArrayList<>();
	private List<ICorporeaSpark> relatives = new ArrayList<>();
	private boolean firstTick = true;

	public EntityCorporeaSpark(EntityType<EntityCorporeaSpark> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(MASTER, false);
		dataManager.register(ITEM_DISPLAY_TICKS, 0);
		dataManager.register(DISPLAY_STACK, ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return isMaster() ? new ItemStack(ModItems.corporeaSparkMaster) : new ItemStack(ModItems.corporeaSpark);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isRemote) {
			return;
		}

		InvWithLocation inv = getSparkInventory();
		if (inv == null) {
			dropAndKill();
			return;
		}

		if (isMaster()) {
			master = this;
		}

		if (firstTick) {
			if (isMaster()) {
				restartNetwork();
			} else {
				findNetwork();
			}

			firstTick = false;
		}

		if (master != null && (((Entity) master).removed || master.getNetwork() != getNetwork())) {
			master = null;
		}

		int displayTicks = getItemDisplayTicks();
		if (displayTicks > 0) {
			setItemDisplayTicks(displayTicks - 1);
		} else if (displayTicks < 0) {
			setItemDisplayTicks(displayTicks + 1);
		}
	}

	private void dropAndKill() {
		entityDropItem(new ItemStack(isMaster() ? ModItems.corporeaSparkMaster : ModItems.corporeaSpark), 0F);
		remove();
	}

	@Override
	public void remove() {
		super.remove();
		connections.remove(this);
		restartNetwork();
	}

	@Override
	public void registerConnections(ICorporeaSpark master, ICorporeaSpark referrer, List<ICorporeaSpark> connections) {
		relatives.clear();
		for (ICorporeaSpark spark : getNearbySparks()) {
			if (spark == null || connections.contains(spark) || spark.getNetwork() != getNetwork() || spark.isMaster() || ((Entity) spark).removed) {
				continue;
			}

			connections.add(spark);
			relatives.add(spark);
			spark.registerConnections(master, this, connections);
		}

		this.master = master;
		this.connections = connections;
	}

	@SuppressWarnings("unchecked")
	private List<ICorporeaSpark> getNearbySparks() {
		return (List) world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPosX() - SCAN_RANGE, getPosY() - SCAN_RANGE, getPosZ() - SCAN_RANGE, getPosX() + SCAN_RANGE, getPosY() + SCAN_RANGE, getPosZ() + SCAN_RANGE), Predicates.instanceOf(ICorporeaSpark.class));
	}

	private void restartNetwork() {
		connections = new ArrayList<>();
		relatives = new ArrayList<>();

		if (master != null) {
			ICorporeaSpark oldMaster = master;
			master = null;

			oldMaster.registerConnections(oldMaster, this, new ArrayList<>());
		}
	}

	private void findNetwork() {
		for (ICorporeaSpark spark : getNearbySparks()) {
			if (spark.getNetwork() == getNetwork() && !((Entity) spark).removed) {
				ICorporeaSpark master = spark.getMaster();
				if (master != null) {
					this.master = master;
					restartNetwork();

					break;
				}
			}
		}
	}

	private static void displayRelatives(PlayerEntity player, List<ICorporeaSpark> checked, ICorporeaSpark spark) {
		if (spark == null) {
			return;
		}

		List<ICorporeaSpark> sparks = spark.getRelatives();
		if (sparks.isEmpty()) {
			EntitySpark.particleBeam(player, (Entity) spark, (Entity) spark.getMaster());
		} else {
			for (ICorporeaSpark endSpark : sparks) {
				if (!checked.contains(endSpark)) {
					EntitySpark.particleBeam(player, (Entity) spark, (Entity) endSpark);
					checked.add(endSpark);
					displayRelatives(player, checked, endSpark);
				}
			}
		}
	}

	@Override
	public InvWithLocation getSparkInventory() {
		int x = MathHelper.floor(getPosX());
		int y = MathHelper.floor(getPosY() - 1);
		int z = MathHelper.floor(getPosZ());
		return InventoryHelper.getInventoryWithLocation(world, new BlockPos(x, y, z), Direction.UP);
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
		if (!stacks.isEmpty()) {
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

	public int getItemDisplayTicks() {
		return dataManager.get(ITEM_DISPLAY_TICKS);
	}

	public void setItemDisplayTicks(int ticks) {
		dataManager.set(ITEM_DISPLAY_TICKS, ticks);
	}

	public ItemStack getDisplayedItem() {
		return dataManager.get(DISPLAY_STACK);
	}

	public void setDisplayedItem(ItemStack stack) {
		dataManager.set(DISPLAY_STACK, stack);
	}

	@Override
	public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!removed && !stack.isEmpty()) {
			if (stack.getItem() == ModItems.twigWand) {
				if (!world.isRemote) {
					if (player.isSneaking()) {
						dropAndKill();
						if (isMaster()) {
							restartNetwork();
						}
					} else {
						displayRelatives(player, new ArrayList<>(), master);
					}
				}
				return ActionResultType.func_233537_a_(world.isRemote);
			} else if (stack.getItem() instanceof DyeItem) {
				DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
				if (color != getNetwork()) {
					if (!world.isRemote) {
						setNetwork(color);

						if (isMaster()) {
							restartNetwork();
						} else {
							findNetwork();
						}

						stack.shrink(1);
					}

					return ActionResultType.func_233537_a_(world.isRemote);
				}
			} else if (stack.getItem() == ModItems.phantomInk) {
				if (!world.isRemote) {
					setInvisible(true);
				}
				return ActionResultType.func_233537_a_(world.isRemote);
			}
		}

		return ActionResultType.PASS;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void readAdditional(@Nonnull CompoundNBT cmp) {
		super.readAdditional(cmp);
		setMaster(cmp.getBoolean(TAG_MASTER));
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT cmp) {
		super.writeAdditional(cmp);
		cmp.putBoolean(TAG_MASTER, isMaster());
	}

}
