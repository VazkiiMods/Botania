/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:24:51 PM (GMT)]
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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TilePlatform extends TileMod {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.PLATFORM)
	public static TileEntityType<TilePlatform> TYPE;
	private static final String TAG_CAMO = "camo";

	public BlockState camoState;

	public TilePlatform() {
		super(TYPE);
	}

	public boolean onWanded(PlayerEntity player) {
		if(player != null) {
			if(camoState == null || player.isSneaking())
				swapSelfAndPass(this, true);
			else swapSurroudings(this, false);
			return true;
		}

		return false;
	}

	private void swapSelfAndPass(TilePlatform tile, boolean empty) {
		swap(tile, empty);
		swapSurroudings(tile, empty);
	}

	private void swapSurroudings(TilePlatform tile, boolean empty) {
		for(Direction dir : Direction.values()) {
			BlockPos pos = tile.getPos().offset(dir);
			TileEntity tileAt = world.getTileEntity(pos);
			if(tileAt instanceof TilePlatform) {
				TilePlatform platform = (TilePlatform) tileAt;
				if(empty == (platform.camoState != null))
					swapSelfAndPass(platform, empty);
			}
		}
	}

	private void swap(TilePlatform tile, boolean empty) {
		tile.camoState = empty ? null : camoState;
		world.notifyBlockUpdate(tile.getPos(), world.getBlockState(tile.getPos()), world.getBlockState(tile.getPos()), 3);
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		if(camoState != null) {
			cmp.put(TAG_CAMO, NBTUtil.writeBlockState(camoState));
		}
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		camoState = NBTUtil.readBlockState(cmp.getCompound(TAG_CAMO));
		if(camoState.isAir()) {
			camoState = null;
		}
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
				.withInitial(BotaniaStateProps.HELD_POS, getPos())
				.withInitial(BotaniaStateProps.HELD_STATE, camoState)
				.build();
	}
}
