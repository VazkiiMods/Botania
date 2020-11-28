/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import com.google.common.base.Preconditions;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public abstract class TileSimpleInventory extends TileMod {

	private final SimpleInventory itemHandler = createItemHandler();

	public TileSimpleInventory(BlockEntityType<?> type) {
		super(type);
		itemHandler.addListener(i -> markDirty());
	}

	private static void copyToInv(DefaultedList<ItemStack> src, Inventory dest) {
		Preconditions.checkArgument(src.size() == dest.size());
		for (int i = 0; i < src.size(); i++) {
			dest.setStack(i, src.get(i));
		}
	}

	private static DefaultedList<ItemStack> copyFromInv(Inventory inv) {
		DefaultedList<ItemStack> ret = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
		for (int i = 0; i < inv.size(); i++) {
			ret.set(i, inv.getStack(i));
		}
		return ret;
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		DefaultedList<ItemStack> tmp = DefaultedList.ofSize(inventorySize(), ItemStack.EMPTY);
		Inventories.fromTag(tag, tmp);
		copyToInv(tmp, itemHandler);
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		Inventories.toTag(tag, copyFromInv(itemHandler));
	}

	// NB: Cannot be named the same as the corresponding method in vanilla's interface -- causes obf issues with MCP
	public final int inventorySize() {
		return getItemHandler().size();
	}

	protected abstract SimpleInventory createItemHandler();

	public final Inventory getItemHandler() {
		return itemHandler;
	}
}
