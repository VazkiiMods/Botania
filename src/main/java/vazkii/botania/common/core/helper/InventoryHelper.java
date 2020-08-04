/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nullable;

public class InventoryHelper {

	@Nullable
	public static InvWithLocation getInventoryWithLocation(World world, BlockPos pos, Direction side) {
		IItemHandler ret = getInventory(world, pos, side);
		if (ret == null) {
			return null;
		} else {
			return new InvWithLocation(ret, world, pos);
		}
	}

	@Nullable
	public static IItemHandler getInventory(World world, BlockPos pos, Direction side) {
		BlockEntity te = world.getBlockEntity(pos);

		if (te == null) {
			return null;
		}

		LazyOptional<IItemHandler> ret = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		if (!ret.isPresent()) {
			ret = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		return ret.orElse(null);
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
