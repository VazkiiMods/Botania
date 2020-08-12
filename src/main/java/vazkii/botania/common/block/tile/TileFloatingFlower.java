/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFloatingFlowerProvider;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileFloatingFlower extends TileMod implements IFloatingFlowerProvider {
	private static final String TAG_FLOATING_DATA = "floating";
	private final IFloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			Block b = getCachedState().getBlock();
			if (b instanceof BlockFloatingFlower) {
				return new ItemStack(ModBlocks.getShinyFlower(((BlockFloatingFlower) b).color));
			} else {
				return ItemStack.EMPTY;
			}
		}
	};

	public TileFloatingFlower() {
		super(ModTiles.MINI_ISLAND);
	}

	@Override
	public IFloatingFlower getFloatingData() {
		return floatingData;
	}

	@Override
	public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket packet) {
		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		super.onDataPacket(net, packet);
		if (oldType != floatingData.getIslandType()) {
			ModelDataManager.requestModelDataRefresh(this);
			world.updateListeners(getPos(), getCachedState(), getCachedState(), 0);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.put(TAG_FLOATING_DATA, floatingData.writeNBT());
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		floatingData.readNBT(cmp.getCompound(TAG_FLOATING_DATA));
	}

	@Nonnull
	@Override
	public IModelData getModelData() {
		return new ModelDataMap.Builder()
				.withInitial(BotaniaStateProps.FLOATING_DATA, floatingData)
				.build();
	}
}
