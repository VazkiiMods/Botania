/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 4, 2014, 12:51:05 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.lib.LibBlockNames;

public class TileOpenCrate extends TileSimpleInventory {

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.OPEN_CRATE;
	}

	@Override
	public void updateEntity() {
		boolean redstone = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
			if(redstoneSide > 0) {
				redstone = true;
				break;
			}
		}

		if(canEject()) {
			ItemStack stack = getStackInSlot(0);
			if(stack != null)
				eject(stack, redstone);
		}
	}

	public boolean canEject() {
		Block blockBelow = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		return blockBelow.isAir(worldObj, xCoord, yCoord - 1, zCoord) || blockBelow.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord - 1, zCoord) == null;
	}

	public void eject(ItemStack stack, boolean redstone) {
		EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord - 0.5, zCoord + 0.5, stack);
		item.motionX = 0;
		item.motionY = 0;
		item.motionZ = 0;

		if(redstone)
			item.age = -200;

		setInventorySlotContents(0, null);
		if(!worldObj.isRemote)
			worldObj.spawnEntityInWorld(item);
	}

	public boolean onWanded(EntityPlayer player, ItemStack stack) {
		return false;
	}

	public int getSignal() {
		return 0;
	}
}
