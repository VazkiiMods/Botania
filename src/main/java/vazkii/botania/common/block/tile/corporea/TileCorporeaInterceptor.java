/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 19, 2015, 6:21:08 PM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.CORPOREA_INTERCEPTOR;
	}

	@Override
	public void interceptRequest(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<IInventory> inventories, boolean doit) {
		// NO-OP
	}

	@Override
	public void interceptRequestLast(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<IInventory> inventories, boolean doit) {
		List<ItemStack> filter = getFilter();
		for(ItemStack stack : filter)
			if(requestMatches(request, stack)) {
				int missing = count;
				for(ItemStack stack_ : stacks)
					missing -= stack_.stackSize;

				if(missing > 0 && getBlockMetadata() == 0) {
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1 | 2);
					worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType(), 2);

					TileEntity requestor = (TileEntity) source.getInventory();
					for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
						TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ);
						if(tile != null && tile instanceof TileCorporeaRetainer)
							((TileCorporeaRetainer) tile).setPendingRequest(requestor.xCoord, requestor.yCoord, requestor.zCoord, request, count);
					}

					return;
				}
			}

	}

	public boolean requestMatches(Object request, ItemStack filter) {
		if(filter == null)
			return false;

		if(request instanceof ItemStack) {
			ItemStack stack = (ItemStack) request;
			return stack != null && stack.isItemEqual(filter) && ItemStack.areItemStackTagsEqual(filter, stack);
		}

		String name = (String) request;
		return CorporeaHelper.stacksMatch(filter, name);
	}

	public List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList();

		final int[] orientationToDir = new int[] {
				3, 4, 2, 5
		};

		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			List<EntityItemFrame> frames = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, AxisAlignedBB.getBoundingBox(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, xCoord + dir.offsetX + 1, yCoord + dir.offsetY + 1, zCoord + dir.offsetZ + 1));
			for(EntityItemFrame frame : frames) {
				int orientation = frame.hangingDirection;
				if(orientationToDir[orientation] == dir.ordinal())
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}


}
