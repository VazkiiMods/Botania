/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;

public abstract class BlockCorporeaBase extends BlockMod implements BlockEntityProvider {

	public BlockCorporeaBase(AbstractBlock.Settings builder) {
		super(builder);
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			ItemScatterer.spawn(world, pos, inv.getItemHandler());
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

}
