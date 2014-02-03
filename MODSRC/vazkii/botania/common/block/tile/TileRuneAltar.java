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
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRuneAltar extends TileSimpleInventory implements ISidedInventory {

	public float[] angles = new float[getSizeInventory()];
	boolean firstTick = true;
	
	public void addItem(EntityPlayer player, ItemStack stack) {
		boolean did = false;
		
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) == null) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				setInventorySlotContents(i, stackToAdd);
				
				if(!player.capabilities.isCreativeMode) {
					stack.stackSize--;
					if(stack.stackSize == 0)
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				break;
			}
		
		if(did) {
			Vector3 vec = Vector3.fromTileEntityCenter(this);
			Vector3 endVec = vec.copy().add(0, 2.5, 0);
			Botania.proxy.lightningFX(worldObj, vec, endVec, 2F, 0x00948B, 0x00E4D7);
		}
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(firstTick) {
			updateRotationAngles();
			firstTick = false;
		}
		
		for(int i = 0; i < angles.length; i++)
			angles[i]++;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		super.setInventorySlotContents(i, itemstack);
		updateRotationAngles();
	}
	
	public void updateRotationAngles() {
		int items = 0;
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) == null)
				break;
			else items++;
		
		float anglePer = 360F / (float) items;
		float totalAngle = 0F;
		for(int i = 0; i < angles.length; i++)
			angles[i] = (totalAngle += anglePer);
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
