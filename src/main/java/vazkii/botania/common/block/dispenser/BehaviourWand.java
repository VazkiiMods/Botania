/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import vazkii.botania.api.block.IWandable;

import javax.annotation.Nonnull;

public class BehaviourWand extends OptionalDispenseItemBehavior {

	@Nonnull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.getLevel();
		Direction facing = world.getBlockState(source.getPos()).getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(facing);
		Block block = world.getBlockState(pos).getBlock();
		boolean wandable = block instanceof IWandable;
		setSuccess(wandable);
		if (wandable) {
			((IWandable) block).onUsedByWand(null, stack, world, pos, facing.getOpposite());
		}
		return stack;
	}

}
