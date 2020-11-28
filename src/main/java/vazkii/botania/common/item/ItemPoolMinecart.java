/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class ItemPoolMinecart extends Item {

	public ItemPoolMinecart(Settings builder) {
		super(builder);
	}

	// [VanillaCopy] MinecartItem
	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (!blockState.isIn(BlockTags.RAILS)) {
			return ActionResult.FAIL;
		} else {
			ItemStack itemStack = context.getStack();
			if (!world.isClient) {
				RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock ? blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
				double d = 0.0D;
				if (railShape.isAscending()) {
					d = 0.5D;
				}

				AbstractMinecartEntity abstractMinecartEntity = new EntityPoolMinecart(world, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.0625D + d, (double) blockPos.getZ() + 0.5D);
				if (itemStack.hasCustomName()) {
					abstractMinecartEntity.setCustomName(itemStack.getName());
				}

				world.spawnEntity(abstractMinecartEntity);
			}

			itemStack.decrement(1);
			return ActionResult.success(world.isClient);
		}
	}

}
