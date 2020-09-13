/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.fabric.api.entity.EntityPickInteractionAware;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;

import java.util.*;
import java.util.stream.Collectors;

public class EntityManaSpark extends EntitySparkBase implements IManaSpark, EntityPickInteractionAware {
	private static final int TRANSFER_RATE = 1000;
	private static final String TAG_UPGRADE = "upgrade";
	private static final EntityDataAccessor<Integer> UPGRADE = SynchedEntityData.defineId(EntityManaSpark.class, EntityDataSerializers.INT);

	private final Set<IManaSpark> transfers = Collections.newSetFromMap(new WeakHashMap<>());

	private int removeTransferants = 2;

	public EntityManaSpark(EntityType<EntityManaSpark> type, Level world) {
		super(type, world);
	}

	public EntityManaSpark(Level world) {
		this(ModEntities.SPARK, world);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(UPGRADE, 0);
	}

	@Nonnull
	@Override
	public ItemStack getPickedStack(Player player, HitResult target) {
		return new ItemStack(ModItems.spark);
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide) {
			return;
		}

		ISparkAttachable tile = getAttachedTile();
		if (tile == null) {
			dropAndKill();
			return;
		}

		SparkUpgradeType upgrade = getUpgrade();
		Collection<IManaSpark> transfers = getTransfers();

		switch (upgrade) {
		case DISPERSIVE: {
			List<Player> players = SparkHelper.getEntitiesAround(Player.class, level, getX(), getY() + (getBbHeight() / 2.0), getZ());

			Map<Player, Map<ItemStack, Integer>> receivingPlayers = new HashMap<>();

			ItemStack input = new ItemStack(ModItems.spark);
			for (Player player : players) {
				List<ItemStack> stacks = new ArrayList<>();
				stacks.addAll(player.getInventory().items);
				stacks.addAll(player.getInventory().armor);

				Container inv = BotaniaAPI.instance().getAccessoriesInventory(player);
				for (int i = 0; i < inv.getContainerSize(); i++) {
					stacks.add(inv.getItem(i));
				}

				for (ItemStack stack : stacks) {
					if (stack.isEmpty() || !(stack.getItem() instanceof IManaItem)) {
						continue;
					}

					IManaItem manaItem = (IManaItem) stack.getItem();
					if (manaItem.canReceiveManaFromItem(stack, input)) {
						Map<ItemStack, Integer> receivingStacks;
						boolean add = false;
						if (!receivingPlayers.containsKey(player)) {
							add = true;
							receivingStacks = new HashMap<>();
						} else {
							receivingStacks = receivingPlayers.get(player);
						}

						int recv = Math.min(getAttachedTile().getCurrentMana(), Math.min(TRANSFER_RATE, manaItem.getMaxMana(stack) - manaItem.getMana(stack)));
						if (recv > 0) {
							receivingStacks.put(stack, recv);
							if (add) {
								receivingPlayers.put(player, receivingStacks);
							}
						}
					}
				}
			}

			if (!receivingPlayers.isEmpty()) {
				List<Player> keys = new ArrayList<>(receivingPlayers.keySet());
				Collections.shuffle(keys);
				Player player = keys.iterator().next();

				Map<ItemStack, Integer> items = receivingPlayers.get(player);
				ItemStack stack = items.keySet().iterator().next();
				int cost = items.get(stack);
				int manaToPut = Math.min(getAttachedTile().getCurrentMana(), cost);
				((IManaItem) stack.getItem()).addMana(stack, manaToPut);
				getAttachedTile().receiveMana(-manaToPut);
				particlesTowards(player);
			}

			break;
		}
		case DOMINANT: {
			List<IManaSpark> validSparks = SparkHelper.getSparksAround(level, getX(), getY() + (getBbHeight() / 2), getZ(), getNetwork())
					.filter(s -> {
						SparkUpgradeType otherUpgrade = s.getUpgrade();
						return s != this && otherUpgrade == SparkUpgradeType.NONE && s.getAttachedTile() instanceof IManaPool;
					})
					.collect(Collectors.toList());
			if (validSparks.size() > 0) {
				validSparks.get(level.random.nextInt(validSparks.size())).registerTransfer(this);
			}

			break;
		}
		case RECESSIVE: {
			SparkHelper.getSparksAround(level, getX(), getY() + (getBbHeight() / 2), getZ(), getNetwork())
					.filter(s -> {
						SparkUpgradeType otherUpgrade = s.getUpgrade();
						return s != this
								&& otherUpgrade != SparkUpgradeType.DOMINANT
								&& otherUpgrade != SparkUpgradeType.RECESSIVE
								&& otherUpgrade != SparkUpgradeType.ISOLATED;
					})
					.forEach(transfers::add);
			break;
		}
		case NONE:
		default:
			break;
		}

		if (!transfers.isEmpty()) {
			int manaTotal = Math.min(TRANSFER_RATE * transfers.size(), tile.getCurrentMana());
			int count = transfers.size();
			int manaSpent = 0;

			if (manaTotal > 0) {
				for (IManaSpark spark : transfers) {
					count--;
					if (spark.getAttachedTile() == null || spark.getAttachedTile().isFull() || spark.areIncomingTransfersDone()) {
						continue;
					}

					ISparkAttachable attached = spark.getAttachedTile();
					int spend = Math.min(attached.getAvailableSpaceForMana(), (manaTotal - manaSpent) / (count + 1));
					attached.receiveMana(spend);
					manaSpent += spend;

					particlesTowards(spark.entity());
				}
				tile.receiveMana(-manaSpent);
			}
		}

