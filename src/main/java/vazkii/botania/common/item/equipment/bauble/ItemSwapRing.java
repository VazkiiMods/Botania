/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class ItemSwapRing extends ItemBauble {

	public ItemSwapRing(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.level.isClientSide && !(entity instanceof Player)) {
			return;
		}

		Player player = (Player) entity;
		ItemStack currentStack = player.getMainHandItem();
		if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ISortableTool)
				|| !player.swinging) {
			return;
		}

		ISortableTool tool = (ISortableTool) currentStack.getItem();

		BlockHitResult pos = ToolCommons.raytraceFromEntity(player, 4.5F, false);
		if (pos.getType() != HitResult.Type.BLOCK) {
			return;
		}
		BlockState state = entity.level.getBlockState(pos.getBlockPos());

		ItemStack bestTool = currentStack;
		int bestToolPriority = currentStack.getDestroySpeed(state) > 1.0F ? tool.getSortingPriority(currentStack, state) : -1;
		int bestSlot = -1;

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stackInSlot = player.getInventory().getItem(i);
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
			player.setItemInHand(InteractionHand.MAIN_HAND, bestTool);
			player.getInventory().setItem(bestSlot, currentStack);
		}
	}
}
