/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class TileMod extends TileEntity {
	public TileMod(TileEntityType<?> type) {
		super(type);
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT tag) {
		CompoundNBT ret = super.write(tag);
		writePacketNBT(ret);
		return ret;
	}

	@Nonnull
	@Override
	public final CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		readPacketNBT(tag);
	}

	public void writePacketNBT(CompoundNBT cmp) {}

	public void readPacketNBT(CompoundNBT cmp) {}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT tag = new CompoundNBT();
		writePacketNBT(tag);
		return new SUpdateTileEntityPacket(pos, -999, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		readPacketNBT(packet.getNbtCompound());
	}

}
