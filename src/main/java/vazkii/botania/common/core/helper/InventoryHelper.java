/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.mixin.AccessorHopperBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.function.Function;

public class InventoryHelper {

	// [VanillaCopy] HopperBlockEntity#transfer but simulates instead of doing it
	public static ItemStack simulateTransfer(Container to, ItemStack stack, @Nullable Direction side) {
		stack = stack.copy();

		if (to instanceof WorldlyContainer sidedInventory && side != null) {
			int[] is = sidedInventory.getSlotsForFace(side);

			for (int i = 0; i < is.length && !stack.isEmpty(); ++i) {
				stack = simulateTransfer(to, stack, is[i], side);
			}
		} else {
			int j = to.getContainerSize();

			for (int k = 0; k < j && !stack.isEmpty(); ++k) {
				stack = simulateTransfer(to, stack, k, side);
			}
		}

		return stack;
	}

	// [VanillaCopy] HopperBlockEntity without modifying the destination inventory. `stack` is still modified
	private static ItemStack simulateTransfer(Container to, ItemStack stack, int slot, @Nullable Direction direction) {
		ItemStack itemStack = to.getItem(slot);
		if (AccessorHopperBlockEntity.botania_canInsert(to, stack, slot, direction)) {
			boolean bl = false;
			boolean bl2 = to.isEmpty();
			if (itemStack.isEmpty()) {
				// to.setStack(slot, stack);
				stack = ItemStack.EMPTY;
				bl = true;
			} else if (AccessorHopperBlockEntity.botania_canMerge(itemStack, stack)) {
				int i = stack.getMaxStackSize() - itemStack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.shrink(j);
				// itemStack.increment(j);
				bl = j > 0;
			}

			/*
			if (bl) {
				if (bl2 && to instanceof HopperBlockEntity) {
					HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)to;
					if (!hopperBlockEntity.isDisabled()) {
						int k = 0;
						if (from instanceof HopperBlockEntity) {
							HopperBlockEntity hopperBlockEntity2 = (HopperBlockEntity)from;
							if (hopperBlockEntity.lastTickTime >= hopperBlockEntity2.lastTickTime) {
								k = 1;
							}
						}
			
						hopperBlockEntity.setCooldown(8 - k);
					}
				}
			
				to.markDirty();
			}
			*/
		}

		return stack;
	}

	@Nullable
	public static Container getInventory(Level world, BlockPos pos, Direction side) {
		Container ret = HopperBlockEntity.getContainerAt(world, pos);
		if (ret instanceof Entity) {
			return null;
		}
		return ret;
	}

	public static void withdrawFromInventory(TileSimpleInventory inv, Player player) {
		for (int i = inv.inventorySize() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getItemHandler().getItem(i);
			if (!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				player.getInventory().placeItemBackInInventory(copy);
				inv.getItemHandler().setItem(i, ItemStack.EMPTY);
				break;
			}
		}
	}

	public static boolean overrideStackedOnOther(
			Function<ItemStack, Container> inventoryGetter,
			boolean selfGuiOpen,
			@Nonnull ItemStack container, @Nonnull Slot slot,
			@Nonnull ClickAction clickAction, @Nonnull Player player) {
		if (!selfGuiOpen && clickAction == ClickAction.SECONDARY) {
			ItemStack toInsert = slot.getItem();
			var inventory = inventoryGetter.apply(container);
			if (simulateTransfer(inventory, toInsert, null).isEmpty()) {
				ItemStack taken = slot.safeTake(toInsert.getCount(), Integer.MAX_VALUE, player);
				HopperBlockEntity.addItem(null, inventory, taken, null);
				return true;
			}
		}
		return false;
	}

	public static boolean overrideOtherStackedOnMe(
			Function<ItemStack, Container> inventoryGetter,
			boolean selfGuiOpen,
			@Nonnull ItemStack container, @Nonnull ItemStack toInsert,
			@Nonnull ClickAction clickAction, @Nonnull SlotAccess cursorAccess) {
		if (!selfGuiOpen && clickAction == ClickAction.SECONDARY) {
			var inventory = inventoryGetter.apply(container);
			if (simulateTransfer(inventory, toInsert, null).isEmpty()) {
				HopperBlockEntity.addItem(null, inventory, toInsert, null);
				cursorAccess.set(ItemStack.EMPTY);
				return true;
			}
		}
		return false;
	}

}
