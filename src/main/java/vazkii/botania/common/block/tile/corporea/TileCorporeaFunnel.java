/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 16, 2015, 2:18:30 PM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileCorporeaFunnel extends TileCorporeaBase implements ICorporeaRequestor {

	public void doRequest() {
		ICorporeaSpark spark = getSpark();
		if(spark != null && spark.getMaster() != null) {
			List<ItemStack> filter = getFilter();
			if(!filter.isEmpty()) {
				ItemStack stack = filter.get(worldObj.rand.nextInt(filter.size()));

				if(stack != null)
					doCorporeaRequest(stack, stack.stackSize, spark);
			}
		}
	}

	public List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		final int[] rotationToStackSize = new int[] {
				1, 2, 4, 8, 16, 32, 48, 64
		};

		for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS) {
			List<EntityItemFrame> frames = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for(EntityItemFrame frame : frames) {
				EnumFacing orientation = frame.facingDirection;
				if(orientation == dir) {
					ItemStack stack = frame.getDisplayedItem();
					if(stack != null) {
						ItemStack copy = stack.copy();
						copy.stackSize = rotationToStackSize[frame.getRotation()];
						filter.add(copy);
					}
				}
			}
		}

		return filter;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getName() {
		return LibBlockNames.CORPOREA_FUNNEL;
	}

	@Override
	public void doCorporeaRequest(Object request, int count, ICorporeaSpark spark) {
		if(!(request instanceof ItemStack))
			return;

		IInventory inv = InventoryHelper.getInventory(worldObj, getPos().down());
		if(inv == null || inv instanceof TileCorporeaFunnel)
			inv = InventoryHelper.getInventory(worldObj, getPos().down(2));

		List<ItemStack> stacks = CorporeaHelper.requestItem(request, count, spark, true, true);
		spark.onItemsRequested(stacks);
		for(ItemStack reqStack : stacks) {
			if(inv != null && !(inv instanceof TileCorporeaFunnel) && reqStack.stackSize == InventoryHelper.testInventoryInsertion(inv, reqStack, EnumFacing.UP))
				InventoryHelper.insertItemIntoInventory(inv, reqStack);
			else {
				EntityItem item = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reqStack);
				worldObj.spawnEntityInWorld(item);
			}
		}
	}

}
