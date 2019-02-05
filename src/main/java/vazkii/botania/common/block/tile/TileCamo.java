/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:21:28 PM (GMT)]
 */
package vazkii.botania.common.block.tile;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileCamo extends TileMod {

	private static final String TAG_CAMO = "camo";

	public IBlockState camoState;

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		if(camoState != null) {
			cmp.setTag(TAG_CAMO, NBTUtil.writeBlockState(camoState));
		}
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		camoState = NBTUtil.readBlockState(cmp.getCompound(TAG_CAMO));
		if(camoState.isAir()) {
			camoState = null;
		}
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		super.onDataPacket(manager, packet);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}
}
