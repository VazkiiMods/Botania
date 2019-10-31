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

import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.CORPOREA_INTERCEPTOR)
	public static TileEntityType<TileCorporeaInterceptor> TYPE;

	public TileCorporeaInterceptor() {
		super(TYPE);
	}

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

				if(missing > 0 && !world.getBlockState(getPos()).get(BotaniaStateProps.POWERED)) {
					world.setBlockState(getPos(), world.getBlockState(getPos()).with(BotaniaStateProps.POWERED, true), 1 | 2);
					world.getPendingBlockTicks().scheduleTick(getPos(), getBlockState().getBlock(), 2);

					TileEntity requestor = source.getSparkInventory().world.getTileEntity(source.getSparkInventory().pos);
					for(Direction dir : Direction.values()) {
						TileEntity tile = world.getTileEntity(pos.offset(dir));
						if(tile instanceof TileCorporeaRetainer)
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

		for(Direction dir : Direction.values()) {
			List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for(ItemFrameEntity frame : frames) {
				Direction orientation = frame.getHorizontalFacing();
				if(orientation == dir)
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}


}
