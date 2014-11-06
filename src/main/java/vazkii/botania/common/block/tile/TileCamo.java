/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 7, 2014, 2:21:28 PM (GMT)]
 */
package vazkii.botania.common.block.tile;


import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileCamo extends TileMod {

	private static final String TAG_CAMO = "camo";
	private static final String TAG_CAMO_META = "camoMeta";

	public Block camo;
	public int camoMeta;

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		if(camo != null) {
			cmp.setString(TAG_CAMO, Block.blockRegistry.getNameForObject(camo));
			cmp.setInteger(TAG_CAMO_META, camoMeta);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		camo = Block.getBlockFromName(cmp.getString(TAG_CAMO));
		camoMeta = cmp.getInteger(TAG_CAMO_META);
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(manager, packet);
		worldObj.markBlockRangeForRenderUpdate(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
	}
}
