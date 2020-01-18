/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 17, 2015, 6:48:29 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class ItemPoolMinecart extends Item {

	public ItemPoolMinecart(Properties builder) {
		super(builder);
	}

	// [VanillaCopy] ItemMinecart
	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState iblockstate = world.getBlockState(blockpos);
		if (!iblockstate.isIn(BlockTags.RAILS)) {
			return ActionResultType.FAIL;
		} else {
			ItemStack itemstack = context.getItem();
			if (!world.isRemote) {
				RailShape railshape = iblockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock)iblockstate.getBlock()).getRailDirection(iblockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
				double d0 = 0.0D;
				if (railshape.isAscending()) {
					d0 = 0.5D;
				}

				AbstractMinecartEntity entityminecart = new EntityPoolMinecart(world, blockpos.getX() + 0.5D, blockpos.getY() + 0.0625D + d0, blockpos.getZ() + 0.5D);
				if (itemstack.hasDisplayName()) {
					entityminecart.setCustomName(itemstack.getDisplayName());
				}

				world.addEntity(entityminecart);
			}

			itemstack.shrink(1);
			return ActionResultType.SUCCESS;
		}
	}

}
