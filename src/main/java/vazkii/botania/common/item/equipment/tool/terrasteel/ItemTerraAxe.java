/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2015, 6:55:34 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class ItemTerraAxe extends ItemManasteelAxe implements ISequentialBreaker {


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
	private static final TIntObjectHashMap<Set<BlockSwapper>> blockSwappers = new TIntObjectHashMap<>();

	public ItemTerraAxe() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_AXE);
		MinecraftForge.EVENT_BUS.register(this);
		attackSpeed = -3f;
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "terraaxe_on"), (stack, world, entity) -> {
			if(entity instanceof EntityPlayer && !shouldBreak((EntityPlayer) entity))
				return 0;
			return 1;
		});
	}

	private boolean shouldBreak(EntityPlayer player) {
		return !player.isSneaking() && !ItemTemperanceStone.hasTemperanceActive(player);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		RayTraceResult raycast = ToolCommons.raytraceFromEntity(player.world, player, true, 10);
		if(raycast != null) {
			breakOtherBlock(player, stack, pos, pos, raycast.sideHit);
			ItemLokiRing.breakOnAllCursors(player, this, stack, pos, raycast.sideHit);
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, BlockPos originPos, EnumFacing side) {
		if(shouldBreak(player)) {
			addBlockSwapper(player.world, player, stack, pos, 32, true);
		}
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return false;
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		// Block Swapping ticking should only occur on the server
		if(event.world.isRemote)
			return;

		if(event.phase == Phase.END) {
			int dim = event.world.provider.getDimension();
			if(blockSwappers.containsKey(dim)) {
				Set<BlockSwapper> swappers = blockSwappers.get(dim);

				// Iterate through all of our swappers, removing any
				// which no longer need to tick.
				Iterator<BlockSwapper> swapper = swappers.iterator();
				while(swapper.hasNext()) {
					BlockSwapper next = swapper.next();

					// If a null sneaks in or the swapper is done, remove it
					if(next == null || !next.tick())
						swapper.remove();
				}
			}
		}
	}

	/**
	 * Adds a new block swapper to the provided world as the provided player.
	 * Block swappers are only added on the server, and a marker instance
	 * which is not actually ticked but contains the proper passed in
	 * information will be returned to the client.
	 *
	 * @param world The world to add the swapper to.
	 * @param player The player who is responsible for this swapper.
	 * @param stack The Terra Truncator which caused this block swapper.
	 * @param origCoords The original coordinates the swapper should start at.
	 * @param steps The range of the block swapper, in blocks.
	 * @param leaves If true, will treat leaves specially (see the BlockSwapper
	 * documentation).
	 */
	private static void addBlockSwapper(World world, EntityPlayer player, ItemStack stack, BlockPos origCoords, int steps, boolean leaves) {
		BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, steps, leaves);

		// Block swapper registration should only occur on the server
		if(world.isRemote)
			return;

		// If the mapping for this dimension doesn't exist, create it.
		int dim = world.provider.getDimension();
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new HashSet<>());

		// Add the swapper
		blockSwappers.get(dim).add(swapper);
	}

	/**
	 * A block swapper for the Terra Truncator, which uses a standard
	 * Breadth First Search to try and murder/cut down trees.
	 *
	 * The Terra Truncator will look up to BLOCK_RANGE blocks to find wood
	 * to cut down (only cutting down adjacent pieces of wood, so it doesn't
	 * jump through the air). However, the truncator will only go through
	 * LEAF_BLOCK_RANGE leave blocks in order to prevent adjacent trees which
	 * are connected only by leaves from being devoured as well.
	 *
	 * The leaf restriction is implemented by reducing the number of remaining
	 * steps to the min of LEAF_BLOCK_RANGE and the current range. The restriction
	 * can be removed entirely by setting the "leaves" variable to true, in which
	 * case leaves will be treated normally.
	 */
	private static class BlockSwapper {

		/**
		 * Represents the range which a single block will scan when looking
		 * for the next candidates for swapping. 1 is a good default.
		 */
		public static final int SINGLE_BLOCK_RADIUS = 1;

		/**
		 * The world the block swapper is doing the swapping in.
		 */
		private final World world;

		/**
		 * The player the swapper is swapping for.
		 */
		private final EntityPlayer player;

		/**
		 * The Terra Truncator which created this swapper.
		 */
		private final ItemStack truncator;

		/**
		 * The origin of the swapper (eg, where it started).
		 */
		private final BlockPos origin;

		/**
		 * Denotes whether leaves should be treated specially.
		 */
		private final boolean treatLeavesSpecial;

		/**
		 * The initial range which this block swapper starts with.
		 */
		private final int range;

		/**
		 * The priority queue of all possible candidates for swapping.
		 */
		private final PriorityQueue<SwapCandidate> candidateQueue;

		/**
		 * The set of already swaps coordinates which do not have
		 * to be revisited.
		 */
		private final Set<BlockPos> completedCoords;

		/**
		 * Creates a new block swapper with the provided parameters.
		 * @param world The world the swapper is in.
		 * @param player The player responsible for creating this swapper.
		 * @param truncator The Terra Truncator responsible for creating this swapper.
		 * @param origCoords The original coordinates this swapper should start at.
		 * @param range The range this swapper should swap in.
		 * @param leaves If true, leaves will be treated specially and
		 * severely reduce the radius of further spreading when encountered.
		 */
		public BlockSwapper(World world, EntityPlayer player, ItemStack truncator, BlockPos origCoords, int range, boolean leaves) {
			this.world = world;
			this.player = player;
			this.truncator = truncator;
			origin = origCoords;
			this.range = range;
			treatLeavesSpecial = leaves;

			candidateQueue = new PriorityQueue<>();
			completedCoords = new HashSet<>();

			// Add the origin to our candidate queue with the original range
			candidateQueue.offer(new SwapCandidate(origin, this.range));
		}

		/**
		 * Ticks this Block Swapper, which allows it to swap BLOCK_SWAP_RATE
		 * further blocks and expands the breadth first search. The return
		 * value signifies whether or not the block swapper has more blocks
		 * to swap, or if it has finished swapping.
		 * @return True if the block swapper has more blocks to swap, false
		 * otherwise (implying it can be safely removed).
		 */
		public boolean tick() {
			// If empty, this swapper is done.
			if(candidateQueue.isEmpty())
				return false;

			int remainingSwaps = BLOCK_SWAP_RATE;
			while(remainingSwaps > 0 && !candidateQueue.isEmpty()) {
				SwapCandidate cand = candidateQueue.poll();

				// If we've already completed this location, move along, as this
				// is just a suboptimal one.
				if(completedCoords.contains(cand.coordinates))
					continue;

				// If this candidate is out of range, discard it.
				if(cand.range <= 0)
					continue;

				// Otherwise, perform the break and then look at the adjacent tiles.
				// This is a ridiculous function call here.
				ToolCommons.removeBlockWithDrops(player, truncator, world,
						cand.coordinates,
						state -> ToolCommons.materialsAxe.contains(state.getMaterial()),
						false, treatLeavesSpecial);

				remainingSwaps--;

				completedCoords.add(cand.coordinates);

				// Then, go through all of the adjacent blocks and look if
				// any of them are any good.
				for(BlockPos adj : adjacent(cand.coordinates)) {
					Block block = world.getBlockState(adj).getBlock();

					boolean isWood = block.isWood(world, adj);
					boolean isLeaf = block.isLeaves(world.getBlockState(adj), world, adj);

					// If it's not wood or a leaf, we aren't interested.
					if(!isWood && !isLeaf)
						continue;

					// If we treat leaves specially and this is a leaf, it gets
					// the minimum of the leaf range and the current range - 1.
					// Otherwise, it gets the standard range - 1.
					int newRange = treatLeavesSpecial && isLeaf ?
							Math.min(LEAF_BLOCK_RANGE, cand.range - 1) :
								cand.range - 1;

							candidateQueue.offer(new SwapCandidate(adj, newRange));
				}
			}

			// If we did any iteration, then hang around until next tick.
			return true;
		}

		public List<BlockPos> adjacent(BlockPos original) {
			List<BlockPos> coords = new ArrayList<>();
			// Visit all the surrounding blocks in the provided radius.
			// Gotta love these nested loops, right?
			for(int dx = -SINGLE_BLOCK_RADIUS; dx <= SINGLE_BLOCK_RADIUS; dx++)
				for(int dy = -SINGLE_BLOCK_RADIUS; dy <= SINGLE_BLOCK_RADIUS; dy++)
					for(int dz = -SINGLE_BLOCK_RADIUS; dz <= SINGLE_BLOCK_RADIUS; dz++) {
						// Skip the central tile.
						if(dx == 0 && dy == 0 && dz == 0)
							continue;

						coords.add(original.add(dx, dy, dz));
					}

			return coords;
		}

		/**
		 * Represents a potential candidate for swapping/removal. Sorted by
		 * range (where a larger range is more preferable). As we're using
		 * a priority queue, which is a min-heap internally, larger ranges
		 * are considered "smaller" than smaller ranges (so they show up in the
		 * min-heap first).
		 */
		public static final class SwapCandidate implements Comparable<SwapCandidate> {
			/**
			 * The location of this swap candidate.
			 */
			public final BlockPos coordinates;

			/**
			 * The remaining range of this swap candidate.
			 */
			public final int range;

			/**
			 * Constructs a new Swap Candidate with the provided
			 * coordinates and range.
			 * @param coordinates The coordinates of this candidate.
			 * @param range The remaining range of this candidate.
			 */
			public SwapCandidate(BlockPos coordinates, int range) {
				this.coordinates = coordinates;
				this.range = range;
			}

			@Override
			public int compareTo(@Nonnull SwapCandidate other) {
				// Aka, a bigger range implies a smaller value, meaning
				// bigger ranges will be preferred in a min-heap
				return other.range - range;
			}

			@Override
			public boolean equals(Object other) {
				if(!(other instanceof SwapCandidate)) return false;

				SwapCandidate cand = (SwapCandidate) other;
				return coordinates.equals(cand.coordinates) && range == cand.range;
			}

			@Override
			public int hashCode() {
				return Objects.hash(coordinates, range);
			}
		}
	}

}
