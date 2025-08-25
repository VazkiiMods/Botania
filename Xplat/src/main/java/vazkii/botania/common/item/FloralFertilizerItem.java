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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloralFertilizerItem extends Item {
	private static final int RANGE = 3;
	private static final double CANDIDATE_PROBABILITY = 0.25;

	public FloralFertilizerItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);
		if (state.is(BotaniaTags.Blocks.FERTILIZER_EXCLUDED_PLANTS)
				|| !state.is(BotaniaTags.Blocks.FERTILIZER_FLOWERS_SOIL)
						&& !state.is(BotaniaTags.Blocks.FERTILIZER_MUSHROOMS_SOIL)
						&& !state.is(BotaniaTags.Blocks.FERTILIZER_SPREADABLE_PLANTS)) {
			return InteractionResult.PASS;
		}
		if (world.isClientSide) {
			spawnGrowthParticles(pos, world);
			return InteractionResult.sidedSuccess(true);
		}

		if (state.is(BotaniaTags.Blocks.FERTILIZER_SPREADABLE_PLANTS)) {
			spreadPlant(pos, world, state);
		} else /* is FERTILIZER_FLOWERS_SOIL or FERTILIZER_MUSHROOMS_SOIL */ {
			growFLowersAndMushrooms(pos, world);
		}

		ctx.getItemInHand().shrink(1);
		return InteractionResult.sidedSuccess(false);
	}

	private static void spawnGrowthParticles(BlockPos pos, Level world) {
		for (int i = 0; i < 15; i++) {
			double x = pos.getX() - RANGE + world.random.nextInt(RANGE * 2 + 1) + Math.random();
			double y = pos.getY() + 1;
			double z = pos.getZ() - RANGE + world.random.nextInt(RANGE * 2 + 1) + Math.random();
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			WispParticleData data = WispParticleData.wisp(0.15F + (float) Math.random() * 0.25F, red, green, blue, 1);
			world.addParticle(data, x, y, z, 0, (float) Math.random() * 0.1F - 0.05F, 0);
		}
	}

	public static void spreadPlant(BlockPos pos, Level world, BlockState state) {
		List<BlockPos> validCoords = new ArrayList<>();

		for (BlockPos candidatePos : BlockPos.betweenClosed(
				pos.getX() - RANGE, pos.getY() - 2, pos.getZ() - RANGE,
				pos.getX() + RANGE, pos.getY() + 2, pos.getZ() + RANGE)) {
			// ignore potential positions to make automation a little bit more challenging
			if (Math.random() < CANDIDATE_PROBABILITY || !world.isInWorldBounds(candidatePos) ||
					!world.isEmptyBlock(candidatePos) || !state.canSurvive(world, candidatePos)) {
				continue;
			}
			validCoords.add(candidatePos.immutable());
		}

		// up to 3, but usually 2:
		int numCopies = 1 + world.random.nextInt(2) + world.random.nextInt(2);
		while (numCopies > 0 && !validCoords.isEmpty()) {
			BlockPos coords = validCoords.remove(world.random.nextInt(validCoords.size()));
			world.setBlockAndUpdate(coords, state);
			numCopies--;
		}
	}

	private static void growFLowersAndMushrooms(BlockPos pos, Level world) {
		Optional<HolderSet.Named<Block>> flowersTag =
				BuiltInRegistries.BLOCK.getTag(BotaniaTags.Blocks.MYSTICAL_FLOWERS);
		Optional<HolderSet.Named<Block>> mushroomsTag =
				BuiltInRegistries.BLOCK.getTag(BotaniaTags.Blocks.SHIMMERING_MUSHROOMS);
		boolean flowersAvailable = flowersTag.map(holders -> holders.size() > 0).orElse(false);
		boolean mushroomsAvailable = mushroomsTag.map(holders -> holders.size() > 0).orElse(false);

		List<BlockPos> validCoords = new ArrayList<>();

		for (BlockPos candidatePos : BlockPos.betweenClosed(
				pos.getX() - RANGE, pos.getY() - 2, pos.getZ() - RANGE,
				pos.getX() + RANGE, pos.getY() + 2, pos.getZ() + RANGE)) {
			if (!world.isInWorldBounds(candidatePos) || !world.isEmptyBlock(candidatePos)) {
				continue;
			}
			BlockState belowState = world.getBlockState(candidatePos.below());
			if (flowersAvailable && canPlaceFlower(belowState, world) || mushroomsAvailable && canPlaceMushroom(belowState)) {
				validCoords.add(candidatePos.immutable());
			}
		}

		int petalCount = world.random.nextIntBetweenInclusive(5, 7);
		while (petalCount > 0 && !validCoords.isEmpty()) {
			petalCount--;
			BlockPos coords = validCoords.get(world.random.nextInt(validCoords.size()));
			validCoords.remove(coords);
			BlockState belowState = world.getBlockState(coords.below());
			boolean tryPlaceFlower = flowersAvailable && canPlaceFlower(belowState, world);
			boolean tryPlaceMushroom = mushroomsAvailable && canPlaceMushroom(belowState);

			Optional<Holder<Block>> toPlace;
			if (tryPlaceMushroom && (!tryPlaceFlower || world.random.nextInt(3) == 0)) {
				toPlace = mushroomsTag.get().getRandomElement(world.random);
			} else if (tryPlaceFlower) {
				toPlace = flowersTag.get().getRandomElement(world.random);
				petalCount--;
			} else {
				continue;
			}

			if (toPlace.isPresent() && !toPlace.get().value().defaultBlockState().isAir()) {
				world.setBlockAndUpdate(coords, toPlace.get().value().defaultBlockState());
			}
		}
	}

	private static boolean canPlaceMushroom(BlockState belowState) {
		return belowState.is(BotaniaTags.Blocks.FERTILIZER_MUSHROOMS_SOIL);
	}

	private static boolean canPlaceFlower(BlockState belowState, Level world) {
		return belowState.is(BotaniaTags.Blocks.FERTILIZER_FLOWERS_SOIL) && !world.dimensionType().ultraWarm();
	}
}
