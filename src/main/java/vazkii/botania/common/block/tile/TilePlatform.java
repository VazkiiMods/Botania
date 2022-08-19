/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 7, 2014, 2:24:51 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaCollisionGhost;

public class TilePlatform extends TileCamo implements IManaCollisionGhost {

	@Override
	public boolean isGhost() {
		return true;
	}

	public boolean onWanded(EntityPlayer player) {
		if(player != null) {
			if(camo == null || player.isSneaking())
				swapSelfAndPass(this, true);
			else swapSurroudings(this, false);
			return true;
		}

		return false;
	}

	void swapSelfAndPass(TilePlatform tile, boolean empty) {
		swap(tile, empty);
		swapSurroudings(tile, empty);
	}

	void swapSurroudings(TilePlatform tile, boolean empty) {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int x = tile.xCoord + dir.offsetX;
			int y = tile.yCoord + dir.offsetY;
			int z = tile.zCoord + dir.offsetZ;
			TileEntity tileAt = worldObj.getTileEntity(x, y, z);
			if(tileAt != null && tileAt instanceof TilePlatform) {
				TilePlatform platform = (TilePlatform) tileAt;
				if(empty ? platform.camo != null : platform.camo == null)
					swapSelfAndPass(platform, empty);
			}
		}
	}

	void swap(TilePlatform tile, boolean empty) {
		tile.camo = empty ? null : camo;
		tile.camoMeta = empty ? 0 : camoMeta;
		worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
	}

}
