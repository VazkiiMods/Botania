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

import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemPoolMinecart extends ItemMod {

	public ItemPoolMinecart(Item.Builder builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		ItemStack stack = ctx.getItem();

		if(BlockRailBase.isRail(world.getBlockState(pos))) {
			if(!world.isRemote) {
				EntityMinecart entityminecart = new EntityPoolMinecart(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

				if(stack.hasDisplayName())
					entityminecart.setCustomName(stack.getDisplayName());

				world.spawnEntity(entityminecart);
			}

			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

}
