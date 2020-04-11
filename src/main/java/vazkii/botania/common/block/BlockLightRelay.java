/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockLightRelay extends BlockModWaterloggable implements IWandable {

	private static final VoxelShape SHAPE = makeCuboidShape(5, 5, 5, 11, 11, 11);
	public final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Properties builder) {
		super(builder);
		this.variant = variant;
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		((TileLightRelay) world.getTileEntity(pos)).mountEntity(player);
		return ActionResultType.SUCCESS;
	}

	@Override
	public int tickRate(IWorldReader world) {
		return 2;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isRemote && variant == LuminizerVariant.TOGGLE) {
			if (state.get(BlockStateProperties.POWERED) && !worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
			} else if (!state.get(BlockStateProperties.POWERED) && worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, true));
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.get(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return false;
	}

}
