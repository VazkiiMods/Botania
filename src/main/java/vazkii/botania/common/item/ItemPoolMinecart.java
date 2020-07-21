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

	// [VanillaCopy] ItemMinecart
	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getBlockPos();
		BlockState iblockstate = world.getBlockState(blockpos);
		if (!iblockstate.isIn(BlockTags.RAILS)) {
			return ActionResult.FAIL;
		} else {
			ItemStack itemstack = context.getStack();
			if (!world.isClient) {
				RailShape railshape = iblockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) iblockstate.getBlock()).getRailDirection(iblockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
				double d0 = 0.0D;
				if (railshape.isAscending()) {
					d0 = 0.5D;
				}

				AbstractMinecartEntity entityminecart = new EntityPoolMinecart(world, blockpos.getX() + 0.5D, blockpos.getY() + 0.0625D + d0, blockpos.getZ() + 0.5D);
				if (itemstack.hasCustomName()) {
					entityminecart.setCustomName(itemstack.getName());
				}

				world.spawnEntity(entityminecart);
			}

			itemstack.decrement(1);
			return ActionResult.SUCCESS;
		}
	}

}
