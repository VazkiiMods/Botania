/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 18, 2015, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

// [VanillaCopy] ItemMinecart
public class BehaviourPoolMinecart extends BehaviorDefaultDispenseItem {
	private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		EnumFacing enumfacing = source.getBlockState().get(BlockDispenser.FACING);
		World world = source.getWorld();
		double d0 = source.getX() + (double)enumfacing.getXOffset() * 1.125D;
		double d1 = Math.floor(source.getY()) + (double)enumfacing.getYOffset();
		double d2 = source.getZ() + (double)enumfacing.getZOffset() * 1.125D;
		BlockPos blockpos = source.getBlockPos().offset(enumfacing);
		IBlockState iblockstate = world.getBlockState(blockpos);
		RailShape railshape = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase)iblockstate.getBlock()).getRailDirection(iblockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
		double d3;
		if (iblockstate.isIn(BlockTags.RAILS)) {
			if (railshape.isAscending()) {
				d3 = 0.6D;
			} else {
				d3 = 0.1D;
			}
		} else {
			if (!iblockstate.isAir() || !world.getBlockState(blockpos.down()).isIn(BlockTags.RAILS)) {
				return this.behaviourDefaultDispenseItem.dispense(source, stack);
			}

			IBlockState iblockstate1 = world.getBlockState(blockpos.down());
			RailShape railshape1 = iblockstate1.getBlock() instanceof BlockRailBase ? ((BlockRailBase)iblockstate1.getBlock()).getRailDirection(iblockstate1, world, blockpos, null) : RailShape.NORTH_SOUTH;
			if (enumfacing != EnumFacing.DOWN && railshape1.isAscending()) {
				d3 = -0.4D;
			} else {
				d3 = -0.9D;
			}
		}

		EntityMinecart entityminecart = new EntityPoolMinecart(world, d0, d1 + d3, d2);
		if (stack.hasDisplayName()) {
			entityminecart.setCustomName(stack.getDisplayName());
		}

		world.spawnEntity(entityminecart);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	}

}
