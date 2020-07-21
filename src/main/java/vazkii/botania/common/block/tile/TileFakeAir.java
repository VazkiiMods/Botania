/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.common.block.subtile.functional.SubTileBubbell;

import javax.annotation.Nonnull;

public class TileFakeAir extends TileMod {
	private static final String TAG_FLOWER_X = "flowerX";
	private static final String TAG_FLOWER_Y = "flowerY";
	private static final String TAG_FLOWER_Z = "flowerZ";

	private BlockPos flowerPos = BlockPos.ORIGIN;

	public TileFakeAir() {
		super(ModTiles.FAKE_AIR);
	}

	public void setFlower(BlockEntity tile) {
		flowerPos = tile.getPos();
		markDirty();
	}

	public boolean canStay() {
		return SubTileBubbell.isValidBubbell(world, flowerPos);
	}

	@Nonnull
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag ret = super.toTag(tag);
		ret.putInt(TAG_FLOWER_X, flowerPos.getX());
		ret.putInt(TAG_FLOWER_Y, flowerPos.getY());
		ret.putInt(TAG_FLOWER_Z, flowerPos.getZ());
		return ret;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		flowerPos = new BlockPos(
				tag.getInt(TAG_FLOWER_X),
				tag.getInt(TAG_FLOWER_Y),
				tag.getInt(TAG_FLOWER_Z)
		);
	}

}
