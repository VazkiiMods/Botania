/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

import java.util.List;
import java.util.Map;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			moveBlocks(entity.level, rtr.getBlockPos().relative(rtr.getDirection()), rtr.getDirection().getOpposite());
		}

		return shouldKill;
	}

	public static BlockState unWaterlog(BlockState state) {
		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			return state.setValue(BlockStateProperties.WATERLOGGED, false);
		} else {
			return state;
		}
	}

	// [VanillaCopy] Based on PistonBaseBlock
	public static void moveBlocks(Level level, BlockPos pistonPos, Direction direction) {
		PistonStructureResolver pistonStructureResolver = new PistonStructureResolver(level, pistonPos, direction, true);
		if (pistonStructureResolver.resolve()) {
			Map<BlockPos, BlockState> map = Maps.newHashMap();
			List<BlockPos> positionsToPush = pistonStructureResolver.getToPush();
			List<BlockState> statesToPush = Lists.newArrayList();

			for (BlockPos posToPush : positionsToPush) {
				BlockState state = level.getBlockState(posToPush);
				statesToPush.add(state);
				map.put(posToPush, state);
			}

			List<BlockPos> positionsToDestroy = pistonStructureResolver.getToDestroy();
			BlockState[] affectedStates = new BlockState[positionsToPush.size() + positionsToDestroy.size()];
			int affectedStatesPtr = 0;

			for (int k = positionsToDestroy.size() - 1; k >= 0; --k) {
				BlockPos posToDestroy = positionsToDestroy.get(k);
				BlockState state = level.getBlockState(posToDestroy);
				BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(posToDestroy) : null;
				Block.dropResources(state, level, posToDestroy, blockEntity);
				level.setBlock(posToDestroy, Blocks.AIR.defaultBlockState(), 18);
				if (!state.is(BlockTags.FIRE)) {
					level.addDestroyBlockEffect(posToDestroy, state);
				}

				affectedStates[affectedStatesPtr++] = state;
			}

			for (int l = positionsToPush.size() - 1; l >= 0; --l) {
				BlockPos pos = positionsToPush.get(l);
				BlockState state = level.getBlockState(pos);
				pos = pos.relative(direction);
				map.remove(pos);
				BlockState movingPiston = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, direction);
				level.setBlock(pos, movingPiston, 68);
				level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pos, movingPiston, statesToPush.get(l), direction, true, false));
				affectedStates[affectedStatesPtr++] = state;
			}

			BlockState air = Blocks.AIR.defaultBlockState();

			for (BlockPos blockPos6 : map.keySet()) {
				level.setBlock(blockPos6, air, 82);
			}

			for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
				BlockPos blockPos7 = entry.getKey();
				BlockState blockState8 = entry.getValue();
				blockState8.updateIndirectNeighbourShapes(level, blockPos7, 2);
				air.updateNeighbourShapes(level, blockPos7, 2);
				air.updateIndirectNeighbourShapes(level, blockPos7, 2);
			}

			affectedStatesPtr = 0;

			for (int m = positionsToDestroy.size() - 1; m >= 0; --m) {
				BlockState state = affectedStates[affectedStatesPtr++];
				BlockPos posToDestroy = positionsToDestroy.get(m);
				state.updateIndirectNeighbourShapes(level, posToDestroy, 2);
				level.updateNeighborsAt(posToDestroy, state.getBlock());
			}

			for (int n = positionsToPush.size() - 1; n >= 0; --n) {
				level.updateNeighborsAt(positionsToPush.get(n), affectedStates[affectedStatesPtr++].getBlock());
			}

			if (!level.isClientSide) {
				level.playSound(null, pistonPos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.6F);
			}
		}
	}

}
