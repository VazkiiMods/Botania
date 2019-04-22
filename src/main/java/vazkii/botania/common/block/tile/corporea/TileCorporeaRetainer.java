/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 28, 2015, 11:55:56 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.block.tile.TileMod;

public class TileCorporeaRetainer extends TileMod {

	private static final String TAG_PENDING_REQUEST = "pendingRequest";
	private static final String TAG_REQUEST_X = "requestX";
	private static final String TAG_REQUEST_Y = "requestY";
	private static final String TAG_REQUEST_Z = "requestZ";
	private static final String TAG_REQUEST_TYPE = "requestType";
	private static final String TAG_REQUEST_CONTENTS = "requestContents";
	private static final String TAG_REQUEST_STACK = "requestStack";
	private static final String TAG_REQUEST_COUNT = "requestCount";

	private static final int REQUEST_NULL = 0;
	private static final int REQUEST_ITEMSTACK = 1;
	private static final int REQUEST_STRING = 2;

	boolean pendingRequest = false;
	BlockPos requestPos = BlockPos.ORIGIN;
	Object request;
	int requestCount;
	int compValue;

	public void setPendingRequest(BlockPos pos, Object request, int requestCount) {
		if(pendingRequest)
			return;

		requestPos = pos;
		this.request = request;
		this.requestCount = requestCount;
		pendingRequest = true;
		
		compValue = CorporeaHelper.signalStrengthForRequestSize(requestCount);
		world.updateComparatorOutputLevel(getPos(), world.getBlockState(getPos()).getBlock());
	}
	
	public int getComparatorValue() {
		return compValue;
	}
	
	public boolean hasPendingRequest() {
		return pendingRequest;
	}

	public void fulfilRequest() {
		if(!hasPendingRequest())
			return;

		ICorporeaSpark spark = CorporeaHelper.getSparkForBlock(world, requestPos);
		if(spark != null) {
			InvWithLocation inv = spark.getSparkInventory();
			if(inv != null && inv.world.getTileEntity(inv.pos) instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) inv.world.getTileEntity(inv.pos);
				requestor.doCorporeaRequest(request, requestCount, spark);
				pendingRequest = false;
				
				compValue = 0;
				world.updateComparatorOutputLevel(getPos(), world.getBlockState(getPos()).getBlock());
			}
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);

		cmp.setBoolean(TAG_PENDING_REQUEST, pendingRequest);
		cmp.setInteger(TAG_REQUEST_X, requestPos.getX());
		cmp.setInteger(TAG_REQUEST_Y, requestPos.getY());
		cmp.setInteger(TAG_REQUEST_Z, requestPos.getZ());

		int reqType = REQUEST_NULL;
		if(request != null)
			reqType = request instanceof String ? REQUEST_STRING : REQUEST_ITEMSTACK;
		cmp.setInteger(TAG_REQUEST_TYPE, reqType);

		switch (reqType) {
		case REQUEST_STRING:
			cmp.setString(TAG_REQUEST_CONTENTS, (String) request);
			break;
		case REQUEST_ITEMSTACK:
			NBTTagCompound cmp1 = ((ItemStack) request).writeToNBT(new NBTTagCompound());
			cmp.setTag(TAG_REQUEST_STACK, cmp1);
			break;
		default: break;
		}
		cmp.setInteger(TAG_REQUEST_COUNT, requestCount);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);

		pendingRequest = cmp.getBoolean(TAG_PENDING_REQUEST);
		int x = cmp.getInteger(TAG_REQUEST_X);
		int y = cmp.getInteger(TAG_REQUEST_Y);
		int z = cmp.getInteger(TAG_REQUEST_Z);
		requestPos = new BlockPos(x, y, z);

		int reqType = cmp.getInteger(TAG_REQUEST_TYPE);
		switch (reqType) {
		case REQUEST_STRING:
			request = cmp.getString(TAG_REQUEST_CONTENTS);
			break;
		case REQUEST_ITEMSTACK:
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_REQUEST_STACK);
			request = new ItemStack(cmp1);
			break;
		default: break;
		}
		requestCount = cmp.getInteger(TAG_REQUEST_COUNT);
	}

}
