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
import net.minecraft.world.ItemInteractionResult;
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

import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.block.block_entity.SparkTinkererBlockEntity;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.lib.BotaniaTags;

public class SparkTinkererBlock extends BotaniaWaterloggedBlock implements EntityBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 3, 16);

	public SparkTinkererBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWERED, true));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos,
			boolean movedByPiston) {
		boolean power = level.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (power && !powered) {
			if (level.getBlockEntity(pos) instanceof SparkTinkererBlockEntity tinkerer) {
				tinkerer.doSwap();
			}
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), Block.UPDATE_CLIENTS);
		} else if (!power && powered) {
			level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof SparkTinkererBlockEntity changer)) {
			return ItemInteractionResult.FAIL;
		}
		ItemStack cstack = changer.getItemHandler().getItem(0);
		if (!cstack.isEmpty()) {
			changer.getItemHandler().setItem(0, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(cstack);
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		} else if (!stack.isEmpty() && stack.is(BotaniaTags.Items.MANA_SPARK_AUGMENTS)) {
			changer.getItemHandler().setItem(0, stack.split(1));
			changer.setChanged();

			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof SimpleInventoryBlockEntity inventory) {
				Containers.dropContents(level, pos, inventory.getItemHandler());
			}
			super.onRemove(state, level, pos, newState, movedByPiston);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof SparkTinkererBlockEntity changer) {
			ItemStack stack = changer.getItemHandler().getItem(0);
			if (!stack.isEmpty() && stack.is(BotaniaTags.Items.MANA_SPARK_AUGMENTS)) {
				return stack.getOrDefault(BotaniaDataComponents.AUGMENT_ID, (byte) 0) + 1;
			}
		}
		return 0;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SparkTinkererBlockEntity(pos, state);
	}

}
