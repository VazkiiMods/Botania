/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public interface TallFlowerGrower extends BonemealableBlock {

	@Nullable
	Block getTallFlower();

	@Override
	default boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.above()).isAir()
				&& getTallFlower() instanceof DoublePlantBlock;
	}

	@Override
	default boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
		return isValidBonemealTarget(world, pos, state);
	}

	@Override
	default void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
		if (getTallFlower() instanceof DoublePlantBlock block) {
			DoublePlantBlock.placeAt(world, block.defaultBlockState(), pos, 3);
		}
	}
}
