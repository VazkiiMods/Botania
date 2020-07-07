/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;

/**
 * Version of {@link TileSimpleInventory} where the backing inventory is exposed to automation
 */
public abstract class TileExposedSimpleInventory extends TileSimpleInventory implements IInventory {
	public TileExposedSimpleInventory(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean isEmpty() {
		return getItemHandler().isEmpty();
	}

	@Override
	public int getSizeInventory() {
		return inventorySize();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return getItemHandler().getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return getItemHandler().decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return getItemHandler().removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		getItemHandler().setInventorySlotContents(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return getItemHandler().isUsableByPlayer(player);
	}

	@Override
	public void clear() {
		getItemHandler().clear();
	}
}
