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
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

// [VanillaCopy] ItemMinecart
public class BehaviourPoolMinecart extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
		World world = source.getWorld();
		double d0 = source.getX() + (double) enumfacing.getXOffset() * 1.125D;
		double d1 = Math.floor(source.getY()) + (double) enumfacing.getYOffset();
		double d2 = source.getZ() + (double) enumfacing.getZOffset() * 1.125D;
		BlockPos blockpos = source.getBlockPos().offset(enumfacing);
		BlockState iblockstate = world.getBlockState(blockpos);
		RailShape railshape = iblockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) iblockstate.getBlock()).getRailDirection(iblockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
		double d3;
		if (iblockstate.func_235714_a_(BlockTags.RAILS)) {
			if (railshape.isAscending()) {
				d3 = 0.6D;
			} else {
				d3 = 0.1D;
			}
		} else {
			if (!iblockstate.isAir() || !world.getBlockState(blockpos.down()).func_235714_a_(BlockTags.RAILS)) {
				return this.behaviourDefaultDispenseItem.dispense(source, stack);
			}

			BlockState iblockstate1 = world.getBlockState(blockpos.down());
			RailShape railshape1 = iblockstate1.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) iblockstate1.getBlock()).getRailDirection(iblockstate1, world, blockpos, null) : RailShape.NORTH_SOUTH;
			if (enumfacing != Direction.DOWN && railshape1.isAscending()) {
				d3 = -0.4D;
			} else {
				d3 = -0.9D;
			}
		}

		AbstractMinecartEntity entityminecart = new EntityPoolMinecart(world, d0, d1 + d3, d2);
		if (stack.hasDisplayName()) {
			entityminecart.setCustomName(stack.getDisplayName());
		}

		world.addEntity(entityminecart);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	}

}
