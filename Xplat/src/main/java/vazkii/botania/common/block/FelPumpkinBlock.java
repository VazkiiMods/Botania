/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.loot.BotaniaLootTables;
import vazkii.botania.mixin.MobAccessor;

public class FelPumpkinBlock extends BotaniaBlock {
	public static final MapCodec<FelPumpkinBlock> CODEC = simpleCodec(FelPumpkinBlock::new);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	@Nullable
	private BlockPattern felBlazeFull;
	@Nullable
	private BlockPattern felBlazeBase;

	public FelPumpkinBlock(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH));
	}

	@Override
	public MapCodec<? extends FelPumpkinBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (!oldState.is(state.getBlock())) {
			this.trySpawnBlaze(level, pos);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING,
				context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING,
				mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING,
				rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	// [VanillaCopy] all code relevant to snow golems from CarvedPumpkinBlock

	public boolean canSpawnBlaze(LevelReader level, BlockPos pos) {
		return this.getOrCreateFelBlazeBase().find(level, pos) != null;
	}

	private void trySpawnBlaze(Level level, BlockPos pos) {
		BlockPattern.BlockPatternMatch blockPatternMatch = this.getOrCreateFelBlazeFull().find(level, pos);
		if (blockPatternMatch != null) {
			Blaze blaze = EntityType.BLAZE.create(level);
			if (blaze != null) {
				blaze.setPersistenceRequired();
				((MobAccessor) blaze).botania_setLootTable(BotaniaLootTables.FEL_BLAZE);
				spawnBlazeInWorld(level, blockPatternMatch, blaze, blockPatternMatch.getBlock(0, 2, 0).getPos());
			}
		}
	}

	private static void spawnBlazeInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity blaze, BlockPos pos) {
		CarvedPumpkinBlock.clearPatternBlocks(level, patternMatch);
		blaze.moveTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.05, (double) pos.getZ() + 0.5, 0.0F, 0.0F);
		level.addFreshEntity(blaze);

		for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, blaze.getBoundingBox().inflate(5.0))) {
			CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, blaze);
		}

		CarvedPumpkinBlock.updatePatternBlocks(level, patternMatch);
	}

	private BlockPattern getOrCreateFelBlazeBase() {
		if (this.felBlazeBase == null) {
			this.felBlazeBase = BlockPatternBuilder.start()
					.aisle(" ", "#", "#")
					.where('#', BlockInWorld.hasState(state -> state.is(BotaniaTags.Blocks.FEL_BLAZE_BASE)))
					.build();
		}

		return this.felBlazeBase;
	}

	private BlockPattern getOrCreateFelBlazeFull() {
		if (this.felBlazeFull == null) {
			this.felBlazeFull = BlockPatternBuilder.start()
					.aisle("^", "#", "#")
					.where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(BotaniaBlocks.FEL_PUMPKIN)))
					.where('#', BlockInWorld.hasState(state -> state.is(BotaniaTags.Blocks.FEL_BLAZE_BASE)))
					.build();
		}

		return this.felBlazeFull;
	}

}
