/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.Direction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Set;
import java.util.stream.IntStream;

/**
 * Version of {@link TileSimpleInventory} where the backing inventory is exposed to automation
 */
public abstract class TileExposedSimpleInventory extends TileSimpleInventory implements SidedInventory {
	private final Lazy<int[]> slots = new Lazy<>(() -> IntStream.range(0, size()).toArray());

	public TileExposedSimpleInventory(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public boolean isEmpty() {
		return getItemHandler().isEmpty();
	}

	@Override
	public int size() {
		return inventorySize();
	}

	@Override
	public ItemStack getStack(int index) {
		return getItemHandler().getStack(index);
	}

	@Override
	public ItemStack removeStack(int index, int count) {
		return getItemHandler().removeStack(index, count);
	}

	@Override
	public ItemStack removeStack(int index) {
		return getItemHandler().removeStack(index);
	}

	@Override
	public void setStack(int index, ItemStack stack) {
		getItemHandler().setStack(index, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return getItemHandler().canPlayerUse(player);
	}

	@Override
	public void clear() {
		getItemHandler().clear();
	}

	@Override
	public int getMaxCountPerStack() {
		return getItemHandler().getMaxCountPerStack();
	}

	@Override
	public void onOpen(PlayerEntity player) {
		getItemHandler().onOpen(player);
	}

	@Override
	public void onClose(PlayerEntity player) {
		getItemHandler().onClose(player);
	}

	@Override
	public boolean isValid(int index, ItemStack stack) {
		return getItemHandler().isValid(index, stack);
	}

	@Override
	public int count(Item item) {
		return getItemHandler().count(item);
	}

	@Override
	public boolean containsAny(Set<Item> set) {
		return getItemHandler().containsAny(set);
	}

	@Nonnull
	@Override
	public int[] getAvailableSlots(@Nonnull Direction side) {
		return slots.get();
	}

	@Override
	public boolean canInsert(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		if (isValid(index, stack)) {
			// Vanilla hoppers do not check the inventory's stack limit, so do so here.
			// We don't have to check anything else like stackability because the hopper logic will do it
			ItemStack existing = getStack(index);
			return existing.isEmpty() || existing.getCount() + stack.getCount() <= getMaxCountPerStack();
		}

		return false;
	}

	@Override
	public boolean canExtract(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return true;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new SidedInvWrapper(this, side)));
	}
}
