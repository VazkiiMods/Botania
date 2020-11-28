/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileTinyPotato;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTinyPotato extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(6, 0, 6, 10, 6, 10);

	public BlockTinyPotato(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState()
				.with(Properties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				ItemScatterer.spawn(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileTinyPotato) {
			((TileTinyPotato) tile).interact(player, hand, player.getStackInHand(hand), hit.getSide());
			if (!world.isClient) {
				Box box = SHAPE.getBoundingBox();
				((ServerWorld) world).spawnParticles(ParticleTypes.HEART, pos.getX() + box.minX + Math.random() * (box.maxX - box.minX), pos.getY() + box.maxY, pos.getZ() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0, 0, 0, 0);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
		if (stack.hasCustomName()) {
			((TileTinyPotato) world.getBlockEntity(pos)).name = stack.getName();
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileTinyPotato();
	}
}
