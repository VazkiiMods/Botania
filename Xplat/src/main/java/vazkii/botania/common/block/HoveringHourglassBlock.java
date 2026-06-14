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
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.HoveringHourglassBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.item.WandOfTheForestItem;

import java.util.function.BiConsumer;

public class HoveringHourglassBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	/**
	 * Whether the hourglass is currently emitting a redstone signal.
	 */
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	/**
	 * Whether the hourglass is generally enabled, i.e. will run when it contains sand.
	 * (Toggled by mana burst hits unless containing a counter material.)
	 */
	public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
	/**
	 * Whether the hourglass is currently running, i.e. enabled and containing a non-counter material.
	 */
	public static final BooleanProperty ACTIVE = BotaniaStateProperties.ACTIVE;
	/**
	 * Whether the hourglass's content is currently locked for player interactions.
	 */
	public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
	/**
	 * Whether the hourglass is currently flipped upside down.
	 */
	public static final BooleanProperty FLIPPED = BotaniaStateProperties.FLIPPED;

	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 18.4, 12);

	protected HoveringHourglassBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(POWERED, false)
				.setValue(ENABLED, true)
				.setValue(ACTIVE, false)
				.setValue(LOCKED, false)
				.setValue(FLIPPED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(POWERED).add(ENABLED).add(ACTIVE).add(LOCKED).add(FLIPPED);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		// implementation is inspired by ChiseledBookShelfBlock::useItemOn

		if (stack.getItem() instanceof WandOfTheForestItem
				|| !(level.getBlockEntity(pos) instanceof HoveringHourglassBlockEntity hourglass)) {
			// player has wand or this block's entity data is invalid, so skip useWithoutItem
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}

		boolean locked = state.getValue(LOCKED);
		boolean canRemove = !hourglass.getContents().isEmpty();
		boolean canInsert = HourglassMaterial.LOOKUP.find(stack) != null;
		if (locked || canRemove || !canInsert) {
			// hourglass is locked, already filled, or the item is not an hourglass material
			return canRemove || locked && canInsert
					// only show lock notice if trying to insert or remove material
					? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
					: ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}

		// put the material into the hourglass
		if (!level.isClientSide()) {
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			hourglass.setContents(stack.consumeAndReturn(stack.getCount(), player));
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {
		// implementation is inspired by ChiseledBookShelfBlock::useWithoutItem

		if (!(level.getBlockEntity(pos) instanceof HoveringHourglassBlockEntity hourglass)) {
			// this block's entity data is invalid
			return InteractionResult.PASS;
		}

		// we already know the player is not using a Wand of the Forest, so the interaction will be consumed

		if (state.getValue(LOCKED)) {
			if (level.isClientSide()) {
				player.displayClientMessage(Component.translatable("botaniamisc.hourglassLock"), true);
			}
			// There's BS going on in the server code. "Fail" doesn't prevent the item itself from being used/placed.
			return InteractionResult.CONSUME_PARTIAL;
		}

		ItemStack hourglassStack = hourglass.getContents();
		if (hourglassStack.isEmpty()) {
			return InteractionResult.CONSUME;
		}

		// remove material from the hourglass
		if (!level.isClientSide()) {
			hourglass.setContents(ItemStack.EMPTY);
			if (!player.addItem(hourglassStack)) {
				player.drop(hourglassStack, false);
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		return state.getValue(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		// ensure hourglass doesn't stay powered forever if it doesn't have a scheduled tick to turn itself off
		if (!level.isClientSide() && !state.is(oldState.getBlock()) && state.getValue(POWERED)
				&& !level.getBlockTicks().hasScheduledTick(pos, this)) {
			level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
			level.updateNeighborsAt(pos, state.getBlock());
		}
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		SimpleInventoryBlockEntity.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HoveringHourglassBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return state.getValue(ACTIVE)
				? createTickerHelper(type, BotaniaBlockEntities.HOVERING_HOURGLASS, HoveringHourglassBlockEntity::commonTick)
				: null;
	}

	@Override
	protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
		if (explosion.canTriggerBlocks() && level.getBlockEntity(pos) instanceof HoveringHourglassBlockEntity hourglass) {
			hourglass.flipEarly();
		}

		super.onExplosionHit(state, level, pos, explosion, dropConsumer);
	}
}
