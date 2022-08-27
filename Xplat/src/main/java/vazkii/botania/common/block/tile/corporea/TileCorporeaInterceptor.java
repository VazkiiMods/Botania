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
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.block.tile.ModTiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {
	public TileCorporeaInterceptor(BlockPos pos, BlockState state) {
		super(ModTiles.CORPOREA_INTERCEPTOR, pos, state);
	}

	@Override
	public void interceptRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, Set<ICorporeaNode> nodes, boolean doit) {}

	@Override
	public void interceptRequestLast(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, Set<ICorporeaNode> nodes, boolean doit) {
		List<ItemStack> filter = getFilter();

		boolean filterMatch = false;
		for (ItemStack stack : filter) {
			if (request.test(stack)) {
				filterMatch = true;
			}
		}

		if (filterMatch || filter.isEmpty()) {
			int missing = count;
			for (ItemStack stack : stacks) {
				missing -= stack.getCount();
			}

			if (missing > 0 && !getBlockState().getValue(BlockStateProperties.POWERED)) {
				BlockPos requestorPos = source.getSparkNode().getPos();

				List<TileCorporeaRetainer> retainers = new ArrayList<>();
				for (Direction dir : Direction.values()) {
					BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
					if (tile instanceof TileCorporeaRetainer retainer) {
						retainers.add(retainer);
						retainer.forget();
					}
				}

				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true));
				level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);

				for (TileCorporeaRetainer retainer : retainers) {
					retainer.remember(requestorPos, request, count, missing);
				}
			}
		}
	}

	private List<ItemStack> getFilter() {
		List<ItemStack> filter = new ArrayList<>();

		for (Direction dir : Direction.values()) {
			List<ItemFrame> frames = level.getEntitiesOfClass(ItemFrame.class, new AABB(worldPosition.relative(dir), worldPosition.relative(dir).offset(1, 1, 1)));
			for (ItemFrame frame : frames) {
				Direction orientation = frame.getDirection();
				if (orientation == dir) {
					ItemStack stack = frame.getItem();
					if (!stack.isEmpty()) {
						filter.add(stack);
					}
				}
			}
		}

		return filter;
	}

}
