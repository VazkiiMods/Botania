/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

public class BlockOpenCrate extends BlockMod implements ITileEntityProvider {

	protected BlockOpenCrate(Properties builder) {
		super(builder);
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (newState.getBlock() != state.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileSimpleInventory) {
				InventoryHelper.dropInventoryItems(world, pos, ((TileSimpleInventory) te).getItemHandler());
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileOpenCrate();
	}

}
