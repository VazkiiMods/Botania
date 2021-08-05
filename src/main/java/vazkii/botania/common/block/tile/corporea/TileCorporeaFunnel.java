/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.core.helper.InventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class TileCorporeaFunnel extends TileCorporeaBase implements ICorporeaRequestor {
	public TileCorporeaFunnel(BlockPos pos, BlockState state) {
		super(ModTiles.CORPOREA_FUNNEL, pos, state);
	}

	public void doRequest() {
		ICorporeaSpark spark = getSpark();
		if (spark != null && spark.getMaster() != null) {
			List<ItemStack> filter = getFilter();
			if (!filter.isEmpty()) {
				ItemStack stack = filter.get(level.random.nextInt(filter.size()));

				if (!stack.isEmpty()) {
					doCorporeaRequest(CorporeaHelper.instance().createMatcher(stack, true), stack.getCount(), spark);
				}
			}
		}
	}

	public List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		final int[] rotationToStackSize = new int[] {
				1, 2, 4, 8, 16, 32, 48, 64
		};

		for (Direction dir : Direction.values()) {
			List<ItemFrame> frames = level.getEntitiesOfClass(ItemFrame.class, new AABB(worldPosition.relative(dir), worldPosition.relative(dir).offset(1, 1, 1)));
			for (ItemFrame frame : frames) {
				Direction orientation = frame.getDirection();
				if (orientation == dir) {
					ItemStack stack = frame.getItem();
					if (!stack.isEmpty()) {
						ItemStack copy = stack.copy();
						copy.setCount(rotationToStackSize[frame.getRotation()]);
						filter.add(copy);
					}
				}
			}
		}

		return filter;
	}

	@Override
	public void doCorporeaRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark) {
		Container inv = getInv();

		List<ItemStack> stacks = CorporeaHelper.instance().requestItem(request, count, spark, true).getStacks();
		spark.onItemsRequested(stacks);
		for (ItemStack reqStack : stacks) {
			if (inv != null && InventoryHelper.simulateTransfer(inv, reqStack, Direction.UP).isEmpty()) {
				HopperBlockEntity.addItem(null, inv, reqStack, Direction.UP);
			} else {
				ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, reqStack);
				level.addFreshEntity(item);
			}
		}
	}

	private Container getInv() {
		BlockEntity te = level.getBlockEntity(worldPosition.below());
		Container ret = InventoryHelper.getInventory(level, worldPosition.below(), Direction.UP);
		if (ret == null) {
			ret = InventoryHelper.getInventory(level, worldPosition.below(), null);
		}
		if (ret != null && !(te instanceof TileCorporeaFunnel)) {
			return ret;
		}

		te = level.getBlockEntity(worldPosition.below(2));
		ret = InventoryHelper.getInventory(level, worldPosition.below(2), Direction.UP);
		if (ret == null) {
			ret = InventoryHelper.getInventory(level, worldPosition.below(2), null);
		}
		if (ret != null && !(te instanceof TileCorporeaFunnel)) {
			return ret;
		}

		return null;
	}

}
