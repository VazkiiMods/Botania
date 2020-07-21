/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.Vector3;

import java.util.*;

public class LightningSegmentGenerator {
	private static final int BRANCH_FACTOR = 2;
	private final Random rand;
	private final Map<Integer, Integer> splitParents = new HashMap<>();
	private int segmentCount = 1;
	private int splitCount = 0;

	public LightningSegmentGenerator(long seed) {
		this.rand = new Random(seed);
	}

	public Pair<Integer, List<FXLightningSegment>> compute(Vector3 start, Vector3 end, double length) {
		List<FXLightningSegment> segmentsA = new ArrayList<>();
		segmentsA.add(new FXLightningSegment(start, end));

		List<FXLightningSegment> segmentsB = new ArrayList<>();

		// Alternate between two lists to save memory allocations
		fractal(segmentsA, segmentsB, length / 1.5, 0.7F, 0.7F, 45);
		fractal(segmentsB, segmentsA, length / 4, 0.5F, 0.8F, 50);
		fractal(segmentsA, segmentsB, length / 15, 0.5F, 0.9F, 55);
		fractal(segmentsB, segmentsA, length / 30, 0.5F, 1.0F, 60);
		fractal(segmentsA, segmentsB, length / 60, 0, 0, 0);
		fractal(segmentsB, segmentsA, length / 100, 0, 0, 0);
		fractal(segmentsA, segmentsB, length / 400, 0, 0, 0);

		calculateCollisionAndDiffs(segmentsB);
		segmentsB.sort((o1, o2) -> Float.compare(o2.light, o1.light));
		return Pair.of(segmentCount, segmentsB);
	}

	private void fractal(List<FXLightningSegment> oldSegments, List<FXLightningSegment> outputSegments, double amount, double splitChance, double splitLength, double splitAngle) {
		outputSegments.clear();
		FXLightningSegment prev;

		for (FXLightningSegment segment : oldSegments) {
			prev = segment.prev;

			Vector3 subsegment = segment.diff.multiply(1F / BRANCH_FACTOR);

			FXLightningBoltPoint[] newpoints = new FXLightningBoltPoint[BRANCH_FACTOR + 1];

			Vector3 startpoint = segment.startPoint.point;
			newpoints[0] = segment.startPoint;
			newpoints[BRANCH_FACTOR] = segment.endPoint;

			for (int i = 1; i < BRANCH_FACTOR; i++) {
				Vector3 basepoint = startpoint.add(subsegment.multiply(i));
				Vector3 randoff = segment.diff.perpendicular().normalize().rotate(rand.nextFloat() * 360, segment.diff)
						.multiply((rand.nextFloat() - 0.5F) * amount * 2);
				newpoints[i] = new FXLightningBoltPoint(basepoint, randoff);
			}

			for (int i = 0; i < BRANCH_FACTOR; i++) {
				FXLightningSegment next = new FXLightningSegment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentNo * BRANCH_FACTOR + i, segment.splitNo);
				next.prev = prev;
				if (prev != null) {
					prev.next = next;
				}

				if (i != 0 && rand.nextFloat() < splitChance) {
					Vector3 splitrot = next.diff.xCrossProduct().rotate(rand.nextFloat() * 360, next.diff);
					Vector3 diff = next.diff.rotate((rand.nextFloat() * 0.66F + 0.33F) * splitAngle, splitrot).multiply(splitLength);

					splitCount++;
					splitParents.put(splitCount, next.splitNo);

					FXLightningSegment split = new FXLightningSegment(newpoints[i], new FXLightningBoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.add(diff)), segment.light / 2F, next.segmentNo, splitCount);
					split.prev = prev;

					outputSegments.add(split);
				}

				prev = next;
				outputSegments.add(next);
			}

			if (segment.next != null) {
				segment.next.prev = prev;
			}
		}

		segmentCount *= BRANCH_FACTOR;
	}

	private void calculateCollisionAndDiffs(List<FXLightningSegment> segments) {
		Map<Integer, Integer> lastactivesegment = new HashMap<>();

		segments.sort((o1, o2) -> {
			int comp = Integer.compare(o1.splitNo, o2.splitNo);
			if (comp == 0) {
				return Integer.compare(o1.segmentNo, o2.segmentNo);
			} else {
				return comp;
			}
		});

		int lastSplitCalc = 0;
		int lastActiveSegment = 0;// unterminated
		float splitResistance = 0;

		for (FXLightningSegment segment : segments) {
			if (segment.splitNo > lastSplitCalc) {
				lastactivesegment.put(lastSplitCalc, lastActiveSegment);
				lastSplitCalc = segment.splitNo;
				lastActiveSegment = lastactivesegment.get(splitParents.get(segment.splitNo));
				splitResistance = lastActiveSegment < segment.segmentNo ? 50 : 0;
			}

			if (splitResistance >= 40 * segment.light) {
				continue;
			}

			splitResistance = rayTraceResistance(segment.startPoint.point, segment.endPoint.point, splitResistance);
			lastActiveSegment = segment.segmentNo;
		}
		lastactivesegment.put(lastSplitCalc, lastActiveSegment);

		lastSplitCalc = 0;
		lastActiveSegment = lastactivesegment.get(0);
		for (Iterator<FXLightningSegment> iterator = segments.iterator(); iterator.hasNext();) {
			FXLightningSegment segment = iterator.next();
			if (lastSplitCalc != segment.splitNo) {
				lastSplitCalc = segment.splitNo;
				lastActiveSegment = lastactivesegment.get(segment.splitNo);
			}

			if (segment.segmentNo > lastActiveSegment) {
				iterator.remove();
			}
			segment.calcEndDiffs();
		}
	}

	private float rayTraceResistance(Vector3 start, Vector3 end, float prevresistance) {
		World world = Minecraft.getInstance().world;
		RayTraceContext ctx = new RayTraceContext(start.toVector3d(), end.toVector3d(), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, null);
		BlockRayTraceResult ray = world.rayTraceBlocks(ctx);

		if (ray.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ray.getPos();
			BlockState state = world.getBlockState(pos);

			if (state.isAir()) {
				return prevresistance;
			}

			return prevresistance + state.getBlock().getExplosionResistance() + 0.3F;
		} else {
			return prevresistance;
		}
	}
}
