/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 9, 2014, 5:11:34 PM (GMT)]
 */
package vazkii.botania.common.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.IFloatingFlower.IslandType;
import vazkii.botania.client.fx.ParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemGrassSeeds extends ItemMod implements IFloatingFlowerVariant {
	/**
	 * Represents a map of dimension IDs to a set of all block swappers
	 * active in that dimension.
	 */
	private static final Map<DimensionType, Set<BlockSwapper>> blockSwappers = new HashMap<>();
	private static final Map<IslandType, float[]> COLORS = ImmutableMap.<IslandType, float[]>builder()
			.put(IslandType.GRASS, new float[] {0F, 0.4F, 0F})
			.put(IslandType.PODZOL, new float[] {0.5F, 0.37F, 0F})
			.put(IslandType.MYCEL, new float[] {0.27F, 0F, 0.33F})
			.put(IslandType.DRY, new float[] {0.4F, 0.5F, 0.05F})
			.put(IslandType.GOLDEN, new float[] {0.75F, 0.7F, 0F})
			.put(IslandType.VIVID, new float[] {0F, 0.5F, 0.1F})
			.put(IslandType.SCORCHED, new float[] {0.75F, 0F, 0F})
			.put(IslandType.INFUSED, new float[] {0F, 0.55F, 0.55F})
			.put(IslandType.MUTATED, new float[] {0.4F, 0.1F, 0.4F})
			.build();

	private final IslandType type;

	public ItemGrassSeeds(IslandType type, Properties props) {
		super(props);
		this.type = type;
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = ctx.getItem();

		if(state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK && type != IslandType.GRASS) {
			if(!world.isRemote) {
				BlockSwapper swapper = addBlockSwapper(world, pos, type);
				world.setBlockState(pos, swapper.stateToSet);
				stack.shrink(1);
			} else {
				float r = 0F;
				float g = 0.4F;
				float b = 0F;

				if(COLORS.containsKey(type)) {
					float[] colors = COLORS.get(type);
					r = colors[0];
					g = colors[1];
					b = colors[2];
				}

				for(int i = 0; i < 50; i++) {
					double x = (Math.random() - 0.5) * 3;
					double y = Math.random() - 0.5 + 1;
					double z = (Math.random() - 0.5) * 3;
					float velMul = 0.025F;

                    float motionx = (float) -x * velMul;
                    float motiony = (float) -y * velMul;
                    float motionz = (float) -z * velMul;
                    ParticleData data = ParticleData.wisp((float) Math.random() * 0.15F + 0.15F, r, g, b);
                    world.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, motionx, motiony, motionz);
                }
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@SubscribeEvent
	public static void onTickEnd(TickEvent.WorldTickEvent event) {
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
			DimensionType dim = event.world.getDimension().getType();
			if(blockSwappers.containsKey(dim)) {
				blockSwappers.get(dim).removeIf(next -> next == null || !next.tick());
			}
		}
	}

	/**
	 * Adds a grass seed block swapper to the world at the provided positiona
	 * and with the provided meta (which designates the type of the grass
	 * being spread).
	 *
	 * Block swappers are only actually created on the server, so a client
	 * calling this method will recieve a marker block swapper which contains
	 * the provided information but is not ticked.
	 * @param world The world the swapper will be in.
	 * @param pos The position of the swapper.
	 * @param type The IslandType of the grass seed
	 * @return The created block swapper.
	 */
	private static BlockSwapper addBlockSwapper(World world, BlockPos pos, IslandType type) {
		BlockSwapper swapper = new BlockSwapper(world, pos, stateForType(type));

		DimensionType dim = world.getDimension().getType();
		blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);

		return swapper;
	}

	private static BlockState stateForType(IslandType type) {
		if(type == IslandType.PODZOL)
			return Blocks.PODZOL.getDefaultState();
		else if(type == IslandType.MYCEL)
			return Blocks.MYCELIUM.getDefaultState();
		else if(type == IslandType.DRY)
			return ModBlocks.dryGrass.getDefaultState();
		else if(type == IslandType.GOLDEN)
			return ModBlocks.goldenGrass.getDefaultState();
		else if(type == IslandType.VIVID)
			return ModBlocks.vividGrass.getDefaultState();
		else if(type == IslandType.SCORCHED)
			return ModBlocks.scorchedGrass.getDefaultState();
		else if(type == IslandType.INFUSED)
			return ModBlocks.infusedGrass.getDefaultState();
		else if(type == IslandType.MUTATED)
			return ModBlocks.mutatedGrass.getDefaultState();
		else return Blocks.GRASS_BLOCK.getDefaultState();
	}

	/**
	 * A block swapper for the Pasture Seeds, which swaps dirt and grass blocks
	 * centered around a provided point to a provided block/metadata.
	 */
	private static class BlockSwapper {

		/**
		 * The range of the block swapper, in blocks.
		 */
		public static final int RANGE = 3;

		/**
		 * The range around which a block can spread in a single tick.
		 */
		public static final int TICK_RANGE = 1;

		private final World world;
		private final Random rand;
		private final BlockState stateToSet;

		private final BlockPos startCoords;
		private int ticksExisted = 0;

		/**
		 * Constructs a new block swapper with the provided world, starting
		 * coordinates, target block, and target metadata.
		 * @param world The world to swap blocks in.
		 * @param coords The central coordinates to swap blocks around.
		 * @param state The target blockstate to swap dirt and grass to.
		 */
		public BlockSwapper(World world, BlockPos coords, BlockState state) {
			this.world = world;
			stateToSet = state;
			rand = new Random(coords.hashCode());
			startCoords = coords;
		}

		/**
		 * Ticks this block swapper, allowing it to make an action during
		 * this game tick. This method should return "false" when the swapper
		 * has finished operation and should be removed from the world.
		 * @return true if the swapper should continue to exist, false if it
		 * should be removed.
		 */
		public boolean tick() {
			if(++ticksExisted % 20 == 0) {
				for(BlockPos pos : BlockPos.getAllInBoxMutable(startCoords.add(-RANGE, 0, -RANGE),
						startCoords.add(RANGE, 0, RANGE))) {
					if(world.getBlockState(pos) == stateToSet)
						tickBlock(pos);
				}
			}

			// This swapper should exist for 80 ticks
			return ticksExisted < 80;
		}

		/**
		 * Tick a specific block position, finding the valid blocks
		 * immediately adjacent to it and then replacing one at random.
		 * @param pos The positions to use.
		 */
		public void tickBlock(BlockPos pos) {
			List<BlockPos> validCoords = new ArrayList<>();

			// Go around this block and aggregate valid blocks.
			for(int xOffset = -TICK_RANGE; xOffset <= TICK_RANGE; xOffset++) {
				for(int zOffset = -TICK_RANGE; zOffset <= TICK_RANGE; zOffset++) {
					// Skip the current block
					if(xOffset == 0 && zOffset == 0) continue;

					if(isValidSwapPosition(pos.add(xOffset, 0, zOffset)))
						validCoords.add(pos.add(xOffset, 0, zOffset));
				}
			}

			// If we can make changes, and have at least 1 block to swap,
			// then swap a random block from the valid blocks we could swap.
			if(!validCoords.isEmpty()) {
				BlockPos toSwap = validCoords.get(rand.nextInt(validCoords.size()));

				world.setBlockState(toSwap, stateToSet);
			}
		}

		/**
		 * Determines if a given position is a valid location to spread to, which
		 * means that the block must be either dirt or grass (with meta 0),
		 * and have a block above it which does not block grass growth.
		 * @param pos The position to check.
		 * @return True if the position is valid to swap, false otherwise.
		 */
		public boolean isValidSwapPosition(BlockPos pos) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			// Valid blocks to spread to are either dirt or grass, and do not
			// have blocks which block grass growth.

			// See http://minecraft.gamepedia.com/Grass_Block
			// The major rule is that a block which reduces light
			// levels by 2 or more blocks grass growth.

			return (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK)
					&& world.getBlockState(pos.up()).getOpacity(world, pos.up()) <= 1;
		}
	}

	@Override
	public IslandType getIslandType(ItemStack stack) {
		return type;
	}

}
