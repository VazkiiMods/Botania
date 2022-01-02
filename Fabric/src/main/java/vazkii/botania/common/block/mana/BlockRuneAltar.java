/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

import javax.annotation.Nonnull;

public class BlockRuneAltar extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape TOP = Block.box(0, 6, 0, 16, 12, 16);
	private static final VoxelShape BOTTOM = Block.box(2, 0, 2, 14, 6, 14);
	private static final VoxelShape SHAPE = Shapes.join(TOP, BOTTOM, BooleanOp.OR);

	public BlockRuneAltar(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		TileRuneAltar altar = (TileRuneAltar) world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);

		if (player.isShiftKeyDown()) {
			if (altar.manaToGet == 0) {
				InventoryHelper.withdrawFromInventory(altar, player);
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
				return InteractionResult.SUCCESS;
			}
		} else if (altar.isEmpty() && stack.isEmpty()) {
			altar.trySetLastRecipe(player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return InteractionResult.SUCCESS;
		} else if (!stack.isEmpty()) {
			boolean result = altar.addItem(player, stack, hand);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return result ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileRuneAltar(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide) {
			return createTickerHelper(type, ModTiles.RUNE_ALTAR, TileRuneAltar::clientTick);
		} else {
			return createTickerHelper(type, ModTiles.RUNE_ALTAR, TileRuneAltar::serverTick);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		TileRuneAltar altar = (TileRuneAltar) world.getBlockEntity(pos);
		return altar.signal;
	}

}
