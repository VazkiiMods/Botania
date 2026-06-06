/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

public class SpacialBitSetTest {
	public static BlockPos initPos = new BlockPos(1, 2, 3);
	public static SpatialBitSet initSet = new SpatialBitSet(initPos);

	@Test
	void setCubeAround_EntirelyWithinRange_ExactlyCubePositionsAreSet() {
		BlockPos center = new BlockPos(123, 456, 789);
		var spacialBitSet = new SpatialBitSet(center);

		int range = 3;
		BlockPos setCubeCenter = center.offset(5, 6, 7);
		spacialBitSet.setCubeAround(setCubeCenter, range);

		assertResult(center, spacialBitSet,
				testPos -> Math.abs(testPos.getX() - setCubeCenter.getX()) <= range
						&& Math.abs(testPos.getY() - setCubeCenter.getY()) <= range
						&& Math.abs(testPos.getZ() - setCubeCenter.getZ()) <= range);
	}

	@Test
	void setCubeAround_LargerEncompassingCube_AllPositionsInRangeAreSet() {
		BlockPos center = new BlockPos(123, 456, 789);
		var spacialBitSet = new SpatialBitSet(center);

		spacialBitSet.setCubeAround(center, SpatialBitSet.RANGE + 2);

		assertResult(center, spacialBitSet,
				testPos -> Math.abs(testPos.getX() - center.getX()) <= SpatialBitSet.RANGE
						&& Math.abs(testPos.getY() - center.getY()) <= SpatialBitSet.RANGE
						&& Math.abs(testPos.getZ() - center.getZ()) <= SpatialBitSet.RANGE);
	}

	@Test
	void setCubeAround_CubeEntirelyOutsideRange_NoPosIsSet() {
		BlockPos center = new BlockPos(123, 456, 789);
		var spacialBitSet = new SpatialBitSet(center);

		BlockPos cubeCenter = center.offset(50, 60, 70);
		spacialBitSet.setCubeAround(cubeCenter, 5);

		assertResult(center, spacialBitSet, testPos -> false);
	}

	@Test
	void setPos_PosWithinRange_ExactlyPosIsSet() {
		BlockPos center = new BlockPos(123, 456, 789);
		var spacialBitSet = new SpatialBitSet(center);

		BlockPos pos = center.offset(5, 6, 7);
		spacialBitSet.setPos(pos);

		assertResult(center, spacialBitSet, pos::equals);
	}

	@Test
	void setPos_PosOutsideRange_NoPosIsSet() {
		BlockPos center = new BlockPos(123, 456, 789);
		var spacialBitSet = new SpatialBitSet(center);

		BlockPos pos = center.offset(50, 60, 70);
		spacialBitSet.setPos(pos);

		assertResult(center, spacialBitSet, testPos -> false);
	}

	private static void assertResult(BlockPos centerPos, SpatialBitSet spacialBitSet,
			Predicate<BlockPos> expectedInSetPredicate) {
		BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
		for (int x = -SpatialBitSet.RANGE - 1; x <= SpatialBitSet.RANGE + 1; x++) {
			for (int y = -SpatialBitSet.RANGE - 1; y <= SpatialBitSet.RANGE + 1; y++) {
				for (int z = -SpatialBitSet.RANGE - 1; z <= SpatialBitSet.RANGE + 1; z++) {
					testPos.setWithOffset(centerPos, x, y, z);
					boolean result = spacialBitSet.isSet(testPos);
					if (expectedInSetPredicate.test(testPos)) {
						Assertions.assertTrue(result, () -> "expected in set at " + testPos);
					} else {
						Assertions.assertFalse(result, () -> "expected not in set at " + testPos);
					}
				}
			}
		}
	}
}
