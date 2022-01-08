/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.ModSubtiles;

public class SubTileJiyuulia extends SubTileTangleberrie {
	private static final double RANGE = 8;
	private static final double RANGE_MINI = 3;

	public SubTileJiyuulia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileJiyuulia(BlockPos pos, BlockState state) {
		this(ModSubtiles.JIYUULIA, pos, state);
	}

	@Override
	double getMaxDistance() {
		return 0;
	}

	@Override
	double getRange() {
		return RANGE;
	}

	@Override
	float getMotionVelocity(LivingEntity entity) {
		return -super.getMotionVelocity(entity) * 2;
	}

	@Override
	public int getColor() {
		return 0xBD9ACA;
	}

	public static class Mini extends SubTileJiyuulia {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.JIYUULIA_CHIBI, pos, state);
		}

		@Override
		public double getRange() {
			return RANGE_MINI;
		}
	}
}
