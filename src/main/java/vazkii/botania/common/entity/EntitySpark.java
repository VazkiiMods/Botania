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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import baubles.common.lib.PlayerHandler;

public class EntitySpark extends Entity implements ISparkEntity {

	private static final int TRANSFER_RATE = 1000;
	private static final String TAG_UPGRADE = "upgrade";
	private static final String TAG_INVIS = "invis";
	public static final int INVISIBILITY_DATA_WATCHER_KEY = 27;

	Set<ISparkEntity> transfers = Collections.newSetFromMap(new WeakHashMap());

	int removeTransferants = 2;
	boolean firstTick = false;

	public EntitySpark(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		setSize(0.1F, 0.5F);
		dataWatcher.addObject(INVISIBILITY_DATA_WATCHER_KEY, 0);
		dataWatcher.addObject(28, 0);
		dataWatcher.setObjectWatched(INVISIBILITY_DATA_WATCHER_KEY);
		dataWatcher.setObjectWatched(28);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		ISparkAttachable tile = getAttachedTile();
		if(tile == null) {
			if(!worldObj.isRemote)
				setDead();
			return;
		}

		boolean first = worldObj.isRemote && !firstTick;
		int upgrade = getUpgrade();
		List<ISparkEntity> allSparks = null;
		if(first || upgrade == 2 || upgrade == 3)
			allSparks = SparkHelper.getSparksAround(worldObj, posX, posY, posZ);

		if(first)
			first = true;

		Collection<ISparkEntity> transfers = getTransfers();


		if(upgrade != 0) {
			switch(upgrade) {
			case 1 : { // Dispersive
				List<EntityPlayer> players = SparkHelper.getEntitiesAround(EntityPlayer.class, worldObj, posX, posY, posZ);

				Map<EntityPlayer, Map<ItemStack, Integer>> receivingPlayers = new HashMap();

				ItemStack input = new ItemStack(ModItems.spark);
				for(EntityPlayer player : players) {
					List<ItemStack> stacks = new ArrayList();
					stacks.addAll(Arrays.asList(player.inventory.mainInventory));
					stacks.addAll(Arrays.asList(player.inventory.armorInventory));
					stacks.addAll(Arrays.asList(PlayerHandler.getPlayerBaubles(player).stackList));

					for(ItemStack stack : stacks) {
						if(stack == null || !(stack.getItem() instanceof IManaItem))
							continue;

						IManaItem manaItem = (IManaItem) stack.getItem();
						if(manaItem.canReceiveManaFromItem(stack, input)) {
							Map receivingStacks;
							boolean add = false;
							if(!receivingPlayers.containsKey(player)) {
								add = true;
								receivingStacks = new HashMap();
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
					List<EntityPlayer> keys = new ArrayList(receivingPlayers.keySet());
					Collections.shuffle(keys);
					EntityPlayer player = keys.iterator().next();

					Map<ItemStack, Integer> items = receivingPlayers.get(player);
					ItemStack stack = items.keySet().iterator().next();
					int cost = items.get(stack);
					int manaToPut = Math.min(getAttachedTile().getCurrentMana(), cost);
					((IManaItem) stack.getItem()).addMana(stack, manaToPut);
					getAttachedTile().recieveMana(-manaToPut);
					particlesTowards(player);
				}

				break;
			}
			case 2 : { // Dominant
				List<ISparkEntity> validSparks = new ArrayList();
				for(ISparkEntity spark : allSparks) {
					if(spark == this)
						continue;

					int upgrade_ = spark.getUpgrade();
					if(upgrade_ == 0 && spark.getAttachedTile() instanceof IManaPool)
						validSparks.add(spark);
				}
				if(validSparks.size() > 0)
					validSparks.get(worldObj.rand.nextInt(validSparks.size())).registerTransfer(this);

				break;
			}
			case 3 : { // Recessive
				for(ISparkEntity spark : allSparks) {
					if(spark == this)
						continue;

					int upgrade_ = spark.getUpgrade();
					if(upgrade_ != 2 && upgrade_ != 3 && upgrade_ != 4)
						transfers.add(spark);
				}
				break;
			}
			}
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
		getTransfers();
	}

	void particlesTowards(Entity e) {
		Vector3 thisVec = Vector3.fromEntityCenter(this).add(0, 0, 0);
		Vector3 receiverVec = Vector3.fromEntityCenter(e).add(0, 0, 0);

		double rc = 0.45;
		thisVec.add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);
		receiverVec.add((Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc, (Math.random() - 0.5) * rc);

		Vector3 motion = receiverVec.copy().sub(thisVec);
		motion.multiply(0.04F);
		float r = 0.4F + 0.3F * (float) Math.random();
		float g = 0.4F + 0.3F * (float) Math.random();
		float b = 0.4F + 0.3F * (float) Math.random();
		float size = 0.125F + 0.125F * (float) Math.random();

		Botania.proxy.wispFX(worldObj, thisVec.x, thisVec.y, thisVec.z, r, g, b, size, (float) motion.x, (float) motion.y, (float) motion.z);
	}

	public static void particleBeam(Entity e1, Entity e2) {
		if(e1 == null || e2 == null)
			return;

		Vector3 orig = new Vector3(e1.posX , e1.posY + 0.25, e1.posZ);
		Vector3 end = new Vector3(e2.posX, e2.posY + 0.25, e2.posZ);
		Vector3 diff = end.copy().sub(orig);
		Vector3 movement = diff.copy().normalize().multiply(0.1);
		int iters = (int) (diff.mag() / movement.mag());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vector3 currentPos = orig.copy();
		for(int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			Color color = Color.getHSBColor(hue, 1F, 1F);
			float r = Math.min(1F, color.getRed() / 255F + 0.4F);
			float g = Math.min(1F, color.getGreen() / 255F + 0.4F);
			float b = Math.min(1F, color.getBlue() / 255F + 0.4F);

			Botania.proxy.setSparkleFXNoClip(true);
			Botania.proxy.sparkleFX(e1.worldObj, currentPos.x, currentPos.y, currentPos.z, r, g, b, 1F, 12);
			Botania.proxy.setSparkleFXNoClip(false);
			currentPos.add(movement);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean interactFirst(EntityPlayer player) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null) {
			int upgrade = getUpgrade();
			if(stack.getItem() == ModItems.twigWand) {
				if(player.isSneaking()) {
					if(upgrade > 0) {
						if(!worldObj.isRemote)
							entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade - 1), 0F);
						setUpgrade(0);

						transfers.clear();
						removeTransferants = 2;
					} else setDead();
					if(player.worldObj.isRemote)
						player.swingItem();
					return true;
				} else {
					List<ISparkEntity> allSparks = SparkHelper.getSparksAround(worldObj, posX, posY, posZ);
					for(ISparkEntity spark : allSparks)
						particleBeam(this, (Entity) spark);
					return true;
				}
			} else if(stack.getItem() == ModItems.sparkUpgrade && upgrade == 0) {
				int newUpgrade = stack.getItemDamage() + 1;
				setUpgrade(newUpgrade);
				stack.stackSize--;
				if(player.worldObj.isRemote)
					player.swingItem();
				return true;
			}
		}

		return doPhantomInk(stack);
	}

	public boolean doPhantomInk(ItemStack stack) {
		if(stack != null && stack.getItem() == ModItems.phantomInk && !worldObj.isRemote) {
			int invis = dataWatcher.getWatchableObjectInt(INVISIBILITY_DATA_WATCHER_KEY);
			dataWatcher.updateObject(INVISIBILITY_DATA_WATCHER_KEY, ~invis & 1);
			return true;
		}

		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
		setUpgrade(cmp.getInteger(TAG_UPGRADE));
		dataWatcher.updateObject(INVISIBILITY_DATA_WATCHER_KEY, cmp.getInteger(TAG_INVIS));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_UPGRADE, getUpgrade());
		cmp.setInteger(TAG_INVIS, dataWatcher.getWatchableObjectInt(INVISIBILITY_DATA_WATCHER_KEY));
	}

	@Override
	public void setDead() {
		super.setDead();
		if(!worldObj.isRemote) {
			int upgrade = getUpgrade();
			entityDropItem(new ItemStack(ModItems.spark), 0F);
			if(upgrade > 0)
				entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade - 1), 0F);
		}
	}

	@Override
	public ISparkAttachable getAttachedTile() {
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY) - 1;
		int z = MathHelper.floor_double(posZ);
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		if(tile != null && tile instanceof ISparkAttachable)
			return (ISparkAttachable) tile;

		return null;
	}


	@Override
	public Collection<ISparkEntity> getTransfers() {
		Collection<ISparkEntity> removals = new ArrayList();

		for(ISparkEntity e : transfers) {
			ISparkEntity spark = e;
			int upgr = getUpgrade();
			int supgr = spark.getUpgrade();
			ISparkAttachable atile = spark.getAttachedTile();

			if(!(spark != this && !spark.areIncomingTransfersDone() && atile != null && !atile.isFull() && (upgr == 0 && supgr == 2 || upgr == 3 && (supgr == 0 || supgr == 1) || !(atile instanceof IManaPool))))
				removals.add(e);
		}

		if(!removals.isEmpty())
			transfers.removeAll(removals);

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
	public int getUpgrade() {
		return dataWatcher.getWatchableObjectInt(28);
	}

	@Override
	public void setUpgrade(int upgrade) {
		dataWatcher.updateObject(28, upgrade);
	}

	@Override
	public boolean areIncomingTransfersDone() {
		ISparkAttachable tile = getAttachedTile();
		if(tile instanceof IManaPool)
			return removeTransferants > 0;

			return tile != null && tile.areIncomingTranfersDone();
	}

}
