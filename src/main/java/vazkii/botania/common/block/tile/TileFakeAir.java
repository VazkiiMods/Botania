/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 10, 2015, 10:24:48 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.block.subtile.functional.SubTileBubbell;

import javax.annotation.Nonnull;

public class TileFakeAir extends TileMod {

	private static final String TAG_FLOWER_X = "flowerX";
	private static final String TAG_FLOWER_Y = "flowerY";
	private static final String TAG_FLOWER_Z = "flowerZ";

	private BlockPos flowerPos = BlockPos.ORIGIN;

	public void setFlower(TileEntity tile) {
		flowerPos = tile.getPos();
	}

	public boolean canStay() {
		return SubTileBubbell.isValidBubbell(world, flowerPos);
	}

	@Nonnull
	@Override
	public NBTTagCompound write(NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound ret = super.write(par1nbtTagCompound);
		ret.setInt(TAG_FLOWER_X, flowerPos.getX());
		ret.setInt(TAG_FLOWER_Y, flowerPos.getY());
		ret.setInt(TAG_FLOWER_Z, flowerPos.getZ());
		return ret;
	}

	@Override
	public void read(NBTTagCompound par1nbtTagCompound) {
		super.read(par1nbtTagCompound);
		flowerPos = new BlockPos(
				par1nbtTagCompound.getInt(TAG_FLOWER_X),
				par1nbtTagCompound.getInt(TAG_FLOWER_Y),
				par1nbtTagCompound.getInt(TAG_FLOWER_Z)
				);
	}

}
