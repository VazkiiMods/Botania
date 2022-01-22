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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.entity.EntityManaSpark;

import javax.annotation.Nonnull;

public class ItemManaSpark extends Item {

	public ItemManaSpark(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand()) ? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide) : InteractionResult.PASS;
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof ISparkAttachable attach) {
			if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				if (!world.isClientSide) {
					stack.shrink(1);
					EntityManaSpark spark = new EntityManaSpark(world);
					spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
					world.addFreshEntity(spark);
					attach.attachSpark(spark);
				}
				return true;
			}
		}
		return false;
	}
}
