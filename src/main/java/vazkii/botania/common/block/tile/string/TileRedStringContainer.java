/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 14, 2014, 5:26:39 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRedStringContainer extends TileRedString implements ISidedInventory {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		if(tile != null && tile instanceof IInventory) {
			IInventory inv = (IInventory) tile;
			if(inv instanceof ISidedInventory) {
				ISidedInventory sidedInv = (ISidedInventory) inv;
				for(int i = 0; i < 6; i++)
					if(sidedInv.getAccessibleSlotsFromSide(i).length != 0)
						return true;
				return false;
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public int getSizeInventory() {
		IInventory inv = getInventory();
		return inv != null ? inv.getSizeInventory() : 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		IInventory inv = getInventory();
		return inv != null ? inv.getStackInSlot(slot) : null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		IInventory inv = getInventory();
		return inv != null ? inv.decrStackSize(slot, count) : null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		IInventory inv = getInventory();
		return inv != null ? inv.getStackInSlotOnClosing(slot) : null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		IInventory inv = getInventory();
		if(inv != null)
			inv.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		IInventory inv = getInventory();
		return inv != null ? inv.getInventoryName() : LibBlockNames.RED_STRING_CONTAINER;
	}

	@Override
	public boolean hasCustomInventoryName() {
		IInventory inv = getInventory();
		return inv != null ? inv.hasCustomInventoryName() : false;
	}

	@Override
	public int getInventoryStackLimit() {
		IInventory inv = getInventory();
		return inv != null ? inv.getInventoryStackLimit() : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		IInventory inv = getInventory();
		return inv != null ? inv.isUseableByPlayer(player) : false;
	}

	@Override
	public void openInventory() {
		IInventory inv = getInventory();
		if(inv != null)
			inv.openInventory();
	}

	@Override
	public void closeInventory() {
		IInventory inv = getInventory();
		if(inv != null)
			inv.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		IInventory inv = getInventory();
		return inv != null ? inv.isItemValidForSlot(slot, stack) : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		IInventory inv = getInventory();
		return inv instanceof ISidedInventory ? ((ISidedInventory) inv).getAccessibleSlotsFromSide(side) : inv instanceof IInventory ? InventoryHelper.buildSlotsForLinearInventory(inv) : new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		IInventory inv = getInventory();
		return inv instanceof ISidedInventory ? ((ISidedInventory) inv).canInsertItem(slot, stack, side) : true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		IInventory inv = getInventory();
		return inv instanceof ISidedInventory ? ((ISidedInventory) inv).canExtractItem(slot, stack, side) : true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		TileEntity tile = getTileAtBinding();
		if(tile != null)
			tile.markDirty();
	}

	IInventory getInventory() {
		TileEntity tile = getTileAtBinding();
		if(tile == null || !(tile instanceof IInventory))
			return null;

		return InventoryHelper.getInventory((IInventory) tile);
	}

}
