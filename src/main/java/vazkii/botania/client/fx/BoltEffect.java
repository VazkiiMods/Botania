/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;
/*
 * This class is derived from the Mekanism project.
 * See ALTERNATE_LICENSES.txt.
 * Imported October 2021 and heavily modified.
 */

import com.mojang.math.Vector4f;

import net.minecraft.world.phys.Vec3;

import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class BoltEffect {

	private final Random random = new Random();

	private final BoltRenderInfo renderInfo;

	private final Vec3 start;
	private final Vec3 end;

	private final int segments;

	private int count = 1;
	private float size = 0.1F;

	private int lifespan = 30;

	private SpawnFunction spawnFunction = SpawnFunction.delay(60);
	private FadeFunction fadeFunction = FadeFunction.fade(0.5F);

	public BoltEffect(Vec3 start, Vec3 end) {
		this(BoltRenderInfo.DEFAULT, start, end);
	}

	public BoltEffect(BoltRenderInfo info, Vec3 start, Vec3 end) {
		this(info, start, end, (int) (Math.sqrt(start.distanceTo(end) * 100)));
	}

	public BoltEffect(BoltRenderInfo info, Vec3 start, Vec3 end, int segments) {
		this.renderInfo = info;
		this.start = start;
		this.end = end;
		this.segments = segments;
	}

	/**
	 * Set the amount of bolts to render for this single bolt instance.
	 *
	 * @param count amount of bolts to render
	 *
	 * @return this
	 */
	public BoltEffect count(int count) {
		this.count = count;
		return this;
	}

	/**
	 * Set the starting size (or width) of bolt segments.
	 *
	 * @param size starting size of bolt segments
	 *
	 * @return this
	 */
	public BoltEffect size(float size) {
		this.size = size;
		return this;
	}

	/**
	 * Define the {@link SpawnFunction} for this bolt effect.
	 *
	 * @param spawnFunction spawn function to use
	 *
	 * @return this
	 */
	public BoltEffect spawn(SpawnFunction spawnFunction) {
		this.spawnFunction = spawnFunction;
		return this;
	}

	/**
	 * Define the {@link FadeFunction} for this bolt effect.
	 *
	 * @param fadeFunction fade function to use
	 *
	 * @return this
	 */
	public BoltEffect fade(FadeFunction fadeFunction) {
		this.fadeFunction = fadeFunction;
		return this;
	}

	/**
	 * Define the lifespan (in ticks) of this bolt, at the end of which the bolt will expire.
	 *
	 * @param lifespan lifespan to use in ticks
	 *
	 * @return this
	 */
	public BoltEffect lifespan(int lifespan) {
		this.lifespan = lifespan;
		return this;
	}

	public int getLifespan() {
		return lifespan;
	}

	public SpawnFunction getSpawnFunction() {
		return spawnFunction;
	}

	public FadeFunction getFadeFunction() {
		return fadeFunction;
	}

	public Vector4f getColor() {
		return renderInfo.color;
	}

	public List<BoltQuads> generate() {
		List<BoltQuads> quads = new ArrayList<>();
		Vec3 diff = end.subtract(start);
		float totalDistance = (float) diff.length();
		for (int i = 0; i < count; i++) {
			Queue<BoltInstructions> drawQueue = new ArrayDeque<>();
			drawQueue.add(new BoltInstructions(start, 0, Vec3.ZERO, null, false));
			while (!drawQueue.isEmpty()) {
				BoltInstructions data = drawQueue.poll();
				Vec3 perpendicularDist = data.perpendicularDist();
				float progress = data.progress() + (1F / segments) * (1 - renderInfo.parallelNoise + random.nextFloat() * renderInfo.parallelNoise * 2);
				Vec3 segmentEnd;
				float segmentDiffScale = renderInfo.spreadFunction.getMaxSpread(progress);
				if (progress >= 1 && segmentDiffScale <= 0) {
					segmentEnd = end;
				} else {
					float maxDiff = renderInfo.spreadFactor * segmentDiffScale * totalDistance;
					Vec3 randVec = findRandomOrthogonalVector(diff, random);
					double rand = renderInfo.randomFunction.getRandom(random);
					perpendicularDist = renderInfo.segmentSpreader.getSegmentAdd(perpendicularDist, randVec, maxDiff, segmentDiffScale, progress, rand);
					// new vector is original + current progress through segments + perpendicular change
					segmentEnd = start.add(diff.scale(progress)).add(perpendicularDist);
				}
				float boltSize = size * (0.5F + (1 - progress) * 0.5F);
				Pair<BoltQuads, QuadCache> quadData = createQuads(data.cache(), data.start(), segmentEnd, boltSize);
				quads.add(quadData.getLeft());

				if (progress >= 1) {
					break; // break if we've reached the defined end point
				} else if (!data.isBranch()) {
					// continue the bolt if this is the primary (non-branch) segment
					drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), false));
				} else if (random.nextFloat() < renderInfo.branchContinuationFactor) {
					// branch continuation
					drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), true));
				}

				while (random.nextFloat() < renderInfo.branchInitiationFactor * (1 - progress)) {
					// branch initiation (probability decreases as progress increases)
					drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), true));
				}
			}
		}
		return quads;
	}

	private static Vec3 findRandomOrthogonalVector(Vec3 vec, Random rand) {
		Vec3 newVec = new Vec3(-0.5 + rand.nextDouble(), -0.5 + rand.nextDouble(), -0.5 + rand.nextDouble());
		return vec.cross(newVec).normalize();
	}

	private Pair<BoltQuads, QuadCache> createQuads(@Nullable QuadCache cache, Vec3 startPos, Vec3 end, float size) {
		Vec3 diff = end.subtract(startPos);
		Vec3 rightAdd = diff.cross(new Vec3(0.5, 0.5, 0.5)).normalize().scale(size);
		Vec3 backAdd = diff.cross(rightAdd).normalize().scale(size);
		Vec3 rightAddSplit = rightAdd.scale(0.5F);

		Vec3 start = cache != null ? cache.prevEnd() : startPos;
		Vec3 startRight = cache != null ? cache.prevEndRight() : start.add(rightAdd);
		Vec3 startBack = cache != null ? cache.prevEndBack() : start.add(rightAddSplit).add(backAdd);
		Vec3 endRight = end.add(rightAdd);
		Vec3 endBack = end.add(rightAddSplit).add(backAdd);

		BoltQuads quads = new BoltQuads();
		quads.addQuad(start, end, endRight, startRight);
		quads.addQuad(startRight, endRight, end, start);

		quads.addQuad(startRight, endRight, endBack, startBack);
		quads.addQuad(startBack, endBack, endRight, startRight);

		return Pair.of(quads, new QuadCache(end, endRight, endBack));
	}

	private record QuadCache(Vec3 prevEnd, Vec3 prevEndRight, Vec3 prevEndBack) {
	}

	private record BoltInstructions(Vec3 start, float progress,
			Vec3 perpendicularDist,
			@Nullable QuadCache cache, boolean isBranch) {
	}

	public static class BoltQuads {

		private final List<Vec3> vecs = new ArrayList<>();

		protected void addQuad(Vec3... quadVecs) {
			vecs.addAll(Arrays.asList(quadVecs));
		}

		public List<Vec3> getVecs() {
			return vecs;
		}
	}

	/**
	 * A SpreadFunction defines how far bolt segments can stray from the straight-line vector, based on parallel
	 * 'progress' from start to finish.
	 *
	 * @author aidancbrady
	 */
	public interface SpreadFunction {

		/** A steady linear increase in perpendicular noise. */
		SpreadFunction LINEAR_ASCENT = progress -> progress;
		/**
		 * A steady linear increase in perpendicular noise, followed by a steady decrease after the halfway point.
		 */
		SpreadFunction LINEAR_ASCENT_DESCENT = progress -> (progress - Math.max(0, 2 * progress - 1)) / 0.5F;
		/** Represents a unit sine wave from 0 to PI, scaled by progress. */
		SpreadFunction SINE = progress -> (float) Math.sin(Math.PI * progress);

		float getMaxSpread(float progress);
	}

	/**
	 * A RandomFunction defines the behavior of the RNG used in various bolt generation calculations.
	 *
	 * @author aidancbrady
	 */
	public interface RandomFunction {

		/** Uniform probability distribution. */
		RandomFunction UNIFORM = Random::nextFloat;
		/** Gaussian probability distribution. */
		RandomFunction GAUSSIAN = rand -> (float) rand.nextGaussian();

		float getRandom(Random rand);
	}

	/**
	 * A SegmentSpreader defines how successive bolt segments are arranged in the bolt generation calculation, based on
	 * previous state.
	 *
	 * @author aidancbrady
	 */
	public interface SegmentSpreader {

		/**
		 * Don't remember where the last segment left off, just randomly move from the straight-line vector.
		 */
		SegmentSpreader NO_MEMORY = (perpendicularDist, randVec, maxDiff, scale, progress, rand) -> randVec.scale(maxDiff * rand);

		/**
		 * Move from where the previous segment ended by a certain memory factor. Higher memory will restrict
		 * perpendicular movement.
		 */
		static SegmentSpreader memory(float memoryFactor) {
			return (perpendicularDist, randVec, maxDiff, spreadScale, progress, rand) -> {
				double nextDiff = maxDiff * (1 - memoryFactor) * rand;
				Vec3 cur = randVec.scale(nextDiff);
				perpendicularDist = perpendicularDist.add(cur);
				double length = perpendicularDist.length();
				if (length > maxDiff) {
					perpendicularDist = perpendicularDist.scale(maxDiff / length);
				}
				return perpendicularDist.add(cur);
			};
		}

		Vec3 getSegmentAdd(Vec3 perpendicularDist, Vec3 randVec, float maxDiff, float scale, float progress, double rand);
	}

	/**
	 * A bolt's spawn function defines its spawn behavior (handled by the renderer). A spawn function generates a lower
	 * and upper bound on a spawn delay (via
	 * getSpawnDelayBounds()), for which an intermediate value is chosen randomly from a uniform distribution
	 * (getSpawnDelay()). Spawn functions can also be defined as
	 * 'consecutive,' in which cases the Bolt Renderer will always begin rendering a new bolt instance when one expires.
	 *
	 * @author aidancbrady
	 */
	public interface SpawnFunction {

		/** Allow for bolts to be spawned each update call without any delay. */
		SpawnFunction NO_DELAY = rand -> Pair.of(0F, 0F);
		/** Will re-spawn a bolt each time one expires. */
		SpawnFunction CONSECUTIVE = new SpawnFunction() {
			@Override
			public Pair<Float, Float> getSpawnDelayBounds(Random rand) {
				return Pair.of(0F, 0F);
			}

			@Override
			public boolean isConsecutive() {
				return true;
			}
		};

		/** Spawn bolts with a specified constant delay. */
		static SpawnFunction delay(float delay) {
			return rand -> Pair.of(delay, delay);
		}

		/**
		 * Spawns bolts with a specified delay and specified noise value, which will be randomly applied at either end
		 * of the delay bounds.
		 */
		static SpawnFunction noise(float delay, float noise) {
			return rand -> Pair.of(delay - noise, delay + noise);
		}

		Pair<Float, Float> getSpawnDelayBounds(Random rand);

		default float getSpawnDelay(Random rand) {
			Pair<Float, Float> bounds = getSpawnDelayBounds(rand);
			return bounds.getLeft() + (bounds.getRight() - bounds.getLeft()) * rand.nextFloat();
		}

		default boolean isConsecutive() {
			return false;
		}
	}

	/**
	 * A bolt's fade function allows one to define lower and upper bounds on the bolt segments rendered based on
	 * lifespan. This allows for dynamic 'fade-in' and
	 * 'fade-out' effects.
	 *
	 * @author aidancbrady
	 */
	public interface FadeFunction {

		/** No fade; render the bolts entirely throughout their lifespan. */
		FadeFunction NONE = (totalBolts, lifeScale) -> Pair.of(0, totalBolts);

		/**
		 * Render bolts with a segment-by-segment 'fade' in and out, with a specified fade duration (applied to start
		 * and finish).
		 */
		static FadeFunction fade(float fade) {
			return (totalBolts, lifeScale) -> {
				int start = lifeScale > (1 - fade) ? (int) (totalBolts * (lifeScale - (1 - fade)) / fade) : 0;
				int end = lifeScale < fade ? (int) (totalBolts * (lifeScale / fade)) : totalBolts;
				return Pair.of(start, end);
			};
		}

		Pair<Integer, Integer> getRenderBounds(int totalBolts, float lifeScale);
	}

	public static class BoltRenderInfo {

		public static final BoltRenderInfo DEFAULT = defaultConfig();

		/** How much variance is allowed in segment lengths (parallel to straight line). */
		private final float parallelNoise;
		/**
		 * How much variance is allowed perpendicular to the straight line vector. Scaled by distance and spread
		 * function.
		 */
		private final float spreadFactor;

		/** The chance of creating an additional branch after a certain segment. */
		private final float branchInitiationFactor;
		/** The chance of a branch continuing (post-initiation). */
		private final float branchContinuationFactor;

		private final Vector4f color;

		private final RandomFunction randomFunction;
		private final SpreadFunction spreadFunction;
		private final SegmentSpreader segmentSpreader;

		private BoltRenderInfo(
				float parallelNoise,
				float spreadFactor,
				float branchInitiationFactor,
				float branchContinuationFactor,
				Vector4f color,
				RandomFunction randomFunction,
				SpreadFunction spreadFunction,
				SegmentSpreader segmentSpreader) {
			this.parallelNoise = parallelNoise;
			this.spreadFactor = spreadFactor;
			this.branchInitiationFactor = branchInitiationFactor;
			this.branchContinuationFactor = branchContinuationFactor;
			this.color = color;
			this.randomFunction = randomFunction;
			this.spreadFunction = spreadFunction;
			this.segmentSpreader = segmentSpreader;
		}

		private static BoltRenderInfo defaultConfig() {
			return new BoltRenderInfo(
					0.1F, 0.1F, 0.0F, 0.0F,
					new Vector4f(0.0F, 0.7764F, 1F, 0.8F),
					RandomFunction.GAUSSIAN,
					SpreadFunction.SINE,
					SegmentSpreader.NO_MEMORY
			);
		}

		public BoltRenderInfo noise(float parallelNoise, float spreadFactor) {
			return new BoltRenderInfo(
					parallelNoise,
					spreadFactor,
					this.branchInitiationFactor,
					this.branchContinuationFactor,
					this.color,
					this.randomFunction,
					this.spreadFunction,
					this.segmentSpreader
			);
		}

		public BoltRenderInfo branching(float branchInitiationFactor, float branchContinuationFactor) {
			return new BoltRenderInfo(
					this.parallelNoise,
					this.spreadFactor,
					branchInitiationFactor,
					branchContinuationFactor,
					this.color,
					this.randomFunction,
					this.spreadFunction,
					this.segmentSpreader
			);
		}

		public BoltRenderInfo spreader(SegmentSpreader segmentSpreader) {
			return new BoltRenderInfo(
					this.parallelNoise,
					this.spreadFactor,
					this.branchInitiationFactor,
					this.branchContinuationFactor,
					this.color,
					this.randomFunction,
					this.spreadFunction,
					segmentSpreader
			);
		}

		public BoltRenderInfo randomFunction(RandomFunction randomFunction) {
			return new BoltRenderInfo(
					this.parallelNoise,
					this.spreadFactor,
					this.branchInitiationFactor,
					this.branchContinuationFactor,
					this.color,
					randomFunction,
					this.spreadFunction,
					this.segmentSpreader
			);
		}

		public BoltRenderInfo spreadFunction(SpreadFunction spreadFunction) {
			return new BoltRenderInfo(
					this.parallelNoise,
					this.spreadFactor,
					this.branchInitiationFactor,
					this.branchContinuationFactor,
					this.color,
					this.randomFunction,
					spreadFunction,
					this.segmentSpreader
			);
		}

		public BoltRenderInfo color(Vector4f color) {
			return new BoltRenderInfo(
					this.parallelNoise,
					this.spreadFactor,
					this.branchInitiationFactor,
					this.branchContinuationFactor,
					color,
					this.randomFunction,
					this.spreadFunction,
					this.segmentSpreader
			);
		}
	}
}
