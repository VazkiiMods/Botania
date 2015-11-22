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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRedStringContainer extends TileRedString implements ISidedInventory {

	@Override
	public boolean acceptBlock(BlockPos pos) {
		TileEntity inv = worldObj.getTileEntity(pos);
		return inv != null && inv instanceof IInventory;
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
	public String getCommandSenderName() {
		IInventory inv = getInventory();
		return inv != null ? inv.getCommandSenderName() : LibBlockNames.RED_STRING_CONTAINER;
	}

	@Override
	public boolean hasCustomName() {
		IInventory inv = getInventory();
		return inv != null ? inv.hasCustomName() : false;
	}

	@Override
	public IChatComponent getDisplayName() {
		IInventory inv = getInventory();
		return inv != null ? inv.getDisplayName() : new ChatComponentText(getCommandSenderName());
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
	public void openInventory(EntityPlayer player) {
		IInventory inv = getInventory();
		if(inv != null)
			inv.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		IInventory inv = getInventory();
		if(inv != null)
			inv.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		IInventory inv = getInventory();
		return inv != null ? inv.isItemValidForSlot(slot, stack) : false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		IInventory inv = getInventory();
		return inv instanceof ISidedInventory ? ((ISidedInventory) inv).getSlotsForFace(side) : inv instanceof IInventory ? InventoryHelper.buildSlotsForLinearInventory(inv) : new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		IInventory inv = getInventory();
		return inv instanceof ISidedInventory ? ((ISidedInventory) inv).canInsertItem(slot, stack, side) : true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
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
