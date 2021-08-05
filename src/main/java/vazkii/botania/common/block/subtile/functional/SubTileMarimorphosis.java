/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.util.WeighedRandom;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class SubTileMarimorphosis extends TileEntityFunctionalFlower {
	private static final int COST = 12;
	private static final int RANGE = 8;
	private static final int RANGE_Y = 5;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	// TODO should this try to match the exact same biome sets as <=1.16.1? is this close enough?
	private enum Type {
		FOREST(ModFluffBlocks.biomeStoneForest, Biome.BiomeCategory.FOREST),
		PLAINS(ModFluffBlocks.biomeStonePlains, Biome.BiomeCategory.PLAINS),
		MOUNTAIN(ModFluffBlocks.biomeStoneMountain, Biome.BiomeCategory.EXTREME_HILLS),
		MUSHROOM(ModFluffBlocks.biomeStoneFungal, Biome.BiomeCategory.MUSHROOM),
		SWAMP(ModFluffBlocks.biomeStoneSwamp, Biome.BiomeCategory.SWAMP),
		SANDY(ModFluffBlocks.biomeStoneDesert, Biome.BiomeCategory.DESERT, Biome.BiomeCategory.BEACH),
		COLD(ModFluffBlocks.biomeStoneTaiga, Biome.BiomeCategory.ICY, Biome.BiomeCategory.TAIGA),
		MESA(ModFluffBlocks.biomeStoneMesa, Biome.BiomeCategory.MESA);

		private final Block biomeStone;
		private final Biome.BiomeCategory[] categories;

		Type(Block biomeStone, Biome.BiomeCategory... categories) {
			this.biomeStone = biomeStone;
			this.categories = categories;
		}

		public boolean contains(Biome.BiomeCategory category) {
			for (Biome.BiomeCategory c : categories) {
				if (c == category) {
					return true;
				}
			}
			return false;
		}
	}

	protected SubTileMarimorphosis(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileMarimorphosis(BlockPos pos, BlockState state) {
		this(ModSubtiles.MARIMORPHOSIS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (getLevel().isClientSide || redstoneSignal > 0) {
			return;
		}

		if (getMana() >= COST && ticksExisted % 2 == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				BlockState state = getStoneToPut(coords);
				if (state != null) {
					getLevel().setBlockAndUpdate(coords, state);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getLevel().levelEvent(2001, coords, Block.getId(state));
					}

					addMana(-COST);
					sync();
				}
			}
		}
	}

	public BlockState getStoneToPut(BlockPos coords) {
		Biome.BiomeCategory category = getLevel().getBiome(coords).getBiomeCategory();

		List<StoneEntry> values = new ArrayList<>();
		for (Type type : Type.values()) {
			values.add(new StoneEntry(type, type.contains(category) ? 12 : 1));
		}
		return WeighedRandom.getRandomItem(getLevel().random, values).type.biomeStone.defaultBlockState();
	}

	private static class StoneEntry extends WeighedRandom.WeighedRandomItem {
		private final Type type;

		public StoneEntry(Type type, int weight) {
			super(weight);
			this.type = type;
		}
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		for (BlockPos pos : BlockPos.betweenClosed(getEffectivePos().offset(-range, -rangeY, -range),
				getEffectivePos().offset(range, rangeY, range))) {
			BlockState state = getLevel().getBlockState(pos);
			if (state.getBlock() == Blocks.STONE) {
				possibleCoords.add(pos.immutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getLevel().random.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	public static class Mini extends SubTileMarimorphosis {
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.MARIMORPHOSIS_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}

		@Override
		public int getRangeY() {
			return RANGE_Y_MINI;
		}
	}

}
