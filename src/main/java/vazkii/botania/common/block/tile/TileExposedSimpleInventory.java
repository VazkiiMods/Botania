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
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Set;
import java.util.stream.IntStream;

/**
 * Version of {@link TileSimpleInventory} where the backing inventory is exposed to automation
 */
public abstract class TileExposedSimpleInventory extends TileSimpleInventory implements ISidedInventory {
	private final LazyValue<int[]> slots = new LazyValue<>(() -> IntStream.range(0, getSizeInventory()).toArray());

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

	@Override
	public int getInventoryStackLimit() {
		return getItemHandler().getInventoryStackLimit();
	}

	@Override
	public void openInventory(PlayerEntity player) {
		getItemHandler().openInventory(player);
	}

	@Override
	public void closeInventory(PlayerEntity player) {
		getItemHandler().closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return getItemHandler().isItemValidForSlot(index, stack);
	}

	@Override
	public int count(Item item) {
		return getItemHandler().count(item);
	}

	@Override
	public boolean hasAny(Set<Item> set) {
		return getItemHandler().hasAny(set);
	}

	@Nonnull
	@Override
	public int[] getSlotsForFace(@Nonnull Direction side) {
		return slots.getValue();
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		if (isItemValidForSlot(index, stack)) {
			// Vanilla hoppers do not check the inventory's stack limit, so do so here.
			// We don't have to check anything else like stackability because the hopper logic will do it
			ItemStack existing = getStackInSlot(index);
			return existing.isEmpty() || existing.getCount() + stack.getCount() <= getInventoryStackLimit();
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return true;
	}
}
