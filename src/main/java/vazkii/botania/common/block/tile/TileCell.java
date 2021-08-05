/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;

public class TileCell extends TileMod {
	private static final String TAG_GENERATION = "generation";
	private static final String TAG_TICKED = "ticked";
	private static final String TAG_FLOWER_X = "flowerX";
	private static final String TAG_FLOWER_Y = "flowerY";
	private static final String TAG_FLOWER_Z = "flowerZ";
	private static final String TAG_VALID_X = "validX";
	private static final String TAG_VALID_Y = "validY";
	private static final String TAG_VALID_Z = "validZ";

	private int generation;
	private boolean ticked;
	private BlockPos flowerCoords = new BlockPos(0, -1, 0);
	private BlockPos validCoords = new BlockPos(0, -1, 0);

	public TileCell(BlockPos pos, BlockState state) {
		super(ModTiles.CELL_BLOCK, pos, state);
	}

	public void setGeneration(SubTileDandelifeon flower, int gen) {
		generation = gen;
		if (!ticked) {
			flowerCoords = flower.getEffectivePos();
			validCoords = getBlockPos();
			ticked = true;
		} else if (!validCoords.equals(getBlockPos()) || !flowerCoords.equals(flower.getEffectivePos())) {
			level.removeBlock(worldPosition, false);
		}
	}

	public boolean isSameFlower(SubTileDandelifeon flower) {
		return validCoords.equals(getBlockPos()) && flowerCoords.equals(flower.getEffectivePos());
	}

	public int getGeneration() {
		return generation;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.putInt(TAG_GENERATION, generation);
		cmp.putBoolean(TAG_TICKED, ticked);
		if (ticked) {
			cmp.putInt(TAG_FLOWER_X, flowerCoords.getX());
			cmp.putInt(TAG_FLOWER_Y, flowerCoords.getY());
			cmp.putInt(TAG_FLOWER_Z, flowerCoords.getZ());
			cmp.putInt(TAG_VALID_X, validCoords.getX());
			cmp.putInt(TAG_VALID_Y, validCoords.getY());
			cmp.putInt(TAG_VALID_Z, validCoords.getZ());
		}
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		generation = cmp.getInt(TAG_GENERATION);
		ticked = cmp.getBoolean(TAG_TICKED);
		if (ticked) {
			flowerCoords = new BlockPos(
					cmp.getInt(TAG_FLOWER_X),
					cmp.getInt(TAG_FLOWER_Y),
					cmp.getInt(TAG_FLOWER_Z)
			);
			validCoords = new BlockPos(
					cmp.getInt(TAG_VALID_X),
					cmp.getInt(TAG_VALID_Y),
					cmp.getInt(TAG_VALID_Z)
			);
		}
	}

}
