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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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

	private static final TrackedData<Boolean> MASTER = DataTracker.registerData(EntityCorporeaSpark.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> ITEM_DISPLAY_TICKS = DataTracker.registerData(EntityCorporeaSpark.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<ItemStack> DISPLAY_STACK = DataTracker.registerData(EntityCorporeaSpark.class, TrackedDataHandlerRegistry.ITEM_STACK);

	private ICorporeaSpark master;
	private List<ICorporeaSpark> connections = new ArrayList<>();
	private List<ICorporeaSpark> relatives = new ArrayList<>();
	private boolean firstTick = true;

	public EntityCorporeaSpark(EntityType<EntityCorporeaSpark> type, World world) {
		super(type, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(MASTER, false);
		dataTracker.startTracking(ITEM_DISPLAY_TICKS, 0);
		dataTracker.startTracking(DISPLAY_STACK, ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public ItemStack getPickedResult(HitResult target) {
		return isMaster() ? new ItemStack(ModItems.corporeaSparkMaster) : new ItemStack(ModItems.corporeaSpark);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
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
		dropStack(new ItemStack(isMaster() ? ModItems.corporeaSparkMaster : ModItems.corporeaSpark), 0F);
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
		return (List) world.getEntities(Entity.class, new Box(getX() - SCAN_RANGE, getY() - SCAN_RANGE, getZ() - SCAN_RANGE, getX() + SCAN_RANGE, getY() + SCAN_RANGE, getZ() + SCAN_RANGE), Predicates.instanceOf(ICorporeaSpark.class));
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
		int x = MathHelper.floor(getX());
		int y = MathHelper.floor(getY() - 1);
		int z = MathHelper.floor(getZ());
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
		dataTracker.set(MASTER, master);
	}

	@Override
	public boolean isMaster() {
		return dataTracker.get(MASTER);
	}

	public int getItemDisplayTicks() {
		return dataTracker.get(ITEM_DISPLAY_TICKS);
	}

	public void setItemDisplayTicks(int ticks) {
		dataTracker.set(ITEM_DISPLAY_TICKS, ticks);
	}

	public ItemStack getDisplayedItem() {
		return dataTracker.get(DISPLAY_STACK);
	}

	public void setDisplayedItem(ItemStack stack) {
		dataTracker.set(DISPLAY_STACK, stack);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!removed && !stack.isEmpty()) {
			if (player.world.isClient) {
				boolean valid = stack.getItem() == ModItems.twigWand || stack.getItem() instanceof DyeItem || stack.getItem() == ModItems.phantomInk;
				if (valid) {
					player.swingHand(hand);
				}
				return valid ? ActionResult.SUCCESS : ActionResult.PASS;
			}

			if (stack.getItem() == ModItems.twigWand) {
				if (player.isSneaking()) {
					dropAndKill();
					if (isMaster()) {
						restartNetwork();
					}
				} else {
					displayRelatives(player, new ArrayList<>(), master);
				}
				return ActionResult.SUCCESS;
			} else if (stack.getItem() instanceof DyeItem) {
				DyeColor color = ((DyeItem) stack.getItem()).getColor();
				if (color != getNetwork()) {
					setNetwork(color);

					if (isMaster()) {
						restartNetwork();
					} else {
						findNetwork();
					}

					stack.decrement(1);
					return ActionResult.SUCCESS;
				}
			} else if (stack.getItem() == ModItems.phantomInk) {
				setInvisible(true);
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void readCustomDataFromTag(@Nonnull CompoundTag cmp) {
		super.readCustomDataFromTag(cmp);
		setMaster(cmp.getBoolean(TAG_MASTER));
	}

	@Override
	protected void writeCustomDataToTag(@Nonnull CompoundTag cmp) {
		super.writeCustomDataToTag(cmp);
		cmp.putBoolean(TAG_MASTER, isMaster());
	}

}
