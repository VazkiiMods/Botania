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
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.state.BotaniaStateProps;
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
	public String getName() {
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

				if(missing > 0 && !worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED)) {
					worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
					worldObj.scheduleUpdate(getPos(), getBlockType(), 2);

					TileEntity requestor = (TileEntity) source.getSparkInventory();
					for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS) {
						TileEntity tile = worldObj.getTileEntity(pos.offset(dir));
						if(tile != null && tile instanceof TileCorporeaRetainer)
							((TileCorporeaRetainer) tile).setPendingRequest(requestor.getPos(), request, count);
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
		List<ItemStack> filter = new ArrayList<>();

		for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS) {
			List<EntityItemFrame> frames = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for(EntityItemFrame frame : frames) {
				EnumFacing orientation = frame.facingDirection;
				if(orientation == dir)
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}


}
