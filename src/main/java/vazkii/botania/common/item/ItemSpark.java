/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.entity.EntitySpark;

import javax.annotation.Nonnull;

public class ItemSpark extends Item implements IManaGivingItem {

	public ItemSpark(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof ISparkAttachable) {
			ISparkAttachable attach = (ISparkAttachable) tile;
			ItemStack stack = ctx.getStack();
			if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				if (!world.isClient) {
					stack.decrement(1);
					EntitySpark spark = new EntitySpark(world);
					spark.updatePosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
					world.spawnEntity(spark);
					attach.attachSpark(spark);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
