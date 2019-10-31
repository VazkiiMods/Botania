/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 30, 2015, 3:57:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class TileCorporeaCrystalCube extends TileCorporeaBase implements ICorporeaRequestor, ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.CORPOREA_CRYSTAL_CUBE)
	public static TileEntityType<TileCorporeaCrystalCube> TYPE;
	private static final String TAG_REQUEST_TARGET = "requestTarget";
	private static final String TAG_ITEM_COUNT = "itemCount";
	private static final String TAG_LOCK = "lock";

	private ItemStack requestTarget = ItemStack.EMPTY;
	private int itemCount = 0;
	private int ticks = 0;
	private int compValue = 0;
	public boolean locked = false;

	public TileCorporeaCrystalCube() {
		super(TYPE);
	}

	@Override
	public void tick() {
		++ticks;
		if(ticks % 20 == 0)
			updateCount();
	}

	public void setRequestTarget(ItemStack stack) {
		if(!stack.isEmpty() && !locked) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			requestTarget = copy;
			updateCount();
			if(!world.isRemote)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}

	}

	public ItemStack getRequestTarget() {
		return requestTarget;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void doRequest(boolean fullStack) {
		if(world.isRemote)
			return;

		ICorporeaSpark spark = getSpark();
		if(spark != null && spark.getMaster() != null && requestTarget != null) {
			int count = fullStack ? requestTarget.getMaxStackSize() : 1;
			doCorporeaRequest(requestTarget, count, spark);
		}
	}

	private void updateCount() {
		if(world.isRemote)
			return;

		int oldCount = itemCount;
		itemCount = 0;
		ICorporeaSpark spark = getSpark();
		if(spark != null && spark.getMaster() != null && requestTarget != null) {
			List<ItemStack> stacks = CorporeaHelper.requestItem(requestTarget, -1, spark, true, false);
			for(ItemStack stack : stacks)
				itemCount += stack.getCount();
		}

		if(itemCount != oldCount) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			onUpdateCount();
		}
	}

	private void onUpdateCount() {
		int oldCompValue = compValue;
		compValue = CorporeaHelper.signalStrengthForRequestSize(itemCount);
		if(compValue != oldCompValue)
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}

	@Override
	public void writePacketNBT(CompoundNBT par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		CompoundNBT cmp = new CompoundNBT();
		if(!requestTarget.isEmpty())
			cmp = requestTarget.write(cmp);
		par1nbtTagCompound.put(TAG_REQUEST_TARGET, cmp);
		par1nbtTagCompound.putInt(TAG_ITEM_COUNT, itemCount);
		par1nbtTagCompound.putBoolean(TAG_LOCK, locked);
	}

	@Override
	public void readPacketNBT(CompoundNBT par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		CompoundNBT cmp = par1nbtTagCompound.getCompound(TAG_REQUEST_TARGET);
		requestTarget = ItemStack.read(cmp);
		itemCount = par1nbtTagCompound.getInt(TAG_ITEM_COUNT);
		locked = par1nbtTagCompound.getBoolean(TAG_LOCK);
	}

	public int getComparatorValue() {
		return compValue;
	}

	@Override
	public void doCorporeaRequest(Object request, int count, ICorporeaSpark spark) {
		if(!(request instanceof ItemStack))
			return;

		List<ItemStack> stacks = CorporeaHelper.requestItem(request, count, spark, true, true);
		spark.onItemsRequested(stacks);
		boolean did = false;
		for(ItemStack reqStack : stacks)
			if(requestTarget != null) {
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reqStack);
				world.addEntity(item);
				itemCount -= reqStack.getCount();
				did = true;
			}

		if(did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			onUpdateCount();
		}
	}
}
