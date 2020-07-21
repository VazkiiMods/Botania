/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.corporea.ICorporeaInterceptor;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.block.tile.ModTiles;

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

				if (missing > 0 && !getCachedState().get(Properties.POWERED)) {
					world.setBlockState(getPos(), getCachedState().with(Properties.POWERED, true));
					world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 2);

					BlockEntity requestor = source.getSparkInventory().getWorld().getBlockEntity(source.getSparkInventory().getPos());
					for (Direction dir : Direction.values()) {
						BlockEntity tile = world.getBlockEntity(pos.offset(dir));
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
			List<ItemFrameEntity> frames = world.getNonSpectatingEntities(ItemFrameEntity.class, new Box(pos.offset(dir), pos.offset(dir).add(1, 1, 1)));
			for (ItemFrameEntity frame : frames) {
				Direction orientation = frame.getHorizontalFacing();
				if (orientation == dir) {
					filter.add(frame.getHeldItemStack());
				}
			}
		}

		return filter;
	}

}
