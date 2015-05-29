/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 29, 2015, 8:21:17 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;

public class TileHourglass extends TileSimpleInventory {

	private static final String TAG_TIME = "time";
	int time = 0;
	public float timeFraction = 0F;
	public boolean flip = false;
	public int flipTicks = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		int totalTime = getTotalTime();
		if(totalTime > 0) {
			time++;
			if(time >= totalTime) {
				time = 0;
				flip = !flip;
				flipTicks = 4;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1 | 2);
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType(), getBlockType().tickRate(worldObj));
			}
			timeFraction = (float) time / (float) totalTime;
		} else {
			time = 0;
			timeFraction = 0F;
		}
		
		if(flipTicks > 0)
			flipTicks--;
	}
	
	public int getTotalTime() {
		ItemStack stack = getStackInSlot(0);
		if(stack == null)
			return 0;
		
		return getStackItemTime(stack) * stack.stackSize;
	}
	
	public static int getStackItemTime(ItemStack stack) {
		if(stack == null)
			return 0;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.sand))
			return stack.getItemDamage() == 1 ? 200 : 20;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.soul_sand))
			return 1200;
		return 0;
	}
	
	public int getColor() {
		ItemStack stack = getStackInSlot(0);
		if(stack == null)
			return 0;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.sand))
			return stack.getItemDamage() == 1 ? 0xE95800 : 0xFFEC49;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.soul_sand))
			return 0x5A412f;
		return 0;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_TIME, time);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);
		time = par1nbtTagCompound.getInteger(TAG_TIME);
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		time = 0;
		timeFraction = 0F;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.HOURGLASS;
	}

}
