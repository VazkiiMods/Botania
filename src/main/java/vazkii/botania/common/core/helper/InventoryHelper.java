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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.mixin.AccessorHopperBlockEntity;

import javax.annotation.Nullable;

public class InventoryHelper {

	// [VanillaCopy] HopperBlockEntity#transfer but simulates instead of doing it
	public static ItemStack simulateTransfer(Container to, ItemStack stack, Direction side) {
		stack = stack.copy();

		if (to instanceof WorldlyContainer && side != null) {
			WorldlyContainer sidedInventory = (WorldlyContainer) to;
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
	private static ItemStack simulateTransfer(Container to, ItemStack stack, int slot, Direction direction) {
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
				player.getInventory().placeItemBackInInventory(player.level, copy);
				inv.getItemHandler().setItem(i, ItemStack.EMPTY);
				break;
			}
		}
	}

}
