/**
 * This class was created by <Mikeemoo/boq/nevercast>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;
import vazkii.botania.api.corporea.InvWithLocation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//From OpenBlocksLib: https://github.com/OpenMods/OpenModsLib
public class InventoryHelper {

	protected static boolean areMergeCandidates(ItemStack source, ItemStack target) {
		return source.isItemEqual(target) && ItemStack.areItemStackTagsEqual(source, target) && target.stackSize < target.getMaxStackSize();
	}

	public static int testInventoryInsertion(IInventory inventory, ItemStack item, EnumFacing side) {
		if(item == null || item.stackSize == 0)
			return 0;
		item = item.copy();

		if(inventory == null)
			return 0;

		inventory.getSizeInventory();

		int itemSizeCounter = item.stackSize;
		int[] availableSlots;

		if(inventory instanceof ISidedInventory)
			availableSlots = ((ISidedInventory) inventory).getSlotsForFace(side);
		else {
			availableSlots = buildSlotsForLinearInventory(inventory);
		}

		for(int i : availableSlots) {
			if(itemSizeCounter <= 0)
				break;

			if (!inventory.isItemValidForSlot(i, item))
				continue;

			if(side != null && inventory instanceof ISidedInventory)
				if(!((ISidedInventory)inventory).canInsertItem(i, item, side))
					continue;

			ItemStack inventorySlot = inventory.getStackInSlot(i);
			if(inventorySlot == null)
				itemSizeCounter -= Math.min(Math.min(itemSizeCounter, inventory.getInventoryStackLimit()), item.getMaxStackSize());
			else if(areMergeCandidates(item, inventorySlot)) {
				int space = Math.min(inventory.getInventoryStackLimit(), inventorySlot.getMaxStackSize()) - inventorySlot.stackSize;
				itemSizeCounter -= Math.min(itemSizeCounter, space);
			}
		}

		if(itemSizeCounter != item.stackSize) {
			itemSizeCounter = Math.max(itemSizeCounter, 0);
			return item.stackSize - itemSizeCounter;
		}

		return 0;
	}

	public static int[] buildSlotsForLinearInventory(IInventory inv) {
		int[] slots = new int[inv.getSizeInventory()];
		for (int i = 0; i < slots.length; i++)
			slots[i] = i;

		return slots;
	}

	public static InvWithLocation getInventoryWithLocation(World world, BlockPos pos, EnumFacing side) {
        IItemHandler ret = getInventory(world, pos, side);
        if(ret == null)
            return null;
        else return new InvWithLocation(ret, world, pos);
    }

	public static IItemHandler getInventory(World world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);

        if(te == null)
            return null;
        IItemHandler ret = te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) ?
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : null;
        if(te instanceof TileEntityChest)
            ret = VanillaDoubleChestItemHandler.get(((TileEntityChest) te));
        if(ret == null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            ret = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);


        return ret;
    }

	public static Iterable<ItemStack> toIterable(IItemHandler itemHandler) {
        return () -> new Iterator<ItemStack>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < itemHandler.getSlots();
            }

            @Override
            public ItemStack next() {
                return itemHandler.getStackInSlot(index++);
            }
        };
    }

	public static class GenericInventory implements IInventory {

		protected String inventoryTitle;
		protected int slotsCount;
		protected ItemStack[] inventoryContents;
		protected boolean isInvNameLocalized;

		public GenericInventory(String name, boolean isInvNameLocalized, int size) {
			this.isInvNameLocalized = isInvNameLocalized;
			slotsCount = size;
			inventoryTitle = name;
			inventoryContents = new ItemStack[size];
		}

		@Override
		public ItemStack decrStackSize(int par1, int par2) {
			if(inventoryContents[par1] != null) {
				ItemStack itemstack;

				if(inventoryContents[par1].stackSize <= par2) {
					itemstack = inventoryContents[par1];
					inventoryContents[par1] = null;
					return itemstack;
				}

				itemstack = inventoryContents[par1].splitStack(par2);
				if(inventoryContents[par1].stackSize == 0)
					inventoryContents[par1] = null;

				return itemstack;
			}
			return null;
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public int getSizeInventory() {
			return slotsCount;
		}

		@Override
		public ItemStack getStackInSlot(int i) {
			return inventoryContents[i];
		}

		public ItemStack getStackInSlot(Enum<?> i) {
			return getStackInSlot(i.ordinal());
		}

		@Override
		public ItemStack removeStackFromSlot(int i) {
			if(i >= inventoryContents.length)
				return null;

			if(inventoryContents[i] != null) {
				ItemStack itemstack = inventoryContents[i];
				inventoryContents[i] = null;
				return itemstack;
			}

			return null;
		}

		public boolean isItem(int slot, Item item) {
			return inventoryContents[slot] != null && inventoryContents[slot].getItem() == item;
		}

		@Override
		public boolean isItemValidForSlot(int i, ItemStack itemstack) {
			return true;
		}

		@Override
		public int getField(int id) { return 0; }

		@Override
		public void setField(int id, int value) { }

		@Override
		public int getFieldCount() { return 0; }

		@Override
		public void clear() {
			Arrays.fill(inventoryContents, null);
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer entityplayer) {
			return true;
		}

		public void clearAndSetSlotCount(int amount) {
			slotsCount = amount;
			inventoryContents = new ItemStack[amount];
		}

		public void readFromNBT(NBTTagCompound tag) {
			if(tag.hasKey("size"))
				slotsCount = tag.getInteger("size");

			NBTTagList nbttaglist = tag.getTagList("Items", 10);
			inventoryContents = new ItemStack[slotsCount];
			for(int i = 0; i < nbttaglist.tagCount(); i++) {
				NBTTagCompound stacktag = nbttaglist.getCompoundTagAt(i);
				int j = stacktag.getByte("Slot");
				if(j >= 0 && j < inventoryContents.length)
					inventoryContents[j] = ItemStack.loadItemStackFromNBT(stacktag);
			}
		}

		@Override
		public void setInventorySlotContents(int i, ItemStack itemstack) {
			inventoryContents[i] = itemstack;

			if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
				itemstack.stackSize = getInventoryStackLimit();
		}

		public void writeToNBT(NBTTagCompound tag) {
			tag.setInteger("size", getSizeInventory());
			NBTTagList nbttaglist = new NBTTagList();
			for(int i = 0; i < inventoryContents.length; i++) {
				if(inventoryContents[i] != null) {
					NBTTagCompound stacktag = new NBTTagCompound();
					stacktag.setByte("Slot", (byte)i);
					inventoryContents[i].writeToNBT(stacktag);
					nbttaglist.appendTag(stacktag);
				}
			}
			tag.setTag("Items", nbttaglist);
		}

		public void copyFrom(IInventory inventory) {
			for(int i = 0; i < inventory.getSizeInventory(); i++)
				if(i < getSizeInventory()) {
					ItemStack stack = inventory.getStackInSlot(i);
					if(stack != null)
						setInventorySlotContents(i, stack.copy());
					else setInventorySlotContents(i, null);
				}
		}

		public List<ItemStack> contents() {
			return Arrays.asList(inventoryContents);
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public boolean hasCustomName() {
			return false;
		}

		@Override
		public ITextComponent getDisplayName() {
			return new TextComponentString(getName());
		}

		@Override
		public void markDirty() { }

		@Override
		public void openInventory(EntityPlayer player) { }

		@Override
		public void closeInventory(EntityPlayer player) { }
	}
}
