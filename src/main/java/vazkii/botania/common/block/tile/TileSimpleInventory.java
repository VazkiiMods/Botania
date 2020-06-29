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

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public abstract class TileSimpleInventory extends TileMod {

	private final Inventory itemHandler = createItemHandler();

	public TileSimpleInventory(TileEntityType<?> type) {
		super(type);
		itemHandler.addListener(i -> markDirty());
	}

	private static void copyToInv(NonNullList<ItemStack> src, IInventory dest) {
		Preconditions.checkArgument(src.size() == dest.getSizeInventory());
		for (int i = 0; i < src.size(); i++) {
			dest.setInventorySlotContents(i, src.get(i));
		}
	}

	private static NonNullList<ItemStack> copyFromInv(IInventory inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ret.set(i, inv.getStackInSlot(i));
		}
		return ret;
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		NonNullList<ItemStack> tmp = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag, tmp);
		copyToInv(tmp, itemHandler);
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		ItemStackHelper.saveAllItems(tag, copyFromInv(itemHandler));
	}

	public final int getSizeInventory() {
		return getItemHandler().getSizeInventory();
	}

	protected abstract Inventory createItemHandler();

	public final IInventory getItemHandler() {
		return itemHandler;
	}
}
