/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

public class BlockOpenCrate extends BlockMod implements BlockEntityProvider, IWandable {

	protected BlockOpenCrate(Settings builder) {
		super(builder);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TileOpenCrate crate = (TileOpenCrate) world.getBlockEntity(pos);
		return crate.getSignal();
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (newState.getBlock() != state.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			InventoryHelper.dropInventory(inv, world, state, pos);
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileOpenCrate();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TileOpenCrate crate = (TileOpenCrate) world.getBlockEntity(pos);
		return crate.onWanded(world, player, stack);
	}

}
