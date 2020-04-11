/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.mana.TilePump;

import javax.annotation.Nonnull;

public class BlockPump extends BlockModWaterloggable {

	private static final VoxelShape X_SHAPE = makeCuboidShape(0, 0, 4, 16, 8, 12);
	private static final VoxelShape Z_SHAPE = makeCuboidShape(4, 0, 0, 12, 8, 16);

	public BlockPump(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Nonnull
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		if (state.get(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TilePump) world.getTileEntity(pos)).comparator;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TilePump();
	}
}
