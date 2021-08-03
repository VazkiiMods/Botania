/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.common.block.tile.ModTiles;

import javax.annotation.Nullable;

import java.util.Set;
import java.util.stream.IntStream;

public class TileRedStringContainer extends TileRedString implements WorldlyContainer {

	public TileRedStringContainer() {
		this(ModTiles.RED_STRING_CONTAINER);
	}

	public TileRedStringContainer(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		BlockEntity tile = level.getBlockEntity(pos);
		return tile instanceof Container;
	}

	@Nullable
	private Container getInventoryAtBinding() {
		return (Container) getTileAtBinding();
	}

	@Override
	public int getContainerSize() {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.getContainerSize() : 0;
	}

	@Override
	public boolean isEmpty() {
		Container inv = getInventoryAtBinding();
		return inv == null || inv.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.getItem(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.removeItem(slot, amount) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.removeItemNoUpdate(slot) : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		Container inv = getInventoryAtBinding();
		if (inv != null) {
			inv.setItem(slot, stack);
		}
	}

	@Override
	public int getMaxStackSize() {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.getMaxStackSize() : 0;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		BlockEntity tile = getTileAtBinding();
		if (tile != null) {
			tile.setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void startOpen(Player player) {}

	@Override
	public void stopOpen(Player player) {}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		Container inv = getInventoryAtBinding();
		return inv != null && inv.canPlaceItem(slot, stack);
	}

	@Override
	public int countItem(Item item) {
		Container inv = getInventoryAtBinding();
		return inv != null ? inv.countItem(item) : 0;
	}

	@Override
	public boolean hasAnyOf(Set<Item> items) {
		Container inv = getInventoryAtBinding();
		return inv != null && inv.hasAnyOf(items);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		Container inv = getInventoryAtBinding();
		if (inv instanceof WorldlyContainer) {
			return ((WorldlyContainer) inv).getSlotsForFace(side);
		} else if (inv != null) {
			return IntStream.range(0, inv.getContainerSize()).toArray();
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		Container inv = getInventoryAtBinding();
		if (inv instanceof WorldlyContainer) {
			return ((WorldlyContainer) inv).canPlaceItemThroughFace(slot, stack, dir);
		} else {
			return inv != null;
		}
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		Container inv = getInventoryAtBinding();
		if (inv instanceof WorldlyContainer) {
			return ((WorldlyContainer) inv).canTakeItemThroughFace(slot, stack, dir);
		} else {
			return inv != null;
		}
	}

	@Override
	public void clearContent() {
		Container inv = getInventoryAtBinding();
		if (inv != null) {
			inv.clearContent();
		}
	}
}
