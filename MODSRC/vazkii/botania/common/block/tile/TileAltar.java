/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 21, 2014, 7:51:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class TileAltar extends TileSimpleInventory {

	public static final String TAG_HAS_WATER = "hasWater";
	
	public boolean hasWater = false;
	
	public boolean collideEntityItem(EntityItem item) {
		if(!hasWater)
			return false;

		boolean didChange = false;

		ItemStack stack = item.getEntityItem();
		if(stack.itemID == ModItems.petal.itemID) {
			if(getStackInSlot(getSizeInventory() - 1) != null)
				return false;
			
			stack.stackSize--;
			if(stack.stackSize == 0)
				item.setDead();

			for(int i = 0; i < getSizeInventory(); i++)
				if(getStackInSlot(i) == null) {
					setInventorySlotContents(i, new ItemStack(ModItems.petal.itemID, 1, stack.getItemDamage()));
					didChange = true;
					break;
				}
		}

		return didChange;
	}
	
	@Override
	public void updateEntity() {
		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + (1D / 16D * 20D), zCoord, xCoord + 1, yCoord + (1D / 16D * 21D), zCoord + 1));
		
		boolean didChange = false;
		
		for(EntityItem item : items)
			didChange = collideEntityItem(item) || didChange;
		
		if(didChange)
			PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		
		cmp.setBoolean(TAG_HAS_WATER, hasWater);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		
		hasWater = cmp.getBoolean(TAG_HAS_WATER);
	}

	@Override
	public String getInvName() {
		return LibBlockNames.ALTAR;
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	
}
