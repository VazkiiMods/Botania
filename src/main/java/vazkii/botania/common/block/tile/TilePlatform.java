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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TilePlatform extends TileMod {
	public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();
	public static final ModelProperty<BlockPos> HELD_POS = new ModelProperty<>();

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
			world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
			if (!world.isRemote) {
				world.func_230547_a_(pos, getBlockState().getBlock());
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
			TileEntity tileAt = world.getTileEntity(pos);
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
	public void writePacketNBT(CompoundNBT cmp) {
		if (getCamoState() != null) {
			cmp.put(TAG_CAMO, NBTUtil.writeBlockState(getCamoState()));
		}
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		BlockState state = NBTUtil.readBlockState(cmp.getCompound(TAG_CAMO));
		if (state.isAir()) {
			state = null;
		}
		setCamoState(state);
	}

	@Override
	public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
		super.onDataPacket(manager, packet);
		requestModelDataUpdate();
		if (world instanceof ClientWorld) {
			world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@Nonnull
	@Override
	public IModelData getModelData() {
		return new ModelDataMap.Builder()
				.withInitial(HELD_POS, getPos())
				.withInitial(HELD_STATE, getCamoState())
				.build();
	}
}
