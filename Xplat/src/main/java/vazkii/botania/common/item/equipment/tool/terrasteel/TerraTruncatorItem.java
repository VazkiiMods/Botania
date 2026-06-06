/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.SequentialBreaker;
import vazkii.botania.api.item.SpecialBlockBreakingHandler;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.SpatialBitSet;
import vazkii.botania.common.item.StoneOfTemperanceItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelAxeItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

public class TerraTruncatorItem extends ManasteelAxeItem implements SequentialBreaker, SpecialBlockBreakingHandler {

	/**
	 * The number of blocks per tick which the Terra Truncator will
	 * collect.
	 */
	private static final int BLOCK_SWAP_RATE = 10;

	/**
	 * The maximum radius (in blocks) which the Terra Truncator will go
	 * in order to try and murder/cut down the tree.
	 */
	public static final int BLOCK_RANGE = 32;

	/**
	 * The maximum number of leaf blocks which the Terra Truncator will chew/go
	 * through once a leaf block is encountered.
	 */
	private static final int LEAF_BLOCK_RANGE = 3;

	/**
	 * The amount of mana required to restore 1 point of damage.
	 */
	private static final int MANA_PER_DAMAGE = 100;

	/**
	 * Represents a map of dimension IDs to a set of all block swappers
	 * active in that dimension.
	 */
	private static final Map<ResourceKey<Level>, Set<BlockSwapper>> blockSwappers = new HashMap<>();

	/**
	 * Toggled during the block swapper ticking to prevent adding more of them during the map iteration.
	 */
	private static boolean tickingSwappers = false;

