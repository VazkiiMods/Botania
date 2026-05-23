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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.TerraPlateState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import vazkii.botania.common.handler.BotaniaRecipeIngredientsCache;
import vazkii.botania.common.internal_caps.ItemSources;
import vazkii.botania.xplat.XplatAbstractions;

public class TerrestrialAgglomerationPlateBlock extends BotaniaWaterloggedBlock implements EntityBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 3, 16);

	public TerrestrialAgglomerationPlateBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.TERRA_PLATE_STATE, TerraPlateState.IDLE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.TERRA_PLATE_STATE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (BotaniaRecipeIngredientsCache.isTerraPlateInputItem(level, stack.getItem())) {
			if (!level.isClientSide()) {
				ItemStack target = stack.split(1);
				ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target);
				item.setThrower(player);
				item.setPickUpDelay(40);
				item.setDeltaMovement(Vec3.ZERO);
				level.addFreshEntity(item);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	/**
	 * When we aren't processing items yet, and an item used in a recipe enters the block space, schedule a check to see
	 * if processing should start. The check doesn't happen right away, as this is the middle of the entity tick phase,
	 * and we don't want to perform multiple checks per tick, if we can help it.
	 */
	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity item
				&& state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE).isLookingForIngredients()
				&& !level.getBlockTicks().hasScheduledTick(pos, state.getBlock())
				// while an output item might be used in a subsequent recipe, it's not allowed on its own right away
				&& !XplatAbstractions.instance().isItemSource(item, ItemSources.TERRA_PLATE)
				&& BotaniaRecipeIngredientsCache.isTerraPlateInputItem(level, item.getItem().getItem())) {
			level.scheduleTick(pos, state.getBlock(), 1);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE).isLookingForIngredients()) {
			level.getBlockEntity(pos, BotaniaBlockEntities.TERRA_PLATE)
					.ifPresent(TerrestrialAgglomerationPlateBlockEntity::tryStartProcessing);
		} else if (state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE) == TerraPlateState.DONE) {
			level.setBlock(pos,
					state.setValue(BotaniaStateProperties.TERRA_PLATE_STATE, TerraPlateState.IDLE),
					Block.UPDATE_ALL);
		}
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TerrestrialAgglomerationPlateBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return switch (state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE)) {
			case COLLECTING -> createTickerHelper(type, BotaniaBlockEntities.TERRA_PLATE, level.isClientSide()
					? TerrestrialAgglomerationPlateBlockEntity::clientCollectingTick
					: TerrestrialAgglomerationPlateBlockEntity::serverCollectingTick);
			case DISSIPATING -> level.isClientSide()
					? null
					: createTickerHelper(type, BotaniaBlockEntities.TERRA_PLATE,
							TerrestrialAgglomerationPlateBlockEntity::serverDissipatingTick);
			default -> null;
		};
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return switch (state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE)) {
			case IDLE -> 0;
			case DONE -> 15;
			default -> level.getBlockEntity(pos, BotaniaBlockEntities.TERRA_PLATE)
					.map(TerrestrialAgglomerationPlateBlockEntity::getComparatorLevel)
					.orElse(0);
		};
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(BotaniaStateProperties.TERRA_PLATE_STATE) == TerraPlateState.DISSIPATING) {
			RandomSource rng = level.getRandom();
			level.addParticle(
					WispParticleData.wisp(0.2f + 0.1f * rng.nextFloat(),
							rng.nextFloat() * 0.1f, 0.7f + rng.nextFloat() * 0.1f, 1 - rng.nextFloat() * 0.1f,
							1f + 0.5f * rng.nextFloat()),
					pos.getX() + rng.nextDouble(),
					pos.getY() + 0.2,
					pos.getZ() + rng.nextDouble(),
					rng.nextDouble() * 0.02 - 0.01,
					rng.nextDouble() * 0.01 + 0.01,
					rng.nextDouble() * 0.02 - 0.01);
		}
	}
}
