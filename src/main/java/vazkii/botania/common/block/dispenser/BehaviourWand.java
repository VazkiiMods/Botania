/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;

public class BehaviourWand extends OptionalDispenseBehavior {

	@Nonnull
	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		Direction facing = world.getBlockState(source.getBlockPos()).get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		Block block = world.getBlockState(pos).getBlock();
		boolean wandable = block instanceof IWandable;
		setSuccessful(wandable);
		if (wandable) {
			((IWandable) block).onUsedByWand(null, stack, world, pos, facing.getOpposite());
		}
		return stack;
	}

}
