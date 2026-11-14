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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

import vazkii.botania.common.block.BotaniaWaterloggedEntityBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.helper.InventoryHelper;

public class RunicAltarBlock extends BotaniaWaterloggedEntityBlock {

	private static final VoxelShape TOP = Block.box(0, 6, 0, 16, 12, 16);
	private static final VoxelShape BOTTOM = Block.box(2, 0, 2, 14, 6, 14);
	private static final VoxelShape SHAPE = Shapes.join(TOP, BOTTOM, BooleanOp.OR);

	public RunicAltarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		RunicAltarBlockEntity altar = level.getBlockEntity(pos, BotaniaBlockEntities.RUNIC_ALTAR).orElseThrow();
		if (stack.isEmpty()) {
			if (altar.canAddLastRecipe()) {
				return altar.trySetLastRecipe(player);
			} else if (!altar.isEmpty() && altar.manaToGet == 0) {
				InventoryHelper.withdrawFromInventory(altar, player);
				level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		boolean result = altar.addItem(player, stack, hand);
		level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
		if (result) {
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RunicAltarBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return createTickerHelper(type, BotaniaBlockEntities.RUNIC_ALTAR, RunicAltarBlockEntity::clientTick);
		} else {
			return createTickerHelper(type, BotaniaBlockEntities.RUNIC_ALTAR, RunicAltarBlockEntity::serverTick);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof RunicAltarBlockEntity altar ? altar.signal : 0;
	}

}
