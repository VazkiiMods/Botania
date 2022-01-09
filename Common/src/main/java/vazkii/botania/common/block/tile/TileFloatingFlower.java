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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.FloatingFlowerImpl;
import vazkii.botania.api.block.IFloatingFlower;
import vazkii.botania.api.block.IFloatingFlowerProvider;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;

public class TileFloatingFlower extends TileMod implements IFloatingFlowerProvider {
	private static final String TAG_FLOATING_DATA = "floating";
	private final IFloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			Block b = getBlockState().getBlock();
			if (b instanceof BlockFloatingFlower) {
				return new ItemStack(ModBlocks.getShinyFlower(((BlockFloatingFlower) b).color));
			} else {
				return ItemStack.EMPTY;
			}
		}
	};

	public TileFloatingFlower(BlockPos pos, BlockState state) {
		super(ModTiles.MINI_ISLAND, pos, state);
	}

	@Override
	public IFloatingFlower getFloatingData() {
		return floatingData;
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		cmp.put(TAG_FLOATING_DATA, floatingData.writeNBT());
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		floatingData.readNBT(cmp.getCompound(TAG_FLOATING_DATA));
		if (oldType != floatingData.getIslandType() && level != null && level.isClientSide) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	// [SoftImplement] RenderAttachBlockEntity
	public IFloatingFlower.IslandType getRenderAttachmentData() {
		return floatingData.getIslandType();
	}
}
