/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.helper;

import it.unimi.dsi.fastutil.shorts.Short2LongOpenHashMap;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;

/**
 * This helper class marks block positions within a maximum "chessboard range" of 31 blocks from a center position.
 * It is a special-case helper class for the Terra Truncator's block breaking logic, optimized for block arrangements
 * with a primarily vertical orientation.
 */
public class SpatialBitSet {
	private static final int NUM_COMPONENT_BITS = 6;
	public static final int RANGE = (1 << (NUM_COMPONENT_BITS - 1)) - 1;
	private static final int COMPONENT_MASK = (1 << 6) - 1;

	private final BlockPos center;
	private final Short2LongOpenHashMap map = Util.make(new Short2LongOpenHashMap(), m -> m.defaultReturnValue(0));

	public SpatialBitSet(BlockPos center) {
		this.center = center;
	}

	public BlockPos getCenter() {
		return center;
	}

	public void setPos(BlockPos pos) {
		int xOffset = pos.getX() - center.getX();
		int yOffset = pos.getY() - center.getY();
		int zOffset = pos.getZ() - center.getZ();

		if (Math.abs(xOffset) > RANGE || Math.abs(yOffset) > RANGE || Math.abs(zOffset) > RANGE) {
			return;
		}

		short encodedXZ = encodeXZ(xOffset, zOffset);
		int yShift = calcYShift(yOffset);

		map.put(encodedXZ, map.get(encodedXZ) | 1L << yShift);
	}

	public void setCubeAround(BlockPos pos, int range) {
		int cubeOffsetX = pos.getX() - center.getX();
		int cubeOffsetY = pos.getY() - center.getY();
		int cubeOffsetZ = pos.getZ() - center.getZ();

		int minOffsetX = Math.max(-RANGE, cubeOffsetX - range);
		int maxOffsetX = Math.min(RANGE, cubeOffsetX + range);
		int minOffsetY = Math.max(-RANGE, cubeOffsetY - range);
		int maxOffsetY = Math.min(RANGE, cubeOffsetY + range);
		int minOffsetZ = Math.max(-RANGE, cubeOffsetZ - range);
		int maxOffsetZ = Math.min(RANGE, cubeOffsetZ + range);

		for (int xOffset = minOffsetX; xOffset <= maxOffsetX; xOffset++) {
			for (int zOffset = minOffsetZ; zOffset <= maxOffsetZ; zOffset++) {
				short encodedXZ = encodeXZ(xOffset, zOffset);
				int minYShift = calcYShift(minOffsetY);
				int maxYShift = calcYShift(maxOffsetY);
				long yBits = (1L << maxYShift + 1) - 1 & -(1L << minYShift);
				map.put(encodedXZ, map.get(encodedXZ) | yBits);
			}
		}
	}

	public boolean isSet(BlockPos pos) {
		int xOffset = pos.getX() - center.getX();
		int yOffset = pos.getY() - center.getY();
		int zOffset = pos.getZ() - center.getZ();
		return Math.abs(xOffset) <= RANGE && Math.abs(yOffset) <= RANGE && Math.abs(zOffset) <= RANGE
				&& isSet(xOffset, yOffset, zOffset);
	}

	private static short encodeXZ(int xOffset, int zOffset) {
		return (short) (xOffset & COMPONENT_MASK | (zOffset & COMPONENT_MASK) << NUM_COMPONENT_BITS);
	}

	private static int calcYShift(int yOffset) {
		return yOffset + RANGE;
	}

	private boolean isSet(int xOffset, int yOffset, int zOffset) {
		return (map.get(encodeXZ(xOffset, zOffset)) & 1L << calcYShift(yOffset)) != 0;
	}
}
