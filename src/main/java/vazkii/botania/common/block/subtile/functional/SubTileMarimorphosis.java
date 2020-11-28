/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

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
		FOREST(ModFluffBlocks.biomeStoneForest, Biome.Category.FOREST),
		PLAINS(ModFluffBlocks.biomeStonePlains, Biome.Category.PLAINS),
		MOUNTAIN(ModFluffBlocks.biomeStoneMountain, Biome.Category.EXTREME_HILLS),
		MUSHROOM(ModFluffBlocks.biomeStoneFungal, Biome.Category.MUSHROOM),
		SWAMP(ModFluffBlocks.biomeStoneSwamp, Biome.Category.SWAMP),
		SANDY(ModFluffBlocks.biomeStoneDesert, Biome.Category.DESERT, Biome.Category.BEACH),
		COLD(ModFluffBlocks.biomeStoneTaiga, Biome.Category.ICY, Biome.Category.TAIGA),
		MESA(ModFluffBlocks.biomeStoneMesa, Biome.Category.MESA);

		private final Block biomeStone;
		private final Biome.Category[] categories;

		Type(Block biomeStone, Biome.Category... categories) {
			this.biomeStone = biomeStone;
			this.categories = categories;
		}

		public boolean contains(Biome.Category category) {
			for (Biome.Category c : categories) {
				if (c == category) {
					return true;
				}
			}
			return false;
		}
	}

	public SubTileMarimorphosis(BlockEntityType<?> type) {
		super(type);
	}

	public SubTileMarimorphosis() {
		this(ModSubtiles.MARIMORPHOSIS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (getWorld().isClient || redstoneSignal > 0) {
			return;
		}

		if (getMana() >= COST && ticksExisted % 2 == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				BlockState state = getStoneToPut(coords);
				if (state != null) {
					getWorld().setBlockState(coords, state);
					if (ConfigHandler.COMMON.blockBreakParticles.getValue()) {
						getWorld().syncWorldEvent(2001, coords, Block.getRawIdFromState(state));
					}

					addMana(-COST);
					sync();
				}
			}
		}
	}

	public BlockState getStoneToPut(BlockPos coords) {
		Biome.Category category = getWorld().getBiome(coords).getCategory();

		List<StoneEntry> values = new ArrayList<>();
		for (Type type : Type.values()) {
			values.add(new StoneEntry(type, type.contains(category) ? 12 : 1));
		}
		return WeightedPicker.getRandom(getWorld().random, values).type.biomeStone.getDefaultState();
	}

	private static class StoneEntry extends WeightedPicker.Entry {
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

		for (BlockPos pos : BlockPos.iterate(getEffectivePos().add(-range, -rangeY, -range),
				getEffectivePos().add(range, rangeY, range))) {
			BlockState state = getWorld().getBlockState(pos);
			if (state.getBlock() == Blocks.STONE) {
				possibleCoords.add(pos.toImmutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getWorld().random.nextInt(possibleCoords.size()));
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
		public Mini() {
			super(ModSubtiles.MARIMORPHOSIS_CHIBI);
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
