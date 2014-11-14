/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Nov 14, 2014, 5:26:39 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileRedStringContainer extends TileRedString implements ISidedInventory {

	@Override
	public boolean acceptBlock(int x, int y, int z) {
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		return tile != null && tile instanceof IInventory;
	}

	@Override
	public int getSizeInventory() {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).getSizeInventory() : 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).getStackInSlot(slot) : null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).decrStackSize(slot, count) : null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).getStackInSlotOnClosing(slot) : null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		TileEntity tile = getTileAtBinding();
		if(tile instanceof IInventory)
			((IInventory) tile).setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).getInventoryName() : LibBlockNames.RED_STRING_CONTAINER;
	}

	@Override
	public boolean hasCustomInventoryName() {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).hasCustomInventoryName() : false;
	}

	@Override
	public int getInventoryStackLimit() {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).getInventoryStackLimit() : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).isUseableByPlayer(player) : false;
	}

	@Override
	public void openInventory() {
		TileEntity tile = getTileAtBinding();
		if(tile instanceof IInventory)
			((IInventory) tile).openInventory();	
	}

	@Override
	public void closeInventory() {
		TileEntity tile = getTileAtBinding();
		if(tile instanceof IInventory)
			((IInventory) tile).closeInventory();	
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof IInventory ? ((IInventory) tile).isItemValidForSlot(slot, stack) : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof ISidedInventory ? ((ISidedInventory) tile).getAccessibleSlotsFromSide(side) : tile instanceof IInventory ? InventoryHelper.buildSlotsForLinearInventory((IInventory) tile) : new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof ISidedInventory ? ((ISidedInventory) tile).canInsertItem(slot, stack, side) : true;	
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		TileEntity tile = getTileAtBinding();
		return tile instanceof ISidedInventory ? ((ISidedInventory) tile).canExtractItem(slot, stack, side) : true;		
	}

}
