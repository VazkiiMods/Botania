/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
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
		if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ISortableTool)) {
			return;
		}

		ISortableTool tool = (ISortableTool) currentStack.getItem();

		BlockHitResult pos = ToolCommons.raytraceFromEntity(player, 4.5F, false);
		Tag<Item> typeToFind = null;

		if (player.handSwinging && pos.getType() == HitResult.Type.BLOCK) {
			BlockState state = entity.world.getBlockState(pos.getBlockPos());

			Material mat = state.getMaterial();
			if (ToolCommons.materialsPick.contains(mat)) {
				typeToFind = FabricToolTags.PICKAXES;
			} else if (ToolCommons.materialsShovel.contains(mat)) {
				typeToFind = FabricToolTags.SHOVELS;
			} else if (ToolCommons.materialsAxe.contains(mat)) {
				typeToFind = FabricToolTags.AXES;
			}
		}

		if (typeToFind == null) {
			return;
		}

		ItemStack bestTool = currentStack;
		int bestToolPriority = currentStack.getItem().isIn(typeToFind) ? tool.getSortingPriority(currentStack) : -1;
		int bestSlot = -1;

		for (int i = 0; i < player.inventory.size(); i++) {
			ItemStack stackInSlot = player.inventory.getStack(i);
			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof ISortableTool && stackInSlot != currentStack) {
				ISortableTool toolInSlot = (ISortableTool) stackInSlot.getItem();
				if (stackInSlot.getItem().isIn(typeToFind)) {
					int priority = toolInSlot.getSortingPriority(stackInSlot);
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
