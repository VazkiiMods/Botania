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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TilePlatform extends TileCamo {

	public boolean onWanded(EntityPlayer player) {
		if(player != null) {
			if(camoState == null || player.isSneaking())
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
		for(EnumFacing dir : EnumFacing.VALUES) {
			BlockPos pos = tile.getPos().offset(dir);
			TileEntity tileAt = world.getTileEntity(pos);
			if(tileAt != null && tileAt instanceof TilePlatform) {
				TilePlatform platform = (TilePlatform) tileAt;
				if(empty ? platform.camoState != null : platform.camoState == null)
					swapSelfAndPass(platform, empty);
			}
		}
	}

	void swap(TilePlatform tile, boolean empty) {
		tile.camoState = empty ? null : camoState;
		world.notifyBlockUpdate(tile.getPos(), world.getBlockState(tile.getPos()), world.getBlockState(tile.getPos()), 8);
	}

}
