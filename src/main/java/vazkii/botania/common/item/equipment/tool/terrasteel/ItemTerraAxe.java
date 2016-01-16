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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTerraAxe extends ItemManasteelAxe implements ISequentialBreaker {
	

	/**
	 * The number of blocks per tick which the Terra Truncator will
	 * collect.
	 */
	public static final int BLOCK_SWAP_RATE = 10;
	
	/**
	 * The maximum radius (in blocks) which the Terra Truncator will go
	 * in order to try and murder/cut down the tree.
	 */
	public static final int BLOCK_RANGE = 32;
	
	/**
	 * The maximum number of leaf blocks which the Terra Truncator will chew/go
	 * through once a leaf block is encountered.
	 */
	public static final int LEAF_BLOCK_RANGE = 3;
	
	/**
	 * The amount of mana required to restore 1 point of damage.
	 */
	private static final int MANA_PER_DAMAGE = 100;

	/**
	 * Represents a map of dimension IDs to a set of all block swappers
	 * active in that dimension.
	 */
	private static Map<Integer, Set<BlockSwapper>> blockSwappers = new HashMap<Integer, Set<BlockSwapper>>();

	IIcon iconOn, iconOff;

	public ItemTerraAxe() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_AXE);
		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		iconOn = IconHelper.forItem(par1IconRegister, this, 0);
		iconOff = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return iconOn;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return shouldBreak(player) ? iconOn : iconOff;
	}

	public boolean shouldBreak(EntityPlayer player) {
		return !player.isSneaking() && !ItemTemperanceStone.hasTemperanceActive(player);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		MovingObjectPosition raycast = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 10);
		if(raycast != null) {
			breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
			ItemLokiRing.breakOnAllCursors(player, this, stack, x, y, z, raycast.sideHit);
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side) {
		if(shouldBreak(player)) {
			ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
			addBlockSwapper(player.worldObj, player, stack, coords, 32, true);
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
			int dim = event.world.provider.dimensionId;
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
	 * @return The created block swapper.
	 */
	private static BlockSwapper addBlockSwapper(World world, EntityPlayer player, ItemStack stack, ChunkCoordinates origCoords, int steps, boolean leaves) {
		BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, steps, leaves);

		// Block swapper registration should only occur on the server
		if(world.isRemote)
			return swapper;

		// If the mapping for this dimension doesn't exist, create it.
		int dim = world.provider.dimensionId;
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new HashSet<BlockSwapper>());

		// Add the swapper
		blockSwappers.get(dim).add(swapper);

		return swapper;
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
		private final ChunkCoordinates origin;
		
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
		private PriorityQueue<SwapCandidate> candidateQueue;
		
		/**
		 * The set of already swaps coordinates which do not have
		 * to be revisited.
		 */
		private Set<ChunkCoordinates> completedCoords;
		
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
		public BlockSwapper(World world, EntityPlayer player, ItemStack truncator, ChunkCoordinates origCoords, int range, boolean leaves) {
			this.world = world;
			this.player = player;
			this.truncator = truncator;
			this.origin = origCoords;
			this.range = range;
			this.treatLeavesSpecial = leaves;
			
			this.candidateQueue = new PriorityQueue<SwapCandidate>();
			this.completedCoords = new HashSet<ChunkCoordinates>();
			
			// Add the origin to our candidate queue with the original range
			candidateQueue.offer(new SwapCandidate(this.origin, this.range));
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
						cand.coordinates.posX, cand.coordinates.posY, cand.coordinates.posZ, 
						origin.posX, origin.posY, origin.posZ, 
						null, ToolCommons.materialsAxe,
						EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, truncator) > 0, 
						EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, truncator), 
						0F, false, treatLeavesSpecial);
				
				remainingSwaps--;
				
				completedCoords.add(cand.coordinates);
				
				// Then, go through all of the adjacent blocks and look if
				// any of them are any good.
				for(ChunkCoordinates adj : adjacent(cand.coordinates)) {
					Block block = world.getBlock(adj.posX, adj.posY, adj.posZ);
					
					boolean isWood = block.isWood(world, adj.posX, adj.posY, adj.posZ);
					boolean isLeaf = block.isLeaves(world, adj.posX, adj.posY, adj.posZ);
					
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
		
		public List<ChunkCoordinates> adjacent(ChunkCoordinates original) {
			List<ChunkCoordinates> coords = new ArrayList<ChunkCoordinates>();
			// Visit all the surrounding blocks in the provided radius.
			// Gotta love these nested loops, right?
			for(int dx = -SINGLE_BLOCK_RADIUS; dx <= SINGLE_BLOCK_RADIUS; dx++)
				for(int dy = -SINGLE_BLOCK_RADIUS; dy <= SINGLE_BLOCK_RADIUS; dy++)
					for(int dz = -SINGLE_BLOCK_RADIUS; dz <= SINGLE_BLOCK_RADIUS; dz++) {
						// Skip the central tile.
						if(dx == 0 && dy == 0 && dz == 0) 
							continue;
						
						coords.add(new ChunkCoordinates(original.posX + dx, original.posY + dy, original.posZ + dz));
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
			public ChunkCoordinates coordinates;
			
			/**
			 * The remaining range of this swap candidate.
			 */
			public int range;
			
			/**
			 * Constructs a new Swap Candidate with the provided
			 * coordinates and range.
			 * @param coordinates The coordinates of this candidate.
			 * @param range The remaining range of this candidate.
			 */
			public SwapCandidate(ChunkCoordinates coordinates, int range) {
				this.coordinates = coordinates;
				this.range = range;
			}
			
			@Override
			public int compareTo(SwapCandidate other) {
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
		}
	}

}
