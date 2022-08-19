/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 23, 2015, 7:21:52 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.item.ISortableTool.ToolType;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

public class ItemSwapRing extends ItemBauble {

	public ItemSwapRing() {
		super(LibItemNames.SWAP_RING);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		if(!(entity instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) entity;
		ItemStack currentStack = player.getCurrentEquippedItem();
		if(currentStack == null || !(currentStack.getItem() instanceof ISortableTool))
			return;

		ISortableTool tool = (ISortableTool) currentStack.getItem();

		MovingObjectPosition pos = ToolCommons.raytraceFromEntity(entity.worldObj, entity, true, 4.5F);
		ToolType typeToFind = null;

		if(player.isSwingInProgress && pos != null) {
			Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);

			if(block != null) {
				Material mat = block.getMaterial();
				if(ToolCommons.isRightMaterial(mat, ToolCommons.materialsPick))
					typeToFind = ToolType.PICK;
				else if(ToolCommons.isRightMaterial(mat, ToolCommons.materialsShovel))
					typeToFind = ToolType.SHOVEL;
				else if(ToolCommons.isRightMaterial(mat, ToolCommons.materialsAxe))
					typeToFind = ToolType.AXE;
			}
		}

		if(typeToFind == null)
			return;

		ItemStack bestTool = currentStack;
		int bestToolPriority = tool.getSortingType(currentStack) == typeToFind ? tool.getSortingPriority(currentStack) : -1;
		int bestSlot = -1;

		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if (stackInSlot != null && stackInSlot.getItem() instanceof ISortableTool && stackInSlot != currentStack) {
				ISortableTool toolInSlot = (ISortableTool) stackInSlot.getItem();
				if(toolInSlot.getSortingType(stackInSlot).equals(typeToFind)) {
					int priority = toolInSlot.getSortingPriority(stackInSlot);
					if(priority > bestToolPriority) {
						bestTool = stackInSlot;
						bestToolPriority = priority;
						bestSlot = i;
					}
				}
			}
		}

		if(bestSlot != -1) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, bestTool);
			player.inventory.setInventorySlotContents(bestSlot, currentStack);
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

}
