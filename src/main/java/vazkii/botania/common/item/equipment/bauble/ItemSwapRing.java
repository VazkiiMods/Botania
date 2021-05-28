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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class ItemSwapRing extends ItemBauble {

	public ItemSwapRing(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.world.isClient && !(entity instanceof PlayerEntity)) {
			return;
		}

		PlayerEntity player = (PlayerEntity) entity;
		ItemStack currentStack = player.getMainHandStack();
		if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ISortableTool)
				|| !player.handSwinging) {
			return;
		}

		ISortableTool tool = (ISortableTool) currentStack.getItem();

		BlockHitResult pos = ToolCommons.raytraceFromEntity(player, 4.5F, false);
		if (pos.getType() != HitResult.Type.BLOCK) {
			return;
		}
		BlockState state = entity.world.getBlockState(pos.getBlockPos());

		ItemStack bestTool = currentStack;
		int bestToolPriority = currentStack.getMiningSpeedMultiplier(state) > 1.0F ? tool.getSortingPriority(currentStack, state) : -1;
		int bestSlot = -1;

		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack stackInSlot = player.inventory.getStack(i);
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof ISortableTool && stackInSlot != currentStack) {
				ISortableTool toolInSlot = (ISortableTool) stackInSlot.getItem();
				if (stackInSlot.getMiningSpeedMultiplier(state) > 1.0F) {
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
			player.setStackInHand(Hand.MAIN_HAND, bestTool);
			player.inventory.setStack(bestSlot, currentStack);
		}
	}
}
