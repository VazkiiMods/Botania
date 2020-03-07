/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import vazkii.botania.common.block.BlockMod;

public abstract class BlockRedString extends BlockMod {

	public BlockRedString(Block.Properties builder) {
		super(builder);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}