	public TerraTruncatorItem(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props.attributes(TerraTruncatorItem.createAttributes(BotaniaAPI.instance().getTerrasteelItemTier(), 5.0F, -3.0F)));
	}

	public static boolean shouldBreak(Player player) {
		return !player.isShiftKeyDown() && !StoneOfTemperanceItem.hasTemperanceActive(player);
	}

	@Override
	public void onBlockStartBreak(ServerLevel level, ItemStack stack, BlockPos pos, Player player) {
		BlockHitResult raycast = ToolCommons.raytraceFromEntity(player, 10, false);
		if (raycast.getType() == HitResult.Type.BLOCK) {
			Direction face = raycast.getDirection();
			breakOtherBlock(player, stack, pos, pos, face);
			if (player.isSecondaryUseActive()) {
				BotaniaAPI.instance().breakOnAllCursors(player, stack, pos, face);
			}
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
		if (shouldBreak(player) && !tickingSwappers) {
			addBlockSwapper(player.level(), player, stack, pos);
		}
	}

	public static void onTickEnd(ServerLevel world) {
		ResourceKey<Level> dim = world.dimension();
		if (blockSwappers.containsKey(dim)) {
			tickingSwappers = true;
			Set<BlockSwapper> swappers = blockSwappers.get(dim);

			// Iterate through all of our swappers, removing any
			// which no longer need to tick.
			swappers.removeIf(next -> !next.tick());
			tickingSwappers = false;
		}
	}

	/**
	 * Adds a new block swapper to the provided world as the provided player.
	 * Block swappers are only added on the server, and a marker instance
	 * which is not actually ticked but contains the proper passed in
	 * information will be returned to the client.
	 * 
	 * @param world      The world to add the swapper to.
	 * @param player     The player who is responsible for this swapper.
	 * @param stack      The Terra Truncator which caused this block swapper.
	 * @param origCoords The original coordinates the swapper should start at.
	 */
	private static void addBlockSwapper(Level world, Player player, ItemStack stack, BlockPos origCoords) {
		// Block swapper registration should only occur on the server
		if (world.isClientSide) {
			return;
		}

		BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords,
				// only cut down entire trees when starting with the trunk, but still support multi-break via Loki ring
				world.getBlockState(origCoords).is(BotaniaTags.Blocks.TERRA_TRUNCATOR_TRUNK_BLOCKS)
						? TerraTruncatorItem.BLOCK_RANGE
						: 1);

		ResourceKey<Level> dim = world.dimension();
		blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);
	}

	/**
	 * A block swapper for the Terra Truncator, which (mostly) uses a standard Breadth First Search to try and
	 * murder/cut down trees. The Terra Truncator will look up to {@link #BLOCK_RANGE} blocks to find trunk blocks to
	 * cut down (only cutting down diagonally adjacent blocks, so it doesn't jump through the air).
	 * The crown of trees receives special handling to ensure only leaves (or corresponding blocks for other large
	 * tree-like plants) are broken that likely belong to the same tree. For actual leaves, a depth-first search is
	 * performed to collect all leaves "supported" by the log currently being broken. (As in, the distance property of
	 * those leaves would likely increase by breaking the log. This is necessary to properly cut down the crown of e.g.
	 * large jungle trees.) For other types of crown blocks the breadth-first search includes any crown blocks that are
	 * within {@link #LEAF_BLOCK_RANGE} of any broken trunk block, as long as they can be reached within the standard
	 * {@link #BLOCK_RANGE}. (This is necessary to properly cut down huge nether fungi.)
	 */
	private static class BlockSwapper {

		private final Level world;
		private final Player player;
		private final ItemStack truncator;
		/**
		 * Set of any block positions with trunk blocks close enough to consider breaking crown blocks.
		 */
		private final SpatialBitSet validCrownOffsets;
		/**
		 * Positions we already considered (and either accepted or rejected) for block breaking.
		 * Values are stored as longs for compactness.
		 */
		private final LongSet consideredOffsets = new LongOpenHashSet();
		/**
		 * The priority queue of all possible candidates for swapping.
		 */
		private final PriorityQueue<SwapCandidate> candidateQueue = new PriorityQueue<>();

		/**
		 * Creates a new block swapper with the provided parameters. At this point the initial block is not removed yet.
		 * 
		 * @param world      The world the swapper is in.
		 * @param player     The player responsible for creating this swapper.
		 * @param truncator  The Terra Truncator responsible for creating this swapper.
		 * @param origCoords The original coordinates this swapper should start at.
		 * @param range      The range this swapper should swap in.
		 */
		public BlockSwapper(Level world, Player player, ItemStack truncator, BlockPos origCoords, int range) {
			this.world = world;
			this.player = player;
			this.truncator = truncator;
			this.validCrownOffsets = new SpatialBitSet(origCoords);

			// Add the origin to our candidate queue with the original range
			addToQueue(origCoords, range);
			if (world.getBlockState(origCoords).is(BotaniaTags.Blocks.TERRA_TRUNCATOR_TRUNK_BLOCKS)) {
				// ensure we can break any leaves around this log if it's the only log around
				validCrownOffsets.setCubeAround(origCoords, LEAF_BLOCK_RANGE);
			}
			// Note: Leaves only update their distance from the nearest log in the tick after breaking the log.
		}

		/**
		 * Iterate over the queue of blocks to break. For each block broken this way, consider its (diagonal) neighbors
		 * as additional blocks to break. Only up to {@link #BLOCK_SWAP_RATE} blocks are broken each tick.
		 * 
		 * @return {@code true} if there are more blocks to break in the queue, {@code false} otherwise.
		 */
		public boolean tick() {
			for (int remainingSwaps = BLOCK_SWAP_RATE; remainingSwaps > 0; remainingSwaps--) {
				if (candidateQueue.isEmpty()) {
					// looks like we're done already
					return false;
				}
				SwapCandidate cand = candidateQueue.poll();

				// Note: the initially broken block will already be gone at this point
				BlockState stateToBreak = world.getBlockState(cand.coordinates);

				// Otherwise, perform the break and then look at the adjacent tiles.
				ToolCommons.removeBlockWithDrops(player, truncator, world, cand.coordinates,
						state -> state.is(BotaniaTags.Blocks.TERRA_TRUNCATOR_TRUNK_BLOCKS)
								|| state.is(BotaniaTags.Blocks.TERRA_TRUNCATOR_CROWN_BLOCKS)
				);

				considerAdjacentBlocks(cand.coordinates, cand.range - 1,
						// since the initial block may be gone, we can't reliably check for a trunk block here
						!stateToBreak.is(BotaniaTags.Blocks.TERRA_TRUNCATOR_CROWN_BLOCKS));
			}

			return !candidateQueue.isEmpty();
		}

		/**
		 * Check the adjacent blocks, including diagonal ones, to propagate the block breaking into.
		 * If leaves are found this way, recursively search for all leaves that appear to be "supported" from here.
		 * If searching from a crown block, we ignore any trunk blocks found this way, as they probably belong to a
		 * different tree. If they do belong to the same tree, they will be found through other blocks eventually.
		 */
		private void considerAdjacentBlocks(BlockPos pos, int rangeRemaining, boolean considerTrunkBlocks) {
			if (rangeRemaining <= 0) {
				// can't reach anything else from here
				return;
			}

			for (BlockPos adjacentPos : MathHelper.aroundPosClosed(pos, 1)) {
				long adjacentPosLong = adjacentPos.asLong();
				if (consideredOffsets.contains(adjacentPosLong)) {
					// already considered before
					continue;
				}

				BlockState consideredState = world.getBlockState(adjacentPos);
				boolean adjacentTrunk = consideredState.is(BotaniaTags.Blocks.TERRA_TRUNCATOR_TRUNK_BLOCKS);
				boolean adjacentCrown = consideredState.is(BotaniaTags.Blocks.TERRA_TRUNCATOR_CROWN_BLOCKS);
				if (considerTrunkBlocks && adjacentTrunk) {
					// expand crown block search area, in case of valid non-leaf blocks
					validCrownOffsets.setCubeAround(adjacentPos, LEAF_BLOCK_RANGE);
				} else if (!adjacentCrown || !validCrownOffsets.isSet(adjacentPos)) {
					// not going to propagate block breaking to here (yet?)
					if (!adjacentCrown && !adjacentTrunk) {
						// not interested in this block at all
						consideredOffsets.add(adjacentPosLong);
					}
					continue;
				}
				addToQueue(adjacentPos, rangeRemaining);

				if (consideredState.is(BlockTags.LEAVES) && consideredState.hasProperty(LeavesBlock.DISTANCE)) {
					// visit adjacent leaves, only traversing to leaves that have a higher distance
					considerAdjacentLeaves(adjacentPos, consideredState.getValue(LeavesBlock.DISTANCE), LeavesBlock.DECAY_DISTANCE);
				}
			}
		}

		/**
		 * Depth-first recursive search over all leaves blocks with a distance that indicates they are "supported" by
		 * the block we started the search from. We search over all of them immediately since this happens before they
		 * update their distance after their "supporting" log was removed.
		 */
		private void considerAdjacentLeaves(BlockPos pos, int startDist, int remainingRange) {
			if (startDist == LeavesBlock.DECAY_DISTANCE || remainingRange <= 0) {
				// no more leaves to reach from here
				return;
			}
			int expectedDist = startDist + 1;
			BlockPos.MutableBlockPos adjacentPos = new BlockPos.MutableBlockPos();
			for (Direction dir : Direction.values()) {
				adjacentPos.setWithOffset(pos, dir);
				if (consideredOffsets.contains(adjacentPos.asLong())) {
					// we already looked at this position before
					continue;
				}
				BlockState state = world.getBlockState(adjacentPos);
				if (!state.is(BlockTags.LEAVES) || !state.hasProperty(LeavesBlock.DISTANCE)) {
					continue;
				}
				int actualDist = state.getValue(LeavesBlock.DISTANCE);
				if (expectedDist != actualDist) {
					// this leaves block is not "supported" by the one we started from
					continue;
				}
				addToQueue(adjacentPos, remainingRange);
				if (startDist < LeavesBlock.DECAY_DISTANCE) {
					considerAdjacentLeaves(adjacentPos, expectedDist, remainingRange - 1);
				}
			}
		}

		private void addToQueue(BlockPos adjacentPos, int adjacentTrunk) {
			candidateQueue.offer(new SwapCandidate(adjacentPos.immutable(), adjacentTrunk,
					(byte) world.getRandom().nextInt(Byte.MAX_VALUE))
			);
			consideredOffsets.add(adjacentPos.asLong());
		}

		/**
		 * Represents a potential candidate for swapping/removal. Sorted by
		 * range (where a larger range is more preferable). As we're using
		 * a priority queue, which is a min-heap internally, larger ranges
		 * are considered "smaller" than smaller ranges (so they show up in the
		 * min-heap first).
		 *
		 * @param coordinates The coordinates of this candidate.
		 * @param range       The remaining range of this candidate.
		 */
		public record SwapCandidate(BlockPos coordinates, int range, byte randomness) implements Comparable<SwapCandidate> {
			@Override
			public int compareTo(SwapCandidate other) {
				// Aka, a bigger range implies a smaller value, meaning
				// bigger ranges will be preferred in a min-heap
				int diff = other.range - this.range;
				if (diff == 0) {
					return other.randomness - this.randomness;
				}
				return diff;
			}
		}
	}

}
