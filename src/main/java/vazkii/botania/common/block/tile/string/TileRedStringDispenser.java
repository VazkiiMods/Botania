/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 14, 2014, 11:00:16 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ChunkCoordinates;

public class TileRedStringDispenser extends TileRedStringContainer {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		return tile != null && tile instanceof TileEntityDispenser;
	}

	public void tickDispenser() {
		ChunkCoordinates bind = getBinding();
		if(bind != null) {
			TileEntity tile = worldObj.getTileEntity(bind.posX, bind.posY, bind.posZ);
			if(tile instanceof TileEntityDispenser)
				worldObj.scheduleBlockUpdate(bind.posX, bind.posY, bind.posZ, tile.getBlockType(), tile.getBlockType().tickRate(worldObj));
		}
	}

}
