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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nonnull;

public class TilePlatform extends TileMod {
	public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();
	public static final ModelProperty<BlockPos> HELD_POS = new ModelProperty<>();

	private static final String TAG_CAMO = "camo";

	public BlockState camoState;

	public TilePlatform() {
		super(ModTiles.PLATFORM);
	}

	public boolean onWanded(PlayerEntity player) {
		if (player != null) {
			if (camoState == null || player.isSneaking()) {
				swapSelfAndPass(this, true);
			} else {
				swapSurroudings(this, false);
			}
			return true;
		}

		return false;
	}

	private void swapSelfAndPass(TilePlatform tile, boolean empty) {
		swap(tile, empty);
		swapSurroudings(tile, empty);
	}

	private void swapSurroudings(TilePlatform tile, boolean empty) {
		for (Direction dir : Direction.values()) {
			BlockPos pos = tile.getPos().offset(dir);
			BlockEntity tileAt = world.getBlockEntity(pos);
			if (tileAt instanceof TilePlatform) {
				TilePlatform platform = (TilePlatform) tileAt;
				if (empty == (platform.camoState != null)) {
					swapSelfAndPass(platform, empty);
				}
			}
		}
	}

	private void swap(TilePlatform tile, boolean empty) {
		tile.camoState = empty ? null : camoState;
		world.updateListeners(tile.getPos(), tile.getCachedState(), tile.getCachedState(), 3);
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		if (camoState != null) {
			cmp.put(TAG_CAMO, NbtHelper.fromBlockState(camoState));
		}
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		camoState = NbtHelper.toBlockState(cmp.getCompound(TAG_CAMO));
		if (camoState.isAir()) {
			camoState = null;
		}
	}

	@Override
	public void onDataPacket(ClientConnection manager, BlockEntityUpdateS2CPacket packet) {
		super.onDataPacket(manager, packet);
		requestModelDataUpdate();
		if (world instanceof ClientWorld) {
			world.updateListeners(getPos(), getCachedState(), getCachedState(), 0);
		}
	}

	@Nonnull
	@Override
	public IModelData getModelData() {
		return new ModelDataMap.Builder()
				.withInitial(HELD_POS, getPos())
				.withInitial(HELD_STATE, camoState)
				.build();
	}
}
