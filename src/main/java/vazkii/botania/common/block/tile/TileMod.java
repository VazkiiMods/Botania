/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import javax.annotation.Nonnull;

public class TileMod extends BlockEntity {
	public TileMod(BlockEntityType<?> type) {
		super(type);
	}

	@Nonnull
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag ret = super.toTag(tag);
		writePacketNBT(ret);
		return ret;
	}

	@Nonnull
	@Override
	public final CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		readPacketNBT(tag);
	}

	public void writePacketNBT(CompoundTag cmp) {}

	public void readPacketNBT(CompoundTag cmp) {}

	@Override
	public final BlockEntityUpdateS2CPacket toUpdatePacket() {
		CompoundTag tag = new CompoundTag();
		writePacketNBT(tag);
		return new BlockEntityUpdateS2CPacket(pos, -999, tag);
	}

	@Override
	public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket packet) {
		super.onDataPacket(net, packet);
		readPacketNBT(packet.getCompoundTag());
	}

}
