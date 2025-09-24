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
import net.minecraft.world.level.block.EntityBlock;
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
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;

public class ManaPrismBlock extends BotaniaWaterloggedBlock implements EntityBlock, ManaCollisionGhost, RedstoneSensitiveBlock {
	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 16, 12);

	public ManaPrismBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.POWERED, false)
				.setValue(BotaniaStateProperties.HAS_LENS, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext econtext
				&& econtext.getEntity() instanceof ManaBurstEntity) {
			// Expose the shape so bursts can actually collide with us
			// they will still go through the prism via ManaCollisionGhost
			return SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (isPowered(state)) {
			RedstoneSensitiveBlock.redstoneParticlesInShape(state, world, pos, rand);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProperties.HAS_LENS);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(world.getBlockEntity(pos) instanceof ManaPrismBlockEntity prism)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		ItemStack lens = prism.getItemHandler().getItem(0);
		boolean playerHasLens = !heldItem.isEmpty() && heldItem.getItem() instanceof BasicLensItem;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameComponents(heldItem, lens);
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		if (playerHasLens && !lensIsSame) {
			ItemStack toInsert = heldItem.split(1);

			if (!lens.isEmpty()) {
				player.getInventory().placeItemBackInInventory(lens);
			}

			prism.getItemHandler().setItem(0, toInsert);
			world.playSound(player, pos, BotaniaSounds.prismAddLens, SoundSource.BLOCKS, 1F, 1F);
			world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			player.getInventory().placeItemBackInInventory(lens);
			prism.getItemHandler().setItem(0, ItemStack.EMPTY);

			world.playSound(player, pos, BotaniaSounds.prismRemoveLens, SoundSource.BLOCKS, 1F, 1F);
			world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		RedstoneSensitiveBlock.updateRedstonePower(state, world, pos);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (world.getBlockEntity(pos) instanceof SimpleInventoryBlockEntity inventory) {
				Containers.dropContents(world, pos, inventory.getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
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
