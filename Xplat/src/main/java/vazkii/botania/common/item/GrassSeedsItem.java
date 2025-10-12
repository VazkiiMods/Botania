/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;

public class GrassSeedsItem extends Item {
	/**
	 * Represents a map of dimension IDs to a set of all block swappers
	 * active in that dimension.
	 */
	private static final Map<ResourceKey<Level>, Set<BlockSwapper>> blockSwappers = new HashMap<>();

	private final Block grassBlock;
	private final int color;

	public GrassSeedsItem(Block grassBlock, int color, Properties props) {
		super(props);
		this.grassBlock = grassBlock;
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public Block getGrassBlock() {
		return grassBlock;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();

		return applySeeds(world, pos, stack);
	}

	public InteractionResult applySeeds(Level world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);

		if (state.is(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE) && !state.is(grassBlock)) {
			if (!world.isClientSide) {
				BlockSwapper swapper = addBlockSwapper(world, pos, grassBlock);
				world.setBlockAndUpdate(pos, swapper.stateToSet);
				stack.shrink(1);
			} else {
				spawnParticles(world, pos, color);
			}

			return InteractionResult.sidedSuccess(world.isClientSide());
		}

		return InteractionResult.PASS;
	}

	public static void spawnParticles(Level world, BlockPos pos, int color) {
		float velMul = -0.025F;
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;
		RandomSource rng = world.getRandom();
		Vec3 posVec = pos.getCenter();
		for (int i = 0; i < 50; i++) {
			double x = (rng.nextDouble() - 0.5) * 3;
			double y = rng.nextDouble() - 0.5 + 1;
			double z = (rng.nextDouble() - 0.5) * 3;

			float motionX = (float) x * velMul;
			float motionY = (float) y * velMul;
			float motionZ = (float) z * velMul;
			WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.15f + 0.15f, r, g, b);
			world.addParticle(data, posVec.x() + x, posVec.y() + y, posVec.z() + z, motionX, motionY, motionZ);
		}
	}

	public static void onTickEnd(ServerLevel world) {
		ResourceKey<Level> dim = world.dimension();
		if (blockSwappers.containsKey(dim)) {
			blockSwappers.get(dim).removeIf(next -> next == null || !next.tick());
		}
	}

	/**
	 * Adds a grass seed block swapper to the world at the provided position
	 * and with the provided meta (which designates the type of the grass
	 * being spread).
	 * Block swappers are only actually created on the server, so a client
	 * calling this method will receive a marker block swapper which contains
	 * the provided information but is not ticked.
	 * 
	 * @param world The world the swapper will be in.
	 * @param pos   The position of the swapper.
	 * @param block The grass block type
	 * @return The created block swapper.
	 */
	private static BlockSwapper addBlockSwapper(Level world, BlockPos pos, Block block) {
		BlockSwapper swapper = new BlockSwapper(world, pos, block.defaultBlockState());

		ResourceKey<Level> dim = world.dimension();
		blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);

		return swapper;
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
		 * The horizontal range around which a block can spread in a single tick.
		 */
		public static final int TICK_RANGE_HORIZONTAL = 1;

		/**
		 * The vertical range around which a block can spread in a single tick.
		 */
		public static final int TICK_RANGE_VERTICAL = 2;

		private final Level world;
		private final Random rand;
		private final BlockState stateToSet;

		private final BlockPos startCoords;
		private int ticksExisted = 0;

		/**
		 * Constructs a new block swapper with the provided world, starting
		 * coordinates, target block, and target metadata.
		 * 
		 * @param world  The world to swap blocks in.
		 * @param coords The central coordinates to swap blocks around.
		 * @param state  The target blockstate to swap dirt and grass to.
		 */
		public BlockSwapper(Level world, BlockPos coords, BlockState state) {
			this.world = world;
			stateToSet = state;
			rand = new Random(coords.hashCode());
			startCoords = coords;
		}

		/**
		 * Ticks this block swapper, allowing it to make an action during
		 * this game tick. This method should return "false" when the swapper
		 * has finished operation and should be removed from the world.
		 * 
		 * @return true if the swapper should continue to exist, false if it
		 *         should be removed.
		 */
		public boolean tick() {
			if (++ticksExisted % 20 == 0) {
				var tickPositions = new ArrayList<BlockPos>();
				for (BlockPos pos : MathHelper.aroundPosClosed(startCoords, RANGE)) {
					if (world.getBlockState(pos) == stateToSet && canPropagate(pos)) {
						tickPositions.add(pos.immutable());
					}
				}
				Collections.shuffle(tickPositions);
				tickPositions.forEach(this::tickBlock);
			}

			// This swapper should exist for 80 ticks
			return ticksExisted < 80;
		}

		/**
		 * Tick a specific block position, finding the valid blocks
		 * immediately adjacent to it and then replacing one at random.
		 * 
		 * @param pos The positions to use.
		 */
		public void tickBlock(BlockPos pos) {
			List<BlockPos> validCoords = new ArrayList<>();

			// Go around this block and aggregate valid blocks.
			for (BlockPos targetPos : MathHelper.aroundPosClosed(pos, TICK_RANGE_HORIZONTAL, TICK_RANGE_VERTICAL)) {
				// Skip the current block, and any blocks that are already converted
				if (targetPos.equals(pos) || world.getBlockState(targetPos) == stateToSet) {
					continue;
				}

				if (isValidSwapPosition(targetPos)) {
					validCoords.add(targetPos.immutable());
				}
			}

			// If we can make changes, and have at least 1 block to swap,
			// then swap a random block from the valid blocks we could swap.
			if (!validCoords.isEmpty()) {
				BlockPos toSwap = validCoords.get(rand.nextInt(validCoords.size()));

				world.setBlockAndUpdate(toSwap, stateToSet);
			}
		}

		/**
		 * Determines if a given position is a valid location to spread to, which
		 * means that the block must be either dirt or grass (with meta 0),
		 * and have a block above it which does not block grass growth.
		 * 
		 * @param pos The position to check.
		 * @return True if the position is valid to swap, false otherwise.
		 */
		public boolean isValidSwapPosition(BlockPos pos) {
			BlockState state = world.getBlockState(pos);
			return state.is(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE) && canBeGrass(pos, state);
		}

		// [VanillaCopy] net.minecraft.world.level.block.SpreadingSnowyDirtBlock#canBeGrass
		private boolean canBeGrass(BlockPos pos, BlockState state) {
			BlockPos abovePos = pos.above();
			BlockState aboveState = world.getBlockState(abovePos);
			if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
				// single snow layer, okay to spread below that
				return true;
			}
			if (aboveState.getFluidState().getAmount() == 8) {
				// full-height liquid, don't spread
				return false;
			}
			int lightLevel = LightEngine.getLightBlockInto(world, state, pos, aboveState, abovePos, Direction.UP, aboveState.getLightBlock(world, abovePos));
			return lightLevel < world.getMaxLightLevel();
		}

		// [VanillaCopy] net.minecraft.world.level.block.SpreadingSnowyDirtBlock#canPropagate
		private boolean canPropagate(BlockPos pos) {
			BlockPos abovePos = pos.above();
			return canBeGrass(pos, stateToSet) && !world.getFluidState(abovePos).is(FluidTags.WATER);
		}
	}
}