		if (removeTransferants > 0) {
			removeTransferants--;
		}
		filterTransfers();
	}

	private void particlesTowards(Entity e) {
		PacketBotaniaEffect.sendNearby(this, PacketBotaniaEffect.EffectType.SPARK_MANA_FLOW, getX(), getY(), getZ(),
				getId(), e.getId(), ColorHelper.getColorValue(getNetwork()));
	}

	public static void particleBeam(Player player, Entity e1, Entity e2) {
		if (e1 != null && e2 != null && !e1.level.isClientSide) {
			PacketBotaniaEffect.send(player, PacketBotaniaEffect.EffectType.SPARK_NET_INDICATOR, e1.getX(), e1.getY(), e1.getZ(),
					e1.getId(), e2.getId());
		}
	}

	private void dropAndKill() {
		SparkUpgradeType upgrade = getUpgrade();
		spawnAtLocation(new ItemStack(ModItems.spark), 0F);
		if (upgrade != SparkUpgradeType.NONE) {
			spawnAtLocation(ItemSparkUpgrade.getByType(upgrade), 0F);
		}
		discard();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (isAlive() && !stack.isEmpty()) {
			SparkUpgradeType upgrade = getUpgrade();
			if (stack.is(ModItems.twigWand)) {
				if (!level.isClientSide) {
					if (player.isShiftKeyDown()) {
						if (upgrade != SparkUpgradeType.NONE) {
							spawnAtLocation(ItemSparkUpgrade.getByType(upgrade), 0F);
							setUpgrade(SparkUpgradeType.NONE);

							transfers.clear();
							removeTransferants = 2;
						} else {
							dropAndKill();
						}
					} else {
						SparkHelper.getSparksAround(level, getX(), getY() + (getBbHeight() / 2), getZ(), getNetwork())
								.forEach(s -> particleBeam(player, this, s.entity()));
					}
				}

				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (stack.getItem() instanceof ItemSparkUpgrade && upgrade == SparkUpgradeType.NONE) {
				if (!level.isClientSide) {
					setUpgrade(((ItemSparkUpgrade) stack.getItem()).type);
					stack.shrink(1);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (stack.is(ModItems.phantomInk)) {
				if (!level.isClientSide) {
					setInvisible(true);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (stack.getItem() instanceof DyeItem) {
				DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
				if (color != getNetwork()) {
					if (!level.isClientSide) {
						setNetwork(color);
						stack.shrink(1);
					}
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Override
	protected void readAdditionalSaveData(@Nonnull CompoundTag cmp) {
		super.readAdditionalSaveData(cmp);
		setUpgrade(SparkUpgradeType.values()[cmp.getInt(TAG_UPGRADE)]);
	}

	@Override
	protected void addAdditionalSaveData(@Nonnull CompoundTag cmp) {
		super.addAdditionalSaveData(cmp);
		cmp.putInt(TAG_UPGRADE, getUpgrade().ordinal());
	}

	@Override
	public ISparkAttachable getAttachedTile() {
		BlockEntity tile = level.getBlockEntity(getAttachPos());
		if (tile instanceof ISparkAttachable) {
			return (ISparkAttachable) tile;
		}

		return null;
	}

	private void filterTransfers() {
		Iterator<IManaSpark> iter = transfers.iterator();
		while (iter.hasNext()) {
			IManaSpark spark = iter.next();
			SparkUpgradeType upgr = getUpgrade();
			SparkUpgradeType supgr = spark.getUpgrade();
			ISparkAttachable atile = spark.getAttachedTile();

			if (spark == this
					|| spark.areIncomingTransfersDone()
					|| getNetwork() != spark.getNetwork()
					|| atile == null
					|| atile.isFull()
					|| !(upgr == SparkUpgradeType.NONE && supgr == SparkUpgradeType.DOMINANT
							|| upgr == SparkUpgradeType.RECESSIVE && (supgr == SparkUpgradeType.NONE || supgr == SparkUpgradeType.DISPERSIVE)
							|| !(atile instanceof IManaPool))) {
				iter.remove();
			}
		}
	}

	@Override
	public Collection<IManaSpark> getTransfers() {
		filterTransfers();
		return transfers;
	}

	private boolean hasTransfer(IManaSpark entity) {
		return transfers.contains(entity);
	}

	@Override
	public void registerTransfer(IManaSpark entity) {
		if (hasTransfer(entity)) {
			return;
		}
		transfers.add(entity);
	}

	@Override
	public SparkUpgradeType getUpgrade() {
		return SparkUpgradeType.values()[entityData.get(UPGRADE)];
	}

	@Override
	public void setUpgrade(SparkUpgradeType upgrade) {
		entityData.set(UPGRADE, upgrade.ordinal());
	}

	@Override
	public boolean areIncomingTransfersDone() {
		ISparkAttachable tile = getAttachedTile();
		if (tile instanceof IManaPool) {
			return removeTransferants > 0;
		}
		return tile != null && tile.areIncomingTranfersDone();
	}

}
