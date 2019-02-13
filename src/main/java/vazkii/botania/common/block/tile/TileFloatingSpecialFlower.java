/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 17, 2014, 5:41:58 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class TileFloatingSpecialFlower extends TileSpecialFlower implements IFloatingFlower {

	public static final String TAG_ISLAND_TYPE = "islandType";
	private IslandType type = IslandType.GRASS;

	@Override
	public boolean isOnSpecialSoil() {
		return false;
	}

	@Override
	public ItemStack getDisplayStack() {
		return ItemBlockSpecialFlower.ofType(subTileName);
	}

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);
		cmp.setString(TAG_ISLAND_TYPE, type.toString());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		IslandType oldType = getIslandType();
		super.onDataPacket(net, packet);
		if(oldType != getIslandType()) {
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);
		type = IslandType.ofType(cmp.getString(TAG_ISLAND_TYPE));
	}

	@Override
	public int getSlowdownFactor() {
		IslandType type = getIslandType();
		if (type == IslandType.MYCEL)
			return SLOWDOWN_FACTOR_MYCEL;
		else if (type == IslandType.PODZOL)
			return SLOWDOWN_FACTOR_PODZOL;
		return 0;
	}

}
