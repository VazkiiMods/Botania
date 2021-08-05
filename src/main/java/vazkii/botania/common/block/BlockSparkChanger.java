/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.TileSparkChanger;
import vazkii.botania.common.item.ItemSparkUpgrade;

import javax.annotation.Nonnull;

public class BlockSparkChanger extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 3, 16);

	public BlockSparkChanger(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, true));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			((TileSparkChanger) world.getBlockEntity(pos)).doSwap();
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TileSparkChanger changer = (TileSparkChanger) world.getBlockEntity(pos);
		ItemStack pstack = player.getItemInHand(hand);
		ItemStack cstack = changer.getItemHandler().getItem(0);
		if (!cstack.isEmpty()) {
			changer.getItemHandler().setItem(0, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(player.level, cstack);
			return InteractionResult.SUCCESS;
		} else if (!pstack.isEmpty() && pstack.getItem() instanceof ItemSparkUpgrade) {
			changer.getItemHandler().setItem(0, pstack.split(1));
			changer.setChanged();

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		TileSparkChanger changer = (TileSparkChanger) world.getBlockEntity(pos);
		ItemStack stack = changer.getItemHandler().getItem(0);
		if (!stack.isEmpty() && stack.getItem() instanceof ItemSparkUpgrade) {
			return ((ItemSparkUpgrade) stack.getItem()).type.ordinal() + 1;
		}
		return 0;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileSparkChanger(pos, state);
	}

}
