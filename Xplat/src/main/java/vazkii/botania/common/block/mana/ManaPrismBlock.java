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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.api.mana.BasicLensItem;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaWaterloggedEntityBlock;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;

public class ManaPrismBlock extends BotaniaWaterloggedEntityBlock
		implements ManaCollisionGhost, RedstoneSensitiveBlock {
	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 16, 12);

	public ManaPrismBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.POWERED, false)
				.setValue(BotaniaStateProperties.HAS_LENS, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext econtext
				&& econtext.getEntity() instanceof ManaBurstEntity) {
			// Expose the shape so bursts can actually collide with us
			// they will still go through the prism via ManaCollisionGhost
			return SHAPE;
		} else {
			return super.getCollisionShape(state, level, pos, context);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (isPowered(state)) {
			RedstoneSensitiveBlock.redstoneParticlesInShape(state, level, pos, random);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProperties.HAS_LENS);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof ManaPrismBlockEntity prism)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		ItemStack lens = prism.getItemHandler().getItem(0);
		boolean playerHasLens = !stack.isEmpty() && stack.getItem() instanceof BasicLensItem;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameComponents(stack, lens);
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		if (playerHasLens && !lensIsSame) {
			ItemStack toInsert = stack.split(1);

			if (!lens.isEmpty()) {
				player.getInventory().placeItemBackInInventory(lens);
			}

			prism.getItemHandler().setItem(0, toInsert);
			level.playSound(player, pos, BotaniaSounds.PRISM_ADD_LENS, SoundSource.BLOCKS, 1F, 1F);
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			player.getInventory().placeItemBackInInventory(lens);
			prism.getItemHandler().setItem(0, ItemStack.EMPTY);

			level.playSound(player, pos, BotaniaSounds.PRISM_REMOVE_LENS, SoundSource.BLOCKS, 1F, 1F);
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		RedstoneSensitiveBlock.updateRedstonePower(state, level, pos);
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
		return new ManaPrismBlockEntity(pos, state);
	}

	@Override
	public Behaviour getGhostBehaviour() {
		return Behaviour.RUN_RECEIVER_TRIGGER;
	}
}
