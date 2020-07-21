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
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubTileMarimorphosis extends TileEntityFunctionalFlower {
	private static final int COST = 12;
	private static final int RANGE = 8;
	private static final int RANGE_Y = 5;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	private static final Type[] TYPES = new Type[] {
			Type.FOREST,
			Type.PLAINS,
			Type.MOUNTAIN,
			Type.MUSHROOM,
			Type.SWAMP,
			Type.SANDY,
			Type.COLD,
			Type.MESA
	};

	public SubTileMarimorphosis(TileEntityType<?> type) {
		super(type);
	}

	public SubTileMarimorphosis() {
		this(ModSubtiles.MARIMORPHOSIS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (getWorld().isRemote || redstoneSignal > 0) {
			return;
		}

		if (getMana() >= COST && ticksExisted % 2 == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				BlockState state = getStoneToPut(coords);
				if (state != null) {
					getWorld().setBlockState(coords, state);
					if (ConfigHandler.COMMON.blockBreakParticles.get()) {
						getWorld().playEvent(2001, coords, Block.getStateId(state));
					}

					addMana(-COST);
					sync();
				}
			}
		}
	}

	public BlockState getStoneToPut(BlockPos coords) {
		Set<Type> types = BiomeDictionary.getTypes(getWorld().getBiome(coords));

		List<Block> values = new ArrayList<>();
		for (Type type : TYPES) {
			int times = 1;
			if (types.contains(type)) {
				times = 12;
			}

			Block block = biomeTypeToBlock(type);
			for (int j = 0; j < times; j++) {
				values.add(block);
			}
		}

		return values.get(getWorld().rand.nextInt(values.size())).getDefaultState();
	}

	private Block biomeTypeToBlock(Type biomeType) {
		switch (biomeType.getName()) {
		default:
			throw new IllegalArgumentException("Should have verified type is suitable already: " + biomeType);
		case "FOREST":
			return ModFluffBlocks.biomeStoneForest;
		case "PLAINS":
			return ModFluffBlocks.biomeStonePlains;
		case "MOUNTAIN":
			return ModFluffBlocks.biomeStoneMountain;
		case "MUSHROOM":
			return ModFluffBlocks.biomeStoneFungal;
		case "SWAMP":
			return ModFluffBlocks.biomeStoneSwamp;
		case "SANDY":
			return ModFluffBlocks.biomeStoneDesert;
		case "COLD":
			return ModFluffBlocks.biomeStoneTaiga;
		case "MESA":
			return ModFluffBlocks.biomeStoneMesa;
		}
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		for (BlockPos pos : BlockPos.getAllInBoxMutable(getEffectivePos().add(-range, -rangeY, -range),
				getEffectivePos().add(range, rangeY, range))) {
			BlockState state = getWorld().getBlockState(pos);
			if (state.getBlock() == Blocks.STONE) {
				possibleCoords.add(pos.toImmutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getWorld().rand.nextInt(possibleCoords.size()));
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
