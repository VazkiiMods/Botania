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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockLightRelay extends BlockModWaterloggable implements BlockEntityProvider, IWandable {

	private static final VoxelShape SHAPE = createCuboidShape(5, 5, 5, 11, 11, 11);
	public final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Settings builder) {
		super(builder);
		this.variant = variant;
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).getItem() != Items.ENDER_PEARL) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TileLightRelay) {
				((TileLightRelay) te).mountEntity(player);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClient && variant == LuminizerVariant.TOGGLE) {
			if (state.get(Properties.POWERED) && !worldIn.isReceivingRedstonePower(pos)) {
				worldIn.setBlockState(pos, state.with(Properties.POWERED, false));
			} else if (!state.get(Properties.POWERED) && worldIn.isReceivingRedstonePower(pos)) {
				worldIn.setBlockState(pos, state.with(Properties.POWERED, true));
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(Properties.POWERED, false));
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.get(Properties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return false;
	}

}
