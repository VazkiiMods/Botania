/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.ModTiles;

import java.util.List;

public class TileCorporeaCrystalCube extends TileCorporeaBase implements ICorporeaRequestor, Tickable {
	private static final String TAG_REQUEST_TARGET = "requestTarget";
	private static final String TAG_ITEM_COUNT = "itemCount";
	private static final String TAG_LOCK = "lock";

	private ItemStack requestTarget = ItemStack.EMPTY;
	private int itemCount = 0;
	private int ticks = 0;
	private int compValue = 0;
	public boolean locked = false;

	public TileCorporeaCrystalCube() {
		super(ModTiles.CORPOREA_CRYSTAL_CUBE);
	}

	@Override
	public void tick() {
		++ticks;
		if (ticks % 20 == 0) {
			updateCount();
		}
	}

	public void setRequestTarget(ItemStack stack) {
		if (!stack.isEmpty() && !locked) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			requestTarget = copy;
			updateCount();
			if (!world.isClient) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}

	}

	public ItemStack getRequestTarget() {
		return requestTarget;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void doRequest(boolean fullStack) {
		if (world.isClient) {
			return;
		}

		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && requestTarget != null) {
			int count = fullStack ? requestTarget.getMaxCount() : 1;
			doCorporeaRequest(CorporeaHelper.instance().createMatcher(requestTarget, true), count, spark);
		}
	}

	private void updateCount() {
		if (world.isClient) {
			return;
		}

		int sum = 0;
		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null && requestTarget != null) {
			List<ItemStack> stacks = CorporeaHelper.instance().requestItem(CorporeaHelper.instance().createMatcher(requestTarget, true), -1, spark, false).getStacks();
			for (ItemStack stack : stacks) {
				sum += stack.getCount();
			}
		}

		setCount(sum);
	}

	private void setCount(int count) {
		int oldCount = this.itemCount;
		this.itemCount = count;
		if (this.itemCount != oldCount) {
			int oldCompValue = this.compValue;
			this.compValue = CorporeaHelper.instance().signalStrengthForRequestSize(itemCount);
			if (this.compValue != oldCompValue && this.world != null) {
				this.world.updateComparators(this.pos, getCachedState().getBlock());
			}
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);
		CompoundTag cmp = new CompoundTag();
		if (!requestTarget.isEmpty()) {
			cmp = requestTarget.toTag(cmp);
		}
		tag.put(TAG_REQUEST_TARGET, cmp);
		tag.putInt(TAG_ITEM_COUNT, itemCount);
		tag.putBoolean(TAG_LOCK, locked);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);
		CompoundTag cmp = tag.getCompound(TAG_REQUEST_TARGET);
		requestTarget = ItemStack.fromTag(cmp);
		setCount(tag.getInt(TAG_ITEM_COUNT));
		locked = tag.getBoolean(TAG_LOCK);
	}

	public int getComparatorValue() {
		return compValue;
	}

	@Override
	public void doCorporeaRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, true).getStacks();
		spark.onItemsRequested(stacks);
		boolean did = false;
		int sum = 0;
		for (ItemStack reqStack : stacks) {
			if (requestTarget != null) {
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reqStack);
				world.spawnEntity(item);
				sum += reqStack.getCount();
				did = true;
			}
		}

		if (did) {
			setCount(getItemCount() - sum);
		}
	}
}
