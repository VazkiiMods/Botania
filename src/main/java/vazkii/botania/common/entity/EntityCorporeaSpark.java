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

import net.fabricmc.fabric.api.entity.EntityPickInteractionAware;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityCorporeaSpark extends EntitySparkBase implements ICorporeaSpark, EntityPickInteractionAware {
	private static final int SCAN_RANGE = 8;

	private static final String TAG_MASTER = "master";

	private static final TrackedData<Boolean> MASTER = DataTracker.registerData(EntityCorporeaSpark.class, TrackedDataHandlerRegistry.BOOLEAN);

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
	}

	@Nonnull
	@Override
	public ItemStack getPickedStack(@Nullable PlayerEntity player, HitResult target) {
		return isMaster() ? new ItemStack(ModItems.corporeaSparkMaster) : new ItemStack(ModItems.corporeaSpark);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
			return;
		}

		ICorporeaNode node = getSparkNode();
		if (node instanceof DummyCorporeaNode && !world.getBlockState(getAttachPos()).isIn(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE)) {
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
		return (List) world.getEntitiesByClass(Entity.class, new Box(getX() - SCAN_RANGE, getY() - SCAN_RANGE, getZ() - SCAN_RANGE, getX() + SCAN_RANGE, getY() + SCAN_RANGE, getZ() + SCAN_RANGE), Predicates.instanceOf(ICorporeaSpark.class));
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
	public BlockPos getAttachPos() {
		int x = MathHelper.floor(getX());
		int y = MathHelper.floor(getY() - 1);
		int z = MathHelper.floor(getZ());
		return new BlockPos(x, y, z);
	}

	@Override
	public ICorporeaNode getSparkNode() {
		return CorporeaNodeDetectors.findNode(world, this);
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
		((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), getX(), getY(), getZ(), 10, 0.125, 0.125, 0.125, 0.05);
	}

	@Override
	public void onItemsRequested(List<ItemStack> stacks) {
		List<Item> shownItems = new ArrayList<>();
		for (ItemStack stack : stacks) {
			if (!shownItems.contains(stack.getItem())) {
				shownItems.add(stack.getItem());
				((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), getX(), getY(), getZ(), 10, 0.125, 0.125, 0.125, 0.05);
			}
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

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!removed && !stack.isEmpty()) {
			if (stack.getItem() == ModItems.twigWand) {
				if (!world.isClient) {
					if (player.isSneaking()) {
						dropAndKill();
						if (isMaster()) {
							restartNetwork();
						}
					} else {
						displayRelatives(player, new ArrayList<>(), master);
					}
				}
				return ActionResult.success(world.isClient);
			} else if (stack.getItem() instanceof DyeItem) {
				DyeColor color = ((DyeItem) stack.getItem()).getColor();
				if (color != getNetwork()) {
					if (!world.isClient) {
						setNetwork(color);

						stack.decrement(1);
					}

					return ActionResult.success(world.isClient);
				}
			} else if (stack.getItem() == ModItems.phantomInk) {
				if (!world.isClient) {
					setInvisible(true);
				}
				return ActionResult.success(world.isClient);
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public void setNetwork(DyeColor color) {
		if (color == getNetwork()) {
			return;
		}

		super.setNetwork(color);

		// Do not access world during deserialization
		if (!firstTick) {
			if (isMaster()) {
				restartNetwork();
			} else {
				findNetwork();
			}
		}
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return PacketSpawnEntity.make(this);
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
