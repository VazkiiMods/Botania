/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 20, 2014, 5:54:39 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.InventoryHelper;

public class SubTileHopperhock extends SubTileFunctional {

	int filterType = 0;

	private static final ForgeDirection[] VALID_DIRS = new ForgeDirection[] {
		ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST
	};

	@Override
	public void onUpdate() {
		super.onUpdate();

		boolean pulledAny = false;
		int range = mana > 0 ? 6 : 3; 

		int x = supertile.xCoord;
		int y = supertile.yCoord;
		int z = supertile.zCoord;

		List<EntityItem> items = supertile.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - range, y - 3, z - range, x + range + 1, y + 3, z + range + 1));
		for(EntityItem item : items) {
			if(item.age < 60)
				continue;

			ItemStack stack = item.getEntityItem();

			IInventory invToPutItemIn = null;
			ForgeDirection sideToPutItemIn = ForgeDirection.UNKNOWN;
			boolean priorityInv = false;

			for(ForgeDirection dir : VALID_DIRS) {
				int x_ = x + dir.offsetX;
				int y_ = y + dir.offsetY;
				int z_ = z + dir.offsetZ;

				IInventory inv = InventoryHelper.getInventory(supertile.worldObj, x_, y_, z_);
				if(inv != null) {
					List<ItemStack> filter = getFilterForInventory(inv, x_, y_, z_, true);
					boolean canAccept = canAcceptItem(stack, filter, filterType);
					int stackSize = InventoryHelper.testInventoryInsertion(inv, stack, dir);
					canAccept &= stackSize == stack.stackSize;

					if(canAccept) {
						boolean priority = !filter.isEmpty();
						
						setInv : {
							if(priorityInv && !priority)
								break setInv;
							
							invToPutItemIn = inv;
							priorityInv = priority;
							sideToPutItemIn = dir.getOpposite();
						}
					}
				}
			}

			if(invToPutItemIn != null) {
				InventoryHelper.insertItemIntoInventory(invToPutItemIn, stack.copy(), sideToPutItemIn, -1);
				if(!supertile.worldObj.isRemote)
					item.setDead();
				pulledAny = true;
				break;
			}
		}

		if(pulledAny && mana > 1)
			mana--;
	}

	public boolean canAcceptItem(ItemStack stack, List<ItemStack> filter, int filterType) {
		if(stack == null)
			return false;

		if(filter.isEmpty())
			return true;

		switch(filterType) {
		case 0 : { // Accept items in frames only
			for(ItemStack filterEntry : filter) {
				if(filterEntry == null)
					continue;

				if(stack.isItemEqual(filterEntry) && ItemStack.areItemStackTagsEqual(filterEntry, stack))
					return true;
			}

			return false;
		}
		case 1 : return !canAcceptItem(stack, filter, 0); // Accept items not in frames only
		default : return true; // Accept all items
		}
	}

	public List<ItemStack> getFilterForInventory(IInventory inv, int x, int y, int z, boolean recursiveForDoubleChests) {
		List<ItemStack> filter = new ArrayList();

		if(recursiveForDoubleChests) {
			TileEntity tileEntity = supertile.worldObj.getBlockTileEntity(x, y, z);
			int chestId = supertile.worldObj.getBlockId(x, y, z);

			if(tileEntity instanceof TileEntityChest)
				for(ForgeDirection dir : VALID_DIRS)
					if(supertile.worldObj.getBlockId(x + dir.offsetX, y, z + dir.offsetZ) == chestId) {
						filter.addAll(getFilterForInventory((IInventory) supertile.worldObj.getBlockTileEntity(x + dir.offsetX, y, z + dir.offsetZ), x + dir.offsetX, y, z + dir.offsetZ, false));
						break;
					}
		}

		final int[] orientationToDir = new int[] {
				3, 4, 2, 5
		};

		for(ForgeDirection dir : VALID_DIRS) {
			List<EntityItemFrame> frames = supertile.worldObj.getEntitiesWithinAABB(EntityItemFrame.class, AxisAlignedBB.getBoundingBox(x+ dir.offsetX, y + dir.offsetY, z + dir.offsetZ, x + dir.offsetX + 1, y + dir.offsetY + 1, z + dir.offsetZ + 1));
			for(EntityItemFrame frame : frames) {
				int orientation = frame.hangingDirection;
				if(orientationToDir[orientation] == dir.ordinal())
					filter.add(frame.getDisplayedItem());
			}
		}

		return filter;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public int getColor() {
		return 0x3F3F3F;
	}
}
