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

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
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
	int requestX, requestY, requestZ;
	Object request;
	int requestCount;

	public void setPendingRequest(int x, int y, int z, Object request, int requestCount) {
		if(pendingRequest)
			return;

		requestX = x;
		requestY = y;
		requestZ = z;
		this.request = request;
		this.requestCount = requestCount;
		pendingRequest = true;
		worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}

	public boolean hasPendingRequest() {
		return pendingRequest;
	}

	public void fulfilRequest() {
		if(!hasPendingRequest())
			return;

		ICorporeaSpark spark = CorporeaHelper.getSparkForBlock(worldObj, requestX, requestY, requestZ);
		if(spark != null) {
			IInventory inv = spark.getInventory();
			if(inv != null && inv instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) inv;
				requestor.doCorporeaRequest(request, requestCount, spark);
				pendingRequest = false;
				worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);

		cmp.setBoolean(TAG_PENDING_REQUEST, pendingRequest);
		cmp.setInteger(TAG_REQUEST_X, requestX);
		cmp.setInteger(TAG_REQUEST_Y, requestY);
		cmp.setInteger(TAG_REQUEST_Z, requestZ);

		int reqType = REQUEST_NULL;
		if(request != null)
			reqType = request instanceof String ? REQUEST_STRING : REQUEST_ITEMSTACK;
		cmp.setInteger(TAG_REQUEST_TYPE, reqType);

		switch (reqType) {
		case REQUEST_STRING:
			cmp.setString(TAG_REQUEST_CONTENTS, (String) request);
			break;
		case REQUEST_ITEMSTACK:
			NBTTagCompound cmp1 = new NBTTagCompound();
			((ItemStack) request).writeToNBT(cmp1);
			cmp.setTag(TAG_REQUEST_STACK, cmp1);
			break;
		default: break;
		}
		cmp.setInteger(TAG_REQUEST_COUNT, requestCount);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		pendingRequest = cmp.getBoolean(TAG_PENDING_REQUEST);
		requestX = cmp.getInteger(TAG_REQUEST_X);
		requestY = cmp.getInteger(TAG_REQUEST_Y);
		requestZ = cmp.getInteger(TAG_REQUEST_Z);

		int reqType = cmp.getInteger(TAG_REQUEST_TYPE);
		switch (reqType) {
		case REQUEST_STRING:
			request = cmp.getString(TAG_REQUEST_CONTENTS);
			break;
		case REQUEST_ITEMSTACK:
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_REQUEST_STACK);
			request = ItemStack.loadItemStackFromNBT(cmp1);
			break;
		default: break;
		}
		requestCount = cmp.getInteger(TAG_REQUEST_COUNT);
	}

}
