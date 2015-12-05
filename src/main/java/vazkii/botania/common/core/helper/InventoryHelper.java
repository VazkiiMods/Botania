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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

//From OpenBlocksLib: https://github.com/OpenMods/OpenModsLib
public class InventoryHelper {

	public static void tryInsertStack(IInventory targetInventory, int slot, ItemStack stack, boolean canMerge) {
		if(targetInventory.isItemValidForSlot(slot, stack)) {
			ItemStack targetStack = targetInventory.getStackInSlot(slot);
			if(targetStack == null) {
				int space = targetInventory.getInventoryStackLimit();
				int mergeAmount = Math.min(space, stack.stackSize);

				ItemStack copy = stack.copy();
				copy.stackSize = mergeAmount;
				targetInventory.setInventorySlotContents(slot, copy);
				stack.stackSize -= mergeAmount;
			} else if(canMerge) {
				if(targetInventory.isItemValidForSlot(slot, stack) && areMergeCandidates(stack, targetStack)) {
					int space = Math.min(targetInventory.getInventoryStackLimit(), targetStack.getMaxStackSize()) - targetStack.stackSize;
					int mergeAmount = Math.min(space, stack.stackSize);

					ItemStack copy = targetStack.copy();
					copy.stackSize += mergeAmount;
					targetInventory.setInventorySlotContents(slot, copy);
					stack.stackSize -= mergeAmount;
				}
			}
		}
	}

	protected static boolean areMergeCandidates(ItemStack source, ItemStack target) {
		return source.isItemEqual(target) && ItemStack.areItemStackTagsEqual(source, target) && target.stackSize < target.getMaxStackSize();
	}

	public static void insertItemIntoInventory(IInventory inventory, ItemStack stack) {
		insertItemIntoInventory(inventory, stack, ForgeDirection.UNKNOWN, -1);
	}

	public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot) {
		insertItemIntoInventory(inventory, stack, side, intoSlot, true);
	}

	public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove) {
		insertItemIntoInventory(inventory, stack, side, intoSlot, doMove, true);
	}

	public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove, boolean canStack) {
		if(stack == null) return;

		IInventory targetInventory = inventory;

		if(!doMove) {
			targetInventory = new GenericInventory("temporary.inventory", false, targetInventory.getSizeInventory());
			((GenericInventory)targetInventory).copyFrom(inventory);
		}

		int i = 0;
		int[] attemptSlots;

		if(inventory instanceof ISidedInventory && side != ForgeDirection.UNKNOWN) {
			attemptSlots = ((ISidedInventory)inventory).getAccessibleSlotsFromSide(side.ordinal());
			if(attemptSlots == null)
				attemptSlots = new int[0];
		} else {
			attemptSlots = new int[inventory.getSizeInventory()];
			for(int a = 0; a < inventory.getSizeInventory(); a++)
				attemptSlots[a] = a;
		}
		if(intoSlot > -1) {
			Set<Integer> x = new HashSet<Integer>();
			for(int attemptedSlot : attemptSlots)
				x.add(attemptedSlot);

			if(x.contains(intoSlot))
				attemptSlots = new int[] { intoSlot };
			else attemptSlots = new int[0];
		}
		while(stack.stackSize > 0 && i < attemptSlots.length) {
			if(side != ForgeDirection.UNKNOWN && inventory instanceof ISidedInventory)
				if(!((ISidedInventory)inventory).canInsertItem(attemptSlots[i], stack, side.ordinal())) {
					i++;
					continue;
				}

			tryInsertStack(targetInventory, attemptSlots[i], stack, canStack);
			i++;
		}
	}

	public static int testInventoryInsertion(IInventory inventory, ItemStack item, ForgeDirection side) {
		if(item == null || item.stackSize == 0)
			return 0;
		item = item.copy();

		if(inventory == null)
			return 0;

		inventory.getSizeInventory();

		int itemSizeCounter = item.stackSize;
		int[] availableSlots;

		if(inventory instanceof ISidedInventory)
			availableSlots = ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side.ordinal());
		else {
			availableSlots = buildSlotsForLinearInventory(inventory);
		}

		for(int i : availableSlots) {
			if(itemSizeCounter <= 0)
				break;

			if (!inventory.isItemValidForSlot(i, item))
				continue;

			if(side != ForgeDirection.UNKNOWN && inventory instanceof ISidedInventory)
				if(!((ISidedInventory)inventory).canInsertItem(i, item, side.ordinal()))
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

	public static IInventory getInventory(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityChest) {
			Block chestBlock = world.getBlock(x, y, z);
			if(world.getBlock(x - 1, y, z) == chestBlock)
				return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x - 1, y, z), (IInventory)tileEntity);
			if(world.getBlock(x + 1, y, z) == chestBlock)
				return new InventoryLargeChest("Large chest", (IInventory)tileEntity, (IInventory)world.getTileEntity(x + 1, y, z));
			if(world.getBlock(x, y, z - 1) == chestBlock)
				return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x, y, z - 1), (IInventory)tileEntity);
			if(world.getBlock(x, y, z + 1) == chestBlock)
				return new InventoryLargeChest("Large chest", (IInventory)tileEntity, (IInventory)world.getTileEntity(x, y, z + 1));
		}
		return tileEntity instanceof IInventory ? (IInventory)tileEntity : null;
	}

	public static IInventory getInventory(World world, int x, int y, int z, ForgeDirection direction) {
		if(direction != null && direction != ForgeDirection.UNKNOWN) {
			x += direction.offsetX;
			y += direction.offsetY;
			z += direction.offsetZ;
		}
		return getInventory(world, x, y, z);

	}

	public static IInventory getInventory(IInventory inventory) {
		if(inventory instanceof TileEntityChest) {
			TileEntity te = (TileEntity)inventory;
			return getInventory(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
		}
		return inventory;
	}

	public static int[] buildSlotsForLinearInventory(IInventory inv) {
		int[] slots = new int[inv.getSizeInventory()];
		for (int i = 0; i < slots.length; i++)
			slots[i] = i;

		return slots;
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
		public ItemStack getStackInSlotOnClosing(int i) {
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
		public String getInventoryName() {
			return null;
		}

		@Override
		public boolean hasCustomInventoryName() {
			return false;
		}

		@Override
		public void markDirty() { }

		@Override
		public void openInventory() { }

		@Override
		public void closeInventory() { }
	}
}
