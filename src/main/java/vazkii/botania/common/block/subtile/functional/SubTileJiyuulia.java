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

import vazkii.botania.common.block.ModSubtiles;

public class SubTileJiyuulia extends SubTileTangleberrie {
	public SubTileJiyuulia() {
		super(ModSubtiles.JIYUULIA);
	}

	@Override
	double getMaxDistance() {
		return 0;
	}

	@Override
	double getRange() {
		return 8;
	}

	@Override
	float getMotionVelocity(LivingEntity entity) {
		return -super.getMotionVelocity(entity) * 2;
	}

	@Override
	public int getColor() {
		return 0xBD9ACA;
	}

}
