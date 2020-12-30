/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.mixin.AccessorHopperBlockEntity;

import javax.annotation.Nullable;

public class InventoryHelper {

	// [VanillaCopy] HopperBlockEntity#transfer but simulates instead of doing it
	public static ItemStack simulateTransfer(Inventory to, ItemStack stack, Direction side) {
		stack = stack.copy();

		if (to instanceof SidedInventory && side != null) {
			SidedInventory sidedInventory = (SidedInventory) to;
			int[] is = sidedInventory.getAvailableSlots(side);

			for (int i = 0; i < is.length && !stack.isEmpty(); ++i) {
				stack = simulateTransfer(to, stack, is[i], side);
			}
		} else {
			int j = to.size();

			for (int k = 0; k < j && !stack.isEmpty(); ++k) {
				stack = simulateTransfer(to, stack, k, side);
			}
		}

		return stack;
	}

	// [VanillaCopy] HopperBlockEntity without modifying the destination inventory. `stack` is still modified
	private static ItemStack simulateTransfer(Inventory to, ItemStack stack, int slot, Direction direction) {
		ItemStack itemStack = to.getStack(slot);
		if (AccessorHopperBlockEntity.botania_canInsert(to, stack, slot, direction)) {
			boolean bl = false;
			boolean bl2 = to.isEmpty();
			if (itemStack.isEmpty()) {
				// to.setStack(slot, stack);
				stack = ItemStack.EMPTY;
				bl = true;
			} else if (AccessorHopperBlockEntity.botania_canMerge(itemStack, stack)) {
				int i = stack.getMaxCount() - itemStack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.decrement(j);
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
	public static Inventory getInventory(World world, BlockPos pos, Direction side) {
		Inventory ret = HopperBlockEntity.getInventoryAt(world, pos);
		if (ret instanceof Entity) {
			return null;
		}
		return ret;
	}

	public static void withdrawFromInventory(TileSimpleInventory inv, PlayerEntity player) {
		for (int i = inv.inventorySize() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getItemHandler().getStack(i);
			if (!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				player.inventory.offerOrDrop(player.world, copy);
				inv.getItemHandler().setStack(i, ItemStack.EMPTY);
				break;
			}
		}
	}

}
