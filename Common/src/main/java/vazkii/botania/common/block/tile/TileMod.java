/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMod extends BlockEntity {
	public TileMod(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		writePacketNBT(tag);
	}

	@Nonnull
	@Override
	public final CompoundTag getUpdateTag() {
		var tag = new CompoundTag();
		writePacketNBT(tag);
		return tag;
	}

	@Override
	public void load(@Nonnull CompoundTag tag) {
		super.load(tag);
		readPacketNBT(tag);
	}

	public void writePacketNBT(CompoundTag cmp) {}

	public void readPacketNBT(CompoundTag cmp) {}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
