/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

// [VanillaCopy] ItemMinecart
public class BehaviourPoolMinecart extends ItemDispenserBehavior {
	private final ItemDispenserBehavior behaviourDefaultDispenseItem = new ItemDispenserBehavior();

	@Nonnull
	@Override
	public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
		World world = source.getWorld();
		double d0 = source.getX() + (double) enumfacing.getOffsetX() * 1.125D;
		double d1 = Math.floor(source.getY()) + (double) enumfacing.getOffsetY();
		double d2 = source.getZ() + (double) enumfacing.getOffsetZ() * 1.125D;
		BlockPos blockpos = source.getBlockPos().offset(enumfacing);
		BlockState iblockstate = world.getBlockState(blockpos);
		RailShape railshape = iblockstate.getBlock() instanceof AbstractRailBlock ? iblockstate.get(((AbstractRailBlock) iblockstate.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
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

			BlockState iblockstate1 = world.getBlockState(blockpos.down());
			RailShape railshape1 = iblockstate1.getBlock() instanceof AbstractRailBlock ? iblockstate1.get(((AbstractRailBlock) iblockstate1.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
			if (enumfacing != Direction.DOWN && railshape1.isAscending()) {
				d3 = -0.4D;
			} else {
				d3 = -0.9D;
			}
		}

		AbstractMinecartEntity entityminecart = new EntityPoolMinecart(world, d0, d1 + d3, d2);
		if (stack.hasCustomName()) {
			entityminecart.setCustomName(stack.getName());
		}

		world.spawnEntity(entityminecart);
		stack.decrement(1);
		return stack;
	}

	@Override
	protected void playSound(BlockPointer source) {
		source.getWorld().syncWorldEvent(1000, source.getBlockPos(), 0);
	}

}
