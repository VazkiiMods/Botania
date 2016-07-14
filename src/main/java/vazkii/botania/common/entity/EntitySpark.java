/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 5:43:44 PM (GMT)]
 */
package vazkii.botania.common.entity;

import baubles.common.lib.PlayerHandler;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class EntitySpark extends Entity implements ISparkEntity {

	private static final int TRANSFER_RATE = 1000;
	private static final String TAG_UPGRADE = "upgrade";
	private static final String TAG_INVIS = "invis";
	public static final DataParameter<Integer> INVISIBILITY = EntityDataManager.createKey(EntitySpark.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> UPGRADE = EntityDataManager.createKey(EntitySpark.class, DataSerializers.VARINT);

	private final Set<ISparkEntity> transfers = Collections.newSetFromMap(new WeakHashMap<>());

	private int removeTransferants = 2;

	public EntitySpark(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		setSize(0.1F, 0.5F);
		dataManager.register(INVISIBILITY, 0);
		dataManager.register(UPGRADE, 0);
	}

	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ModItems.spark);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(worldObj.isRemote)
			return;

		ISparkAttachable tile = getAttachedTile();
		if(tile == null) {
			dropAndKill();
			return;
		}

		SparkUpgradeType upgrade = getUpgrade();
		List<ISparkEntity> allSparks = null;
		if(upgrade == SparkUpgradeType.DOMINANT || upgrade == SparkUpgradeType.RECESSIVE)
			allSparks = SparkHelper.getSparksAround(worldObj, posX, posY, posZ);

		Collection<ISparkEntity> transfers = getTransfers();

		switch(upgrade) {
			case DISPERSIVE : {
				List<EntityPlayer> players = SparkHelper.getEntitiesAround(EntityPlayer.class, worldObj, posX, posY, posZ);

				Map<EntityPlayer, TObjectIntHashMap<ItemStack>> receivingPlayers = new HashMap<>();

				ItemStack input = new ItemStack(ModItems.spark);
				for(EntityPlayer player : players) {
					List<ItemStack> stacks = new ArrayList<>();
					stacks.addAll(Arrays.asList(player.inventory.mainInventory));
					stacks.addAll(Arrays.asList(player.inventory.armorInventory));
					stacks.addAll(Arrays.asList(PlayerHandler.getPlayerBaubles(player).stackList));

					for(ItemStack stack : stacks) {
						if(stack == null || !(stack.getItem() instanceof IManaItem))
							continue;

						IManaItem manaItem = (IManaItem) stack.getItem();
						if(manaItem.canReceiveManaFromItem(stack, input)) {
							TObjectIntHashMap<ItemStack> receivingStacks;
							boolean add = false;
							if(!receivingPlayers.containsKey(player)) {
								add = true;
								receivingStacks = new TObjectIntHashMap<>();
							} else receivingStacks = receivingPlayers.get(player);

							int recv = Math.min(getAttachedTile().getCurrentMana(), Math.min(TRANSFER_RATE, manaItem.getMaxMana(stack) - manaItem.getMana(stack)));
							if(recv > 0) {
								receivingStacks.put(stack, recv);
								if(add)
									receivingPlayers.put(player, receivingStacks);
							}
						}
					}
				}

				if(!receivingPlayers.isEmpty()) {
					List<EntityPlayer> keys = new ArrayList<>(receivingPlayers.keySet());
					Collections.shuffle(keys);
					EntityPlayer player = keys.iterator().next();

					TObjectIntHashMap<ItemStack> items = receivingPlayers.get(player);
					ItemStack stack = items.keySet().iterator().next();
					int cost = items.get(stack);
					int manaToPut = Math.min(getAttachedTile().getCurrentMana(), cost);
					((IManaItem) stack.getItem()).addMana(stack, manaToPut);
					getAttachedTile().recieveMana(-manaToPut);
					particlesTowards(player);
				}

				break;
			}
			case DOMINANT : {
				List<ISparkEntity> validSparks = new ArrayList<>();
				for(ISparkEntity spark : allSparks) {
					if(spark == this)
						continue;

					SparkUpgradeType upgrade_ = spark.getUpgrade();
					if(upgrade_ == SparkUpgradeType.NONE && spark.getAttachedTile() instanceof IManaPool)
						validSparks.add(spark);
				}
				if(validSparks.size() > 0)
					validSparks.get(worldObj.rand.nextInt(validSparks.size())).registerTransfer(this);

				break;
			}
			case RECESSIVE : {
				for(ISparkEntity spark : allSparks) {
					if(spark == this)
						continue;

					SparkUpgradeType upgrade_ = spark.getUpgrade();
					if(upgrade_ != SparkUpgradeType.DOMINANT && upgrade_ != SparkUpgradeType.RECESSIVE && upgrade_ != SparkUpgradeType.ISOLATED)
						transfers.add(spark);
				}
				break;
			}
			case NONE:
			default: break;
		}

		if(!transfers.isEmpty()) {
			int manaTotal = Math.min(TRANSFER_RATE * transfers.size(), tile.getCurrentMana());
			int manaForEach = manaTotal / transfers.size();
			int manaSpent = 0;

			if(manaForEach > transfers.size()) {
				for(ISparkEntity spark : transfers) {
					if(spark.getAttachedTile() == null || spark.getAttachedTile().isFull() || spark.areIncomingTransfersDone()) {
						manaTotal -= manaForEach;
						continue;
					}

					ISparkAttachable attached = spark.getAttachedTile();
					int spend = Math.min(attached.getAvailableSpaceForMana(), manaForEach);
					attached.recieveMana(spend);
					manaSpent += spend;

					particlesTowards((Entity) spark);
				}
				tile.recieveMana(-manaSpent);
			}
		}

		if(removeTransferants > 0)
			removeTransferants--;
		filterTransfers();
	}

	private void particlesTowards(Entity e) {
		PacketHandler.sendToNearby(worldObj, this,
				new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.SPARK_MANA_FLOW, posX, posY, posZ,
						getEntityId(), e.getEntityId()));
	}

	public static void particleBeam(Entity e1, Entity e2) {
		if(e1 != null && e2 != null && !e1.worldObj.isRemote) {
			PacketHandler.sendToNearby(e1.worldObj, e1,
					new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.SPARK_NET_INDICATOR, e1.posX, e1.posY, e1.posZ,
							e1.getEntityId(), e2.getEntityId()));
		}
	}

	private void dropAndKill() {
		SparkUpgradeType upgrade = getUpgrade();
		entityDropItem(new ItemStack(ModItems.spark), 0F);
		if(upgrade !=  SparkUpgradeType.NONE)
			entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade.ordinal() - 1), 0F);
		setDead();
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
		if(stack != null) {
			if(worldObj.isRemote)
				return stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.sparkUpgrade
					|| stack.getItem() == ModItems.phantomInk;

			SparkUpgradeType upgrade = getUpgrade();
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					if(upgrade != SparkUpgradeType.NONE) {
						entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade.ordinal() - 1), 0F);
						setUpgrade(SparkUpgradeType.NONE);

						transfers.clear();
						removeTransferants = 2;
					} else dropAndKill();
					player.swingArm(hand);
					return true;
				} else {
					List<ISparkEntity> allSparks = SparkHelper.getSparksAround(worldObj, posX, posY, posZ);
					for(ISparkEntity spark : allSparks)
						particleBeam(this, (Entity) spark);
					return true;
				}
			} else if(stack.getItem() == ModItems.sparkUpgrade && upgrade == SparkUpgradeType.NONE) {
				int newUpgrade = stack.getItemDamage() + 1;
				setUpgrade(SparkUpgradeType.values()[newUpgrade]);
				stack.stackSize--;
				player.swingArm(hand);
				return true;
			} else if (stack.getItem() == ModItems.phantomInk) {
				int invis = dataManager.get(INVISIBILITY);
				dataManager.set(INVISIBILITY, ~invis & 1);
				return true;
			}
		}

		return false;
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound cmp) {
		setUpgrade(SparkUpgradeType.values()[cmp.getInteger(TAG_UPGRADE)]);
		dataManager.set(INVISIBILITY, cmp.getInteger(TAG_INVIS));
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
		cmp.setInteger(TAG_UPGRADE, getUpgrade().ordinal());
		cmp.setInteger(TAG_INVIS, dataManager.get(INVISIBILITY));
	}

	@Override
	public ISparkAttachable getAttachedTile() {
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY) - 1;
		int z = MathHelper.floor_double(posZ);
		TileEntity tile = worldObj.getTileEntity(new BlockPos(x, y, z));
		if(tile != null && tile instanceof ISparkAttachable)
			return (ISparkAttachable) tile;

		return null;
	}

	private void filterTransfers() {
		Iterator<ISparkEntity> iter = transfers.iterator();
		while (iter.hasNext()) {
			ISparkEntity spark = iter.next();
			SparkUpgradeType upgr = getUpgrade();
			SparkUpgradeType supgr = spark.getUpgrade();
			ISparkAttachable atile = spark.getAttachedTile();

			if(!(spark != this
					&& !spark.areIncomingTransfersDone()
					&& atile != null && !atile.isFull()
					&& (upgr == SparkUpgradeType.NONE && supgr == SparkUpgradeType.DOMINANT || upgr == SparkUpgradeType.RECESSIVE && (supgr == SparkUpgradeType.NONE || supgr == SparkUpgradeType.DISPERSIVE) || !(atile instanceof IManaPool))))
				iter.remove();
		}
	}

	@Override
	public Collection<ISparkEntity> getTransfers() {
		filterTransfers();
		return transfers;
	}

	private boolean hasTransfer(ISparkEntity entity) {
		return transfers.contains(entity);
	}

	@Override
	public void registerTransfer(ISparkEntity entity) {
		if(hasTransfer(entity))
			return;
		transfers.add(entity);
	}

	@Override
	public SparkUpgradeType getUpgrade() {
		return SparkUpgradeType.values()[dataManager.get(UPGRADE)];
	}

	@Override
	public void setUpgrade(SparkUpgradeType upgrade) {
		dataManager.set(UPGRADE, upgrade.ordinal());
	}

	@Override
	public boolean areIncomingTransfersDone() {
		ISparkAttachable tile = getAttachedTile();
		if(tile instanceof IManaPool)
			return removeTransferants > 0;
		return tile != null && tile.areIncomingTranfersDone();
	}

}
