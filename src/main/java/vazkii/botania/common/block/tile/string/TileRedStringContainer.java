/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import vazkii.botania.common.block.tile.ModTiles;

import javax.annotation.Nullable;

import java.util.Set;
import java.util.stream.IntStream;

public class TileRedStringContainer extends TileRedString implements SidedInventory {

	public TileRedStringContainer() {
		this(ModTiles.RED_STRING_CONTAINER);
	}

	public TileRedStringContainer(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		return tile instanceof Inventory;
	}

	@Nullable
	private Inventory getInventoryAtBinding() {
		return (Inventory) getTileAtBinding();
	}

	@Override
	public int size() {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.size() : 0;
	}

	@Override
	public boolean isEmpty() {
		Inventory inv = getInventoryAtBinding();
		return inv == null || inv.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.getStack(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.removeStack(slot, amount) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot) {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.removeStack(slot) : ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		Inventory inv = getInventoryAtBinding();
		if (inv != null) {
			inv.setStack(slot, stack);
		}
	}

	@Override
	public int getMaxCountPerStack() {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.getMaxCountPerStack() : 0;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		BlockEntity tile = getTileAtBinding();
		if (tile != null) {
			tile.markDirty();
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void onOpen(PlayerEntity player) {}

	@Override
	public void onClose(PlayerEntity player) {}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		Inventory inv = getInventoryAtBinding();
		return inv != null && inv.isValid(slot, stack);
	}

	@Override
	public int count(Item item) {
		Inventory inv = getInventoryAtBinding();
		return inv != null ? inv.count(item) : 0;
	}

	@Override
	public boolean containsAny(Set<Item> items) {
		Inventory inv = getInventoryAtBinding();
		return inv != null && inv.containsAny(items);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		Inventory inv = getInventoryAtBinding();
		if (inv instanceof SidedInventory) {
			return ((SidedInventory) inv).getAvailableSlots(side);
		} else if (inv != null) {
			return IntStream.range(0, inv.size()).toArray();
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		Inventory inv = getInventoryAtBinding();
		if (inv instanceof SidedInventory) {
			return ((SidedInventory) inv).canInsert(slot, stack, dir);
		} else {
			return inv != null;
		}
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		Inventory inv = getInventoryAtBinding();
		if (inv instanceof SidedInventory) {
			return ((SidedInventory) inv).canExtract(slot, stack, dir);
		} else {
			return inv != null;
		}
	}

	@Override
	public void clear() {
		Inventory inv = getInventoryAtBinding();
		if (inv != null) {
			inv.clear();
		}
	}
}
