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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.handler.ModSounds;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockPrism extends BlockModWaterloggable implements EntityBlock, IManaCollisionGhost {
	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 16, 12);

	public BlockPrism(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.POWERED, false)
				.setValue(BotaniaStateProps.HAS_LENS, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		if (context instanceof EntityCollisionContext econtext
				&& econtext.getEntity() instanceof EntityManaBurst) {
			// Expose the shape so bursts can actually collide with us
			// they will still go through the prism via IManaCollisionGhost
			return SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			redstoneParticlesInShape(state, world, pos, rand);
		}
	}

	public static void redstoneParticlesInShape(BlockState state, Level world, BlockPos pos, Random rand) {
		if (rand.nextBoolean()) {
			AABB localBox = state.getShape(world, pos).bounds();
			double x = pos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
			double y = pos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
			double z = pos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
			world.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TilePrism prism)) {
			return InteractionResult.PASS;
		}

		ItemStack lens = prism.getItemHandler().getItem(0);
		ItemStack heldItem = player.getItemInHand(hand);
		boolean playerHasLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameTags(heldItem, lens);
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		if (playerHasLens && !lensIsSame) {
			ItemStack toInsert = heldItem.split(1);

			if (!lens.isEmpty()) {
				player.getInventory().placeItemBackInInventory(lens);
			}

			prism.getItemHandler().setItem(0, toInsert);
			world.playSound(player, pos, ModSounds.prismAddLens, SoundSource.BLOCKS, 1F, 1F);
			return InteractionResult.SUCCESS;
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			player.getInventory().placeItemBackInInventory(lens);
			prism.getItemHandler().setItem(0, ItemStack.EMPTY);

			world.playSound(player, pos, ModSounds.prismRemoveLens, SoundSource.BLOCKS, 1F, 1F);

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		boolean power = world.getBestNeighborSignal(pos) > 0;
		return super.getStateForPlacement(context).setValue(BlockStateProperties.POWERED, power);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (!world.isClientSide) {
			if (power && !powered) {
				world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, true));
			} else if (!power && powered) {
				world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
			}
		}
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory inventory) {
				Containers.dropContents(world, pos, inventory.getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TilePrism(pos, state);
	}

	@Override
	public Behaviour getGhostBehaviour() {
		return Behaviour.RUN_RECEIVER_TRIGGER;
	}
}
