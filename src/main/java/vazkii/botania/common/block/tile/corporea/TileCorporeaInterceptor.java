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

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.state.BotaniaStateProps;

import java.util.ArrayList;
import java.util.List;

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {

	@Override
	public void interceptRequest(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit) {}

	@Override
	public void interceptRequestLast(Object request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit) {
		List<ItemStack> filter = getFilter();
		for(ItemStack stack : filter)
			if(requestMatches(request, stack)) {
				int missing = count;
				for(ItemStack stack_ : stacks)
					missing -= stack_.getCount();

				if(missing > 0 && !world.getBlockState(getPos()).getValue(BotaniaStateProps.POWERED)) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
					world.scheduleUpdate(getPos(), getBlockType(), 2);

					TileEntity requestor = source.getSparkInventory().world.getTileEntity(source.getSparkInventory().pos);
					for(EnumFacing dir : EnumFacing.HORIZONTALS) {
						TileEntity tile = world.getTileEntity(pos.offset(dir));
						if(tile != null && tile instanceof TileCorporeaRetainer)
							((TileCorporeaRetainer) tile).setPendingRequest(requestor.getPos(), request, count);
					}

					return;
				}
			}

	}

	public boolean requestMatches(Object request, ItemStack filter) {
		if(filter.isEmpty())
			return false;

		if(request instanceof ItemStack) {
			ItemStack stack = (ItemStack) request;
			return !stack.isEmpty() && stack.isItemEqual(filter) && ItemStack.areItemStackTagsEqual(filter, stack);
		}

		String name = (String) request;
		return CorporeaHelper.stacksMatch(filter, name);
	}

	public List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			List<EntityItemFrame> frames = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for(EntityItemFrame frame : frames) {
				EnumFacing orientation = frame.facingDirection;
				if(orientation == dir)
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}


}
