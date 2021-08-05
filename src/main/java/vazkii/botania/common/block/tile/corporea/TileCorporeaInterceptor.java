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

public class TileCorporeaInterceptor extends TileCorporeaBase implements ICorporeaInterceptor {
	public TileCorporeaInterceptor(BlockPos pos, BlockState state) {
		super(ModTiles.CORPOREA_INTERCEPTOR, pos, state);
	}

	@Override
	public void interceptRequest(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<ICorporeaNode> nodes, boolean doit) {}

	@Override
	public void interceptRequestLast(ICorporeaRequestMatcher request, int count, ICorporeaSpark spark, ICorporeaSpark source, List<ItemStack> stacks, List<ICorporeaNode> nodes, boolean doit) {
		List<ItemStack> filter = getFilter();
		for (ItemStack stack : filter) {
			if (request.test(stack)) {
				int missing = count;
				for (ItemStack stack_ : stacks) {
					missing -= stack_.getCount();
				}

				if (missing > 0 && !getBlockState().getValue(BlockStateProperties.POWERED)) {
					level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true));
					level.getBlockTicks().scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);

					BlockPos requestorPos = source.getSparkNode().getPos();
					for (Direction dir : Direction.values()) {
						BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
						if (tile instanceof TileCorporeaRetainer) {
							((TileCorporeaRetainer) tile).remember(requestorPos, request, count, missing);
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
			List<ItemFrame> frames = level.getEntitiesOfClass(ItemFrame.class, new AABB(worldPosition.relative(dir), worldPosition.relative(dir).offset(1, 1, 1)));
			for (ItemFrame frame : frames) {
				Direction orientation = frame.getDirection();
				if (orientation == dir) {
					filter.add(frame.getItem());
				}
			}
		}

		return filter;
	}

}
