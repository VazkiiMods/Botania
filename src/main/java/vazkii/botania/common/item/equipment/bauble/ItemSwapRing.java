/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class ItemSwapRing extends ItemBauble {

	public ItemSwapRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.world.isRemote && !(entity instanceof PlayerEntity)) {
			return;
		}

		PlayerEntity player = (PlayerEntity) entity;
		ItemStack currentStack = player.getHeldItemMainhand();
		if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ISortableTool)
				|| !player.isSwingInProgress) {
			return;
		}

		ISortableTool tool = (ISortableTool) currentStack.getItem();
		BlockRayTraceResult pos = ToolCommons.raytraceFromEntity(player, 4.5F, false);
		if (pos.getType() != RayTraceResult.Type.BLOCK) {
			return;
		}
		BlockState state = entity.world.getBlockState(pos.getPos());

		ItemStack bestTool = currentStack;
		int bestToolPriority = currentStack.getDestroySpeed(state) > 1.0F ? tool.getSortingPriority(currentStack, state) : -1;
		int bestSlot = -1;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(i);
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof ISortableTool && stackInSlot != currentStack) {
				ISortableTool toolInSlot = (ISortableTool) stackInSlot.getItem();
				if (stackInSlot.getDestroySpeed(state) > 1.0F) {
					int priority = toolInSlot.getSortingPriority(stackInSlot, state);
					if (priority > bestToolPriority) {
						bestTool = stackInSlot;
						bestToolPriority = priority;
						bestSlot = i;
					}
				}
			}
		}

		if (bestSlot != -1) {
			player.setHeldItem(Hand.MAIN_HAND, bestTool);
			player.inventory.setInventorySlotContents(bestSlot, currentStack);
		}
	}
}
