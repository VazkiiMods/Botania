/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:21:28 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import vazkii.botania.api.state.BotaniaStateProps;

import javax.annotation.Nonnull;

public class TileCamo extends TileMod {

	private static final String TAG_CAMO = "camo";

	public BlockState camoState;

	public TileCamo(TileEntityType<?> type) {
		super(type);
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
		world.markForRerender(pos);
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
