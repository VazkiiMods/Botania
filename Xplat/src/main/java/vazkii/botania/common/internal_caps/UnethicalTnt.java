/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;

import vazkii.botania.api.attachment.DataMarkerId;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Helps track TNT entity spawning to check for unethical methods. Used with vanilla piston logic (via mixin) and
 * Force lens block moving (which is used by mana bursts with the lens itself, but also by the Force Relay block).
 * <br>
 * The concept of this check is to keep track of any TNT entity that was spawned as a result of a block update while a
 * block structure is converted into moving blocks. Moving blocks are placed in the destination position, so we look for
 * adjacent moving block entities that represent a TNT block and move away from the TNT entity. If we find such a moving
 * block, the TNT entity was unethically sourced.
 *
 * @see vazkii.botania.common.block.block_entity.flower.generating.EntropinnyumBlockEntity
 * @see vazkii.botania.mixin.PistonBaseBlockMixin
 * @see vazkii.botania.common.item.lens.ForceLens#moveBlocks(Level, BlockPos, Direction, BlockPos)
 */
public class UnethicalTnt {
	public static final ResourceLocation ID = botaniaRL("unethical_tnt");
	public static final DataMarkerId MARKER = new DataMarkerId(ID);

	/**
	 * In case the various loaded dimensions' tick loops are run in their own threads, tracking data is kept
	 * thread-local.
	 */
	private static final ThreadLocal<UnethicalTnt> tracker = ThreadLocal.withInitial(UnethicalTnt::new);

	/**
	 * While the moveBlocks method should not be reentrant, we can't know what crazy ideas some mixin authors might
	 * have. To be safe, we account for reentrancy and hope that we don't miss any method exits. We also assume nobody
	 * is crazy enough to spread the blocks-start-to-move logic across multiple threads.
	 */
	private final AtomicInteger trackTntEntities = new AtomicInteger();

	/**
	 * To reduce the risk of memory leaks, TNT entities are stored using their ID and a weak reference to their Level.
	 */
	private final WeakHashMap<Level, IntOpenHashSet> trackedTntEntities = new WeakHashMap<>();

	/**
	 * A force relay, force lens mana burst or vanilla piston is about to start moving blocks.
	 */
	public static void startTrackingTntEntities() {
		tracker.get().startTracking();
	}

	/**
	 * A TNT entity just spawned. Check it for potentially unethical spawning methods.
	 *
	 * @param entity The TNT entity.
	 */
	public static void addTrackedTntEntity(PrimedTnt entity) {
		tracker.get().addTrackedEntity(entity);
	}

	/**
	 * A force relay, force lens mana burst,or vanilla piston finished converting all blocks into moving block
	 * entities.
	 */
	public static void endTrackingTntEntitiesAndCheck() {
		tracker.get().endTracking();
	}

	private void startTracking() {
		trackTntEntities.incrementAndGet();
	}

	private void addTrackedEntity(PrimedTnt entity) {
		if (trackTntEntities.get() > 0) {
			trackedTntEntities.computeIfAbsent(entity.level(), lvl -> new IntOpenHashSet()).add(entity.getId());
		}
	}

	private void endTracking() {
		if (trackTntEntities.decrementAndGet() == 0) {
			for (final var entry : trackedTntEntities.entrySet()) {
				final var level = entry.getKey();
				final var trackedEntities = entry.getValue();
				for (final var tntId : trackedEntities) {
					final var entity = level.getEntity(tntId);
					if (entity instanceof PrimedTnt tnt) {
						checkUnethical(tnt);
					}
				}
				trackedEntities.clear();
			}
		}
	}

	private static void checkUnethical(PrimedTnt entity) {
		BlockPos center = entity.blockPosition();
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		for (final var dir : Direction.values()) {
			blockPos.setWithOffset(center, dir);
			final var blockState = entity.level().getBlockState(blockPos);
			if (blockState.is(Blocks.MOVING_PISTON)
					&& entity.level().getBlockEntity(blockPos) instanceof PistonMovingBlockEntity movingBlockEntity
					&& movingBlockEntity.getMovementDirection() == dir
					&& (movingBlockEntity.getMovedState().getBlock() instanceof TntBlock
							|| movingBlockEntity.getMovedState().is(BotaniaTags.Blocks.UNETHICAL_TNT_CHECK))) {
				// found a moving block that marks the destination of a TNT block moving away from the TNT entity
				MARKER.addFor(entity);
				break;
			}
		}
	}
}
