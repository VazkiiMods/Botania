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

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.item.ISortableTool.ToolType;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class ItemSwapRing extends ItemBauble {

	public ItemSwapRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if(!(entity instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) entity;
		ItemStack currentStack = player.getHeldItemMainhand();
		if(currentStack.isEmpty() || !(currentStack.getItem() instanceof ISortableTool))
			return;

		ISortableTool tool = (ISortableTool) currentStack.getItem();

		RayTraceResult pos = ToolCommons.raytraceFromEntity(player.world, player,
				RayTraceContext.FluidMode.SOURCE_ONLY, 4.5F);
		ToolType typeToFind = null;

		if(player.isSwingInProgress && pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockState state = entity.world.getBlockState(((BlockRayTraceResult) pos).getPos());

			Material mat = state.getMaterial();
			if(ToolCommons.materialsPick.contains(mat))
				typeToFind = ToolType.PICK;
			else if(ToolCommons.materialsShovel.contains(mat))
				typeToFind = ToolType.SHOVEL;
			else if(ToolCommons.materialsAxe.contains(mat))
				typeToFind = ToolType.AXE;
		}

		if(typeToFind == null)
			return;

		ItemStack bestTool = currentStack;
		int bestToolPriority = tool.getSortingType(currentStack) == typeToFind ? tool.getSortingPriority(currentStack) : -1;
		int bestSlot = -1;

		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if(!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof ISortableTool && stackInSlot != currentStack) {
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
			player.setHeldItem(Hand.MAIN_HAND, bestTool);
			player.inventory.setInventorySlotContents(bestSlot, currentStack);
		}
	}
}
