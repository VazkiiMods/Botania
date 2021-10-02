/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.LivingEntity;

import net.minecraft.tileentity.TileEntityType;
import vazkii.botania.common.block.ModSubtiles;

public class SubTileJiyuulia extends SubTileTangleberrie {
	private static final int RANGE = 8;
	private static final int RANGE_MINI = 2;

	protected SubTileJiyuulia(TileEntityType<?> type) {
		super(type);
	}

	public SubTileJiyuulia() {
		this(ModSubtiles.JIYUULIA);
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
		public Mini() {
			super(ModSubtiles.JIYUULIA_CHIBI);
		}

		@Override
		public double getRange() {
			return RANGE_MINI;
		}
	}
}
