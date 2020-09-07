/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.item.ItemObedienceStick;

import javax.annotation.Nonnull;

public class BehaviourStick extends Bootstrap.BehaviorDispenseOptional {

	@Nonnull
	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		EnumFacing facing = world.getBlockState(source.getBlockPos()).getValue(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);

		successful = ItemObedienceStick.applyStick(world, pos);

		return stack;
	}

}
