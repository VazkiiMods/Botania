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
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.xplat.XplatAbstractions;

public class ManaSparkItem extends Item {

	public ManaSparkItem(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack otherHandStack = ItemStack.EMPTY;
		if (ctx.getPlayer() != null) {
			otherHandStack = ctx.getPlayer().getItemInHand(EntityHelper.otherHand(ctx.getHand()));
			if (ctx.getPlayer().isCreative()) {
				otherHandStack = otherHandStack.copy();
			}
		}
		return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand(), otherHandStack)
				? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide)
				: InteractionResult.PASS;
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack, ItemStack otherHandStack) {
		var attach = XplatAbstractions.INSTANCE.findSparkAttachable(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), Direction.UP);
		if (attach != null) {
			if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
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
