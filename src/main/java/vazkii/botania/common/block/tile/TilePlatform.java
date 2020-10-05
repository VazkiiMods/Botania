/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
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
import javax.annotation.Nullable;

public class TilePlatform extends TileMod implements RenderAttachmentBlockEntity {
	private static final String TAG_CAMO = "camo";

	@Nullable
	private BlockState camoState;

	public TilePlatform() {
		super(ModTiles.PLATFORM);
	}

	public boolean onWanded(PlayerEntity player) {
		if (player != null) {
			if (getCamoState() == null || player.isSneaking()) {
				swapSelfAndPass(this, true);
			} else {
				swapSurroudings(this, false);
			}
			return true;
		}

		return false;
	}

	@Nullable
	public BlockState getCamoState() {
		return camoState;
	}

	public void setCamoState(@Nullable BlockState state) {
		this.camoState = state;

		if (world != null) {
			world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
			if (!world.isClient) {
				world.updateNeighbors(pos, getCachedState().getBlock());
			}
		}
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
				if (empty == (platform.getCamoState() != null)) {
					swapSelfAndPass(platform, empty);
				}
			}
		}
	}

	private void swap(TilePlatform tile, boolean empty) {
		tile.setCamoState(empty ? null : getCamoState());
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		if (getCamoState() != null) {
			cmp.put(TAG_CAMO, NbtHelper.fromBlockState(getCamoState()));
		}
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		BlockState state = NbtHelper.toBlockState(cmp.getCompound(TAG_CAMO));
		if (state.isAir()) {
			state = null;
		}
		setCamoState(state);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		super.fromClientTag(tag);
		if (world instanceof ClientWorld) {
			world.updateListeners(getPos(), getCachedState(), getCachedState(), 0);
		}
	}

	@Override
	public Object getRenderAttachmentData() {
		return new PlatformData(getPos(), camoState);
	}

	public static class PlatformData {
		public final BlockPos pos;

		@Nullable
		public final BlockState state;

		public PlatformData(BlockPos pos, @Nullable BlockState state) {
			this.pos = pos.toImmutable();
			this.state = state;
		}
	}
}
