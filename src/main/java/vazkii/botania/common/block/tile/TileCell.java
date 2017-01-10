/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 6, 2015, 4:07:16 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

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

	public void setGeneration(TileEntity flower, int gen) {
		generation = gen;
		if(!ticked) {
			flowerCoords = flower.getPos();
			validCoords = getPos();
			ticked = true;
		} else if(!matchCoords(validCoords, this) || !matchCoords(flowerCoords, flower))
			world.setBlockToAir(pos);
	}

	public boolean isSameFlower(TileEntity flower) {
		return matchCoords(validCoords, this) && matchCoords(flowerCoords, flower);
	}

	private boolean matchCoords(BlockPos coords, TileEntity tile) {
		return coords.equals(tile.getPos());
	}

	public int getGeneration() {
		return generation;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_GENERATION, generation);
		cmp.setBoolean(TAG_TICKED, ticked);
		if(ticked) {
			cmp.setInteger(TAG_FLOWER_X, flowerCoords.getX());
			cmp.setInteger(TAG_FLOWER_Y, flowerCoords.getY());
			cmp.setInteger(TAG_FLOWER_Z, flowerCoords.getZ());
			cmp.setInteger(TAG_VALID_X, validCoords.getX());
			cmp.setInteger(TAG_VALID_Y, validCoords.getY());
			cmp.setInteger(TAG_VALID_Z, validCoords.getZ());
		}
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		generation = cmp.getInteger(TAG_GENERATION);
		ticked = cmp.getBoolean(TAG_TICKED);
		if(ticked) {
			flowerCoords = new BlockPos(
					cmp.getInteger(TAG_FLOWER_X),
					cmp.getInteger(TAG_FLOWER_Y),
					cmp.getInteger(TAG_FLOWER_Z)
					);
			validCoords = new BlockPos(
					cmp.getInteger(TAG_VALID_X),
					cmp.getInteger(TAG_VALID_Y),
					cmp.getInteger(TAG_VALID_Z)
					);
		}
	}

}
