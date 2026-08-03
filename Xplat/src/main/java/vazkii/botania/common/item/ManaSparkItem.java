/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.mana.spark.ManaSparkAttachable;
import vazkii.botania.api.mana.spark.ManaSparkHelper;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.helper.EntityHelper;

public class ManaSparkItem extends Item {

	public ManaSparkItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack otherHandStack = ItemStack.EMPTY;
		if (context.getPlayer() != null) {
			otherHandStack = context.getPlayer().getItemInHand(EntityHelper.otherHand(context.getHand()));
			if (context.getPlayer().isCreative()) {
				otherHandStack = otherHandStack.copy();
			}
		}
		return attachSpark(context.getLevel(), context.getClickedPos(), context.getItemInHand(), otherHandStack)
				? InteractionResult.sidedSuccess(context.getLevel().isClientSide())
				: InteractionResult.PASS;
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack, ItemStack otherHandStack) {
		var attach = ManaSparkAttachable.LOOKUP.find(world, pos);
		if (attach != null) {
			if (attach.canAttachSpark(stack) && ManaSparkHelper.getAttachedSpark(world, pos) == null) {
				if (!world.isClientSide) {
					stack.shrink(1);
					ManaSparkEntity spark = new ManaSparkEntity(world);
					spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
					if (otherHandStack.getItem() instanceof DyeItem dye) {
						otherHandStack.shrink(1);
						spark.setNetwork(dye.getDyeColor());
					}
					world.addFreshEntity(spark);
					attach.attachSpark(spark);
				}
				return true;
			}
		}
		return false;
	}
}
