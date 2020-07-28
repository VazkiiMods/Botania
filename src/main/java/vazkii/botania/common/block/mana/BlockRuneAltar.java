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
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

public class BlockRuneAltar extends BlockModWaterloggable implements BlockEntityProvider, IWandable {

	private static final VoxelShape TOP = Block.createCuboidShape(0, 6, 0, 16, 12, 16);
	private static final VoxelShape BOTTOM = Block.createCuboidShape(2, 0, 2, 14, 6, 14);
	private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(TOP, BOTTOM, BooleanBiFunction.OR);

	public BlockRuneAltar(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}

		TileRuneAltar altar = (TileRuneAltar) world.getBlockEntity(pos);
		ItemStack stack = player.getStackInHand(hand);

		if (player.isSneaking()) {
			if (altar.manaToGet == 0) {
				InventoryHelper.withdrawFromInventory(altar, player);
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
				return ActionResult.SUCCESS;
			}
		} else if (altar.isEmpty() && stack.isEmpty()) {
			altar.trySetLastRecipe(player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return ActionResult.SUCCESS;
		} else if (!stack.isEmpty()) {
			boolean result = altar.addItem(player, stack, hand);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return result ? ActionResult.SUCCESS : ActionResult.PASS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileSimpleInventory inv = (TileSimpleInventory) world.getBlockEntity(pos);
			ItemScatterer.spawn(world, pos, inv.getItemHandler());
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileRuneAltar();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TileRuneAltar altar = (TileRuneAltar) world.getBlockEntity(pos);
		return altar.signal;
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileRuneAltar) world.getBlockEntity(pos)).onWanded(player, stack);
		return true;
	}

}
