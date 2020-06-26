/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {
	public TileCorporeaInterceptor() {
		super(ModTiles.CORPOREA_INTERCEPTOR);
	}

	@Override
	public void interceptRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit) {}

	@Override
	public void interceptRequestLast(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<InvWithLocation> inventories, boolean doit) {
		List<ItemStack> filter = getFilter();
		for (ItemStack stack : filter) {
			if (request.isStackValid(stack)) {
				int missing = count;
				for (ItemStack stack_ : stacks) {
					missing -= stack_.getCount();
				}

				if (missing > 0 && !getBlockState().get(BlockStateProperties.POWERED)) {
					world.setBlockState(getPos(), getBlockState().with(BlockStateProperties.POWERED, true));
					world.getPendingBlockTicks().scheduleTick(getPos(), getBlockState().getBlock(), 2);

					TileEntity requestor = source.getSparkInventory().getWorld().getTileEntity(source.getSparkInventory().getPos());
					for (Direction dir : Direction.values()) {
						TileEntity tile = world.getTileEntity(pos.offset(dir));
						if (tile instanceof TileCorporeaRetainer) {
							((TileCorporeaRetainer) tile).setPendingRequest(requestor.getPos(), request, count);
						}
					}

					return;
				}
			}
		}

	}

	private List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		for (Direction dir : Direction.values()) {
			List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for (ItemFrameEntity frame : frames) {
				Direction orientation = frame.getHorizontalFacing();
				if (orientation == dir) {
					filter.add(frame.getDisplayedItem());
				}
			}
		}

		return filter;
	}

}
