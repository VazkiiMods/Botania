/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 2, 2014, 6:31:19 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRuneAltar extends TileSimpleInventory implements ISidedInventory {

	public void addItem(EntityPlayer player, ItemStack stack) {
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) == null) {
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				setInventorySlotContents(i, stackToAdd);
				
				stack.stackSize--;
				if(stack.stackSize == 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
	}
	
	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	public String getInvName() {
		return LibBlockNames.RUNE_ALTAR;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int accessibleSlot = -1;
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) != null)
				accessibleSlot = i;

		return accessibleSlot == -1 ? new int[0] : new int[] { accessibleSlot };
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

}
