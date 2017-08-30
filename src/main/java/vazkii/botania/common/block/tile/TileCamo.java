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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileCamo extends TileMod {

	private static final String TAG_CAMO = "camo";
	private static final String TAG_CAMO_META = "camoMeta";

	public IBlockState camoState;

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		if(camoState != null) {
			cmp.setString(TAG_CAMO, Block.REGISTRY.getNameForObject(camoState.getBlock()).toString());
			cmp.setInteger(TAG_CAMO_META, camoState.getBlock().getMetaFromState(camoState));
		}
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		Block b = Block.getBlockFromName(cmp.getString(TAG_CAMO));
		if (b != null) {
			camoState = b.getStateFromMeta(cmp.getInteger(TAG_CAMO_META));
		}
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		super.onDataPacket(manager, packet);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}
}
