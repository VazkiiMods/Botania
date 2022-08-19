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
import net.minecraft.util.ChunkCoordinates;

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
	private ChunkCoordinates flowerCoords = new ChunkCoordinates();
	private ChunkCoordinates validCoords = new ChunkCoordinates();

	@Override
	public boolean canUpdate() {
		return false;
	}

	public void setGeneration(TileEntity flower, int gen) {
		generation = gen;
		if(!ticked) {
			flowerCoords.posX = flower.xCoord;
			flowerCoords.posY = flower.yCoord;
			flowerCoords.posZ = flower.zCoord;
			validCoords.posX = xCoord;
			validCoords.posY = yCoord;
			validCoords.posZ = zCoord;
			ticked = true;
		} else if(!matchCoords(validCoords, this) || !matchCoords(flowerCoords, flower))
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
	}

	public boolean isSameFlower(TileEntity flower) {
		return matchCoords(validCoords, this) && matchCoords(flowerCoords, flower);
	}

	private boolean matchCoords(ChunkCoordinates coords, TileEntity tile) {
		return coords.posX == tile.xCoord && coords.posY == tile.yCoord && coords.posZ == tile.zCoord;
	}

	public int getGeneration() {
		return generation;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_GENERATION, generation);
		cmp.setBoolean(TAG_TICKED, ticked);
		if(ticked) {
			cmp.setInteger(TAG_FLOWER_X, flowerCoords.posX);
			cmp.setInteger(TAG_FLOWER_Y, flowerCoords.posY);
			cmp.setInteger(TAG_FLOWER_Z, flowerCoords.posZ);
			cmp.setInteger(TAG_VALID_X, validCoords.posX);
			cmp.setInteger(TAG_VALID_Y, validCoords.posY);
			cmp.setInteger(TAG_VALID_Z, validCoords.posZ);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		generation = cmp.getInteger(TAG_GENERATION);
		ticked = cmp.getBoolean(TAG_TICKED);
		if(ticked) {
			flowerCoords.posX = cmp.getInteger(TAG_FLOWER_X);
			flowerCoords.posY = cmp.getInteger(TAG_FLOWER_Y);
			flowerCoords.posZ = cmp.getInteger(TAG_FLOWER_Z);
			validCoords.posX = cmp.getInteger(TAG_VALID_X);
			validCoords.posY = cmp.getInteger(TAG_VALID_Y);
			validCoords.posZ = cmp.getInteger(TAG_VALID_Z);
		}
	}

}
