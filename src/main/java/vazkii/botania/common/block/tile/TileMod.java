/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TileMod extends BlockEntity implements BlockEntityClientSerializable {
	public TileMod(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nonnull
	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag ret = super.save(tag);
		writePacketNBT(ret);
		return ret;
	}

	@Nonnull
	@Override
	public final CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		readPacketNBT(tag);
	}

	public void writePacketNBT(CompoundTag cmp) {}

	public void readPacketNBT(CompoundTag cmp) {}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		writePacketNBT(tag);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		readPacketNBT(tag);
	}
}
